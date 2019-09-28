package yummyloop.example.item.spear

import net.fabricmc.fabric.api.client.render.EntityRendererRegistry
import net.minecraft.block.BlockState
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.stat.Stats
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import yummyloop.example.item.Item
import yummyloop.example.item.Items
import yummyloop.example.item.entity.AbstractCobwebProjectileEntity
import yummyloop.example.item.entity.AbstractSpearEntity
import yummyloop.example.item.entity.AbstractProjectileEntity
import yummyloop.example.render.entity.ThrownItemEntityRenderer
import yummyloop.example.util.registry.ClientManager
import yummyloop.example.util.registry.RegistryManager

object Cobweb : Item("cobweb", Settings().group(ItemGroup.COMBAT).maxCount(16)) {

    const val itemName = "cobweb"
    private const val velocityMod = 1.0f

    init {
        this.addPropertyGetter(Identifier("throwing")) { itemStack, world, livingEntity -> if (livingEntity != null && livingEntity.isUsingItem && livingEntity.activeItem == itemStack) 1.0f else 0.0f }
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

     private fun throwProjectile(player : PlayerEntity, stack: ItemStack){
        val thrownEntity = getThrownEntity(player, stack)
        if (thrownEntity is AbstractSpearEntity){
            thrownEntity.setItem(stack)
        }
        // set projectile velocity
        thrownEntity.setProperties(player, player.pitch, player.yaw, 0.0f, this.velocityMod*(2.5f), 1.0f)

        // pickup permissions
        thrownEntity.pickupPermission = AbstractProjectileEntity.PickupPermission.CREATIVE_ONLY

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

    override fun getUseAction(itemStack_1: ItemStack): UseAction {
        return UseAction.SPEAR
    }

    override fun getMaxUseTime(itemStack_1: ItemStack): Int {
        return 72000
    }

     private fun getThrownEntity(player: PlayerEntity, stack: ItemStack): AbstractProjectileEntity {
        return InternalEntity(player.world, player, stack)
    }

    class InternalEntity : AbstractCobwebProjectileEntity {
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
                : super(registeredType, livingEntity, world, stack)
        constructor(world: World, x: Double, y: Double, z: Double)
                : super(registeredType, x, y, z, world)

    }
}