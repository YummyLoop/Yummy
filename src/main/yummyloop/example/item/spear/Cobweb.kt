package yummyloop.example.item.spear

import net.fabricmc.fabric.api.client.render.EntityRendererRegistry
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.stat.Stats
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import yummyloop.example.item.entity.AbstractSpearEntity
import yummyloop.example.item.entity.EntityHelper.channelingEnchant
import yummyloop.example.render.entity.ThrownItemEntityRenderer
import yummyloop.example.util.registry.ClientManager
import yummyloop.example.util.registry.RegistryManager

object Cobweb : AbstractSpear("cobweb", Settings().group(ItemGroup.COMBAT).maxCount(16)) {

    override val attackDamage = 1F
    override val attackSpeed = 1F
    override val velocityMod = SpearSettings.Wooden.velocityMod

    init {
        InternalEntity
        ClientManager.registerEntityRenderer(InternalEntity::class.java) //look for ways to replace the need for a class or use byteBuddy ?
        { entityRenderDispatcher: EntityRenderDispatcher, context: EntityRendererRegistry.Context -> ThrownItemEntityRenderer(entityRenderDispatcher, context, this) }
    }

    override fun use(world_1: World?, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = player.getStackInHand(hand)
        player.setCurrentHand(hand)
        return TypedActionResult(ActionResult.SUCCESS, stack)
    }

    override fun onStoppedUsing(stack: ItemStack, world: World?, player: LivingEntity?, useTimeLeft: Int) {
        if (player is PlayerEntity) {
            val useTime = this.getMaxUseTime(stack) - useTimeLeft
            if (useTime < 10) return

            if (!world!!.isClient) {
                throwProjectile(player, stack)
            }
            player.incrementStat(Stats.USED.getOrCreateStat(this))
        }
    }

    override fun throwProjectile(player : PlayerEntity, stack: ItemStack){
        val thrownEntity = getThrownEntity(player, stack)
        if (thrownEntity is AbstractSpearEntity){
            thrownEntity.setItem(stack)
        }
        // set projectile velocity
        thrownEntity.setProperties(player, player.pitch, player.yaw, 0.0f, this.velocityMod*(2.5f), 1.0f)

        // pickup permissions
        thrownEntity.pickupType = ProjectileEntity.PickupPermission.CREATIVE_ONLY

        if (!player.isCreative) {
            //player.inventory.removeOne(stack) // remove 1 stack
            stack.decrement(1) // reduce stack amount by 1
        }

        player.world.spawnEntity(thrownEntity)
        //player.world.playSoundFromEntity(null, thrownEntity, getThrowSound(), SoundCategory.PLAYERS, 1.0f, 1.0f)
    }

    override fun postHit(itemStack_1: ItemStack, livingEntity_1: LivingEntity, livingEntity_2: LivingEntity): Boolean {
        return false
    }

    override fun postMine(itemStack_1: ItemStack, world_1: World, blockState_1: BlockState, blockPos_1: BlockPos, livingEntity_1: LivingEntity): Boolean {
        return false
    }

    override fun isDamageable(): Boolean {
        return false
    }

    override fun getThrownEntity(player: PlayerEntity, stack: ItemStack): ProjectileEntity {
        return InternalEntity(player.world, player, stack)
    }

    class InternalEntity : AbstractSpearEntity {
        companion object{
            private val registeredType= RegistryManager.registerMiscEntityType(
                    (itemName +"_entity"),
                    { entity: EntityType<InternalEntity>, world : World-> InternalEntity(entity, world) },
                    { world: World, x: Double, y: Double, z: Double -> InternalEntity(world, x,y,z) },
                    RegistryManager.EntitySettings().size(0.85F, 0.85F))
        }

        constructor(internalEntityType : EntityType<InternalEntity>, world : World)
                : super(internalEntityType, world)
        constructor(world : World, livingEntity : LivingEntity, stack : ItemStack)
                : super(registeredType, world, livingEntity, stack)
        constructor(world: World, x: Double, y: Double, z: Double)
                : super(registeredType, world, x, y, z)

        override var attackDamage: Float = 0F

        override fun onEntityHit(entityHitResult: EntityHitResult) {
            super.onEntityHit(entityHitResult)

            val entityHit = entityHitResult.entity
            if (entityHit is LivingEntity) {
                entityHit.movementSpeed = entityHit.movementSpeed * 0.1F
                entityHit.addPotionEffect(StatusEffectInstance(StatusEffects.SLOWNESS, 50, 3, false, false, true))
                if (random.nextInt(100) > 91){
                    entityHit.addPotionEffect(StatusEffectInstance(StatusEffects.POISON, 60, 1, false, false, true))
                }else{
                    entityHit.addPotionEffect(StatusEffectInstance(StatusEffects.MINING_FATIGUE, 60, 1, false, false, true))
                }
            }
            // Set projectile velocity after hitting an entity
            this.velocity = this.velocity.multiply(0.25, 0.25, 0.25)
            //this.remove()
        }

        override fun onBlockCollision(collisionBlockState: BlockState?) {
            if (!world.isClient){
                val currentBlockPos = BlockPos(this)
                val currentBlock = world.getBlockState(currentBlockPos)

                if (currentBlock.isAir && inGround && (random.nextInt(100) > 96)){
                    world.setBlockState(currentBlockPos, Blocks.COBWEB.defaultState, 2)
                    world.playLevelEvent(2001, currentBlockPos, Block.getRawIdFromState(currentBlock))
                    this.remove()
                }
            }
        }

        private var health = 15
        override fun damage(damageSource: DamageSource, damage: Float): Boolean {
            return if (this.isInvulnerableTo(damageSource)) {
                false
            } else {
                this.scheduleVelocityUpdate()
                if (!world.isClient){
                    this.health = (this.health.toFloat() - damage).toInt()
                    if (this.health <= 0) {
                        this.remove()
                    }
                }
                false
            }
        }


        override fun effectiveOnEntityHit(entityHitResult: EntityHitResult, hitSound: SoundEvent, hitThunderSound: SoundEvent) {
            // Channeling enchantment behaviour
            channelingEnchant(entityHitResult.entity, owner, hitThunderSound, this.asItemStack())
        }

        override fun getSound(): SoundEvent {
            return SoundEvents.ENTITY_FOX_SPIT
        }
    }
}