package yummyloop.example.entity

import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.mob.EndermanEntity
import net.minecraft.entity.mob.SpiderEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.world.World

abstract class AbstractCobwebProjectileEntity : AbstractProjectileEntity{
    protected constructor(entityType: EntityType<out AbstractProjectileEntity>, world: World)
            : super(entityType, world){ ini() }
    protected constructor(entityType: EntityType<out AbstractProjectileEntity>, x: Double, y: Double, z: Double, world: World)
            : super(entityType, x,y,z, world){ ini() }
    protected constructor(entityType: EntityType<out AbstractProjectileEntity>, livingEntity: LivingEntity, world: World)
            : super (entityType, livingEntity, world){ ini() }
    protected constructor(entityType: EntityType<out AbstractProjectileEntity>, livingEntity: LivingEntity, world: World, stack : ItemStack)
            : super(entityType,livingEntity,world, stack){
        ini()
        defaultItemStack=stack.copy()
    }

    private fun ini(){
        this.dragInAir=0.85f
        this.dragInWater=0.25f
    }
    private var hitEntity = false
    override fun isAttackable(): Boolean = true
    override fun isPushable(): Boolean = true
    override fun collides(): Boolean = !this.removed
    private fun getDrag() : Float = if (this.isInsideWater) dragInWater else dragInAir
    //override fun getHardCollisionBox(entity: Entity): Box? = if (entity.isPushable) entity.boundingBox else null
    override fun getHitSound(): SoundEvent = SoundEvents.ENTITY_FOX_SPIT

    // Makes the entity a solid similar to a block
    override fun getCollisionBox(): Box? = null//this.boundingBox

    private var defaultItemStack=ItemStack.EMPTY
    override fun asItemStack(): ItemStack {
        return defaultItemStack
    }

    override fun pushAwayFrom(entity: Entity) {
        this.hitEntity = true
        this.isSilent = true
        if (entity is AbstractCobwebProjectileEntity || entity is SpiderEntity) return
        if (!this.world.isClient) {
            if (!entity.noClip && !this.noClip) {
                this.setPosition(entity.x, entity.y+0.01, entity.z)
                this.setVelocity(0.0,0.05000000074505806, 0.0)
                this.inGround=false
                this.life = 0
                this.flyingTick = 0
                this.setEffects(entity)
            }
        }
    }

    override fun handleAttack(entity: Entity?): Boolean {
        shake+=10
        if (entity != null) {
            this.velocity = entity.rotationVector
        }
        return true
    }

    override fun tick() {
        if (shake>33){
            if(!isOnFire) this.dropItemStack(ItemStack(Items.STRING))
        }
        if (this.isOnFire) {
            shake+=7
        } else if (!this.isFireImmune && this.world.doesAreaContainFireSource(this.boundingBox.expand(1.1))) {
            this.setOnFireFor(8)
            shake+=7
        }
        super.tick()
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        val entityHit = entityHitResult.entity

        if (entityHit is AbstractCobwebProjectileEntity || entityHit is SpiderEntity) return
        this.hitEntity = true
        this.setEffects(entityHit)

        // Set projectile velocity after hitting an entity
        this.velocity = this.velocity.multiply(0.25, 0.25, 0.25)
        //this.remove()
    }

    override fun onBlockCollision(collisionBlockState: BlockState?) {
        if (!world.isClient){
            val currentBlockPos = BlockPos(this)
            val currentBlock = world.getBlockState(currentBlockPos)

            val chance = if (hitEntity) 0.001f else 0.05f
            if (currentBlock.isAir && inGround && (random.nextFloat() < chance)){
                this.placeBlock(currentBlockPos, Blocks.COBWEB)
                this.remove()
            }
        }
    }

    private fun setEffects(entityHit : Entity){
        if (this.isOnFire && entityHit !is EndermanEntity) {
            entityHit.setOnFireFor(5)
        }

        if ((entityHit is LivingEntity) && (entityHit !is SpiderEntity)) {
            entityHit.movementSpeed = entityHit.movementSpeed * 0.1F
            entityHit.addPotionEffect(StatusEffectInstance(StatusEffects.SLOWNESS, 50, 3, false, false, true))
            if (random.nextInt(100) > 95) {
                entityHit.addPotionEffect(StatusEffectInstance(StatusEffects.POISON, 60, 1, false, false, true))
            } else {
                entityHit.addPotionEffect(StatusEffectInstance(StatusEffects.MINING_FATIGUE, 60, 1, false, false, true))
            }
        }
    }
}