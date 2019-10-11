package yummyloop.example.entity

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.api.EnvironmentInterface
import net.fabricmc.api.EnvironmentInterfaces
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.EntityType
import net.minecraft.entity.FlyingItemEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.SystemUtil
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import yummyloop.example.entity.EntityHelper.channelingEnchant

@EnvironmentInterfaces(EnvironmentInterface(value = EnvType.CLIENT, itf = FlyingItemEntity::class))
abstract class AbstractSpearEntity : ProjectileEntity, FlyingItemEntity {
    companion object{
        private val ITEM: TrackedData<ItemStack> = DataTracker.registerData(AbstractSpearEntity::class.java, TrackedDataHandlerRegistry.ITEM_STACK)
        private val loyalty: TrackedData<Byte> = DataTracker.registerData<Byte>(AbstractSpearEntity::class.java, TrackedDataHandlerRegistry.BYTE)
    }
    protected abstract var attackDamage: Float
    private var istack: ItemStack = ItemStack.EMPTY
    private var dealtDamage = false
    private var loyaltyTick: Int = 0
    override fun getDragInWater(): Float = 0.90f
    private fun getLoyalty(): TrackedData<Byte> = loyalty
    override fun getSound(): SoundEvent? = SoundEvents.ITEM_TRIDENT_HIT_GROUND

    constructor(entityType : EntityType<out AbstractSpearEntity>, world : World)
            : super(entityType, world)

    constructor(entityType : EntityType<out AbstractSpearEntity>, world : World, livingEntity : LivingEntity, stack : ItemStack)
            : super(entityType, livingEntity, world){
        this.istack = stack.copy()
        this.setItem(this.istack)
        this.dataTracker.set(getLoyalty(), EnchantmentHelper.getLoyalty(stack).toByte())
    }

    constructor(entityType : EntityType<out AbstractSpearEntity>, world: World, x: Double, y: Double, z: Double)
            : super(entityType, x, y, z, world)

    override fun asItemStack(): ItemStack {
        val i = this.getItem()
        return if (i.isEmpty) ItemStack(this.getDefaultItem()) else i.copy()
    }

    override fun initDataTracker() {
        super.initDataTracker()
        this.dataTracker.startTracking(getLoyalty(), 0.toByte())
        this.getDataTracker().startTracking(ITEM, ItemStack.EMPTY)
    }

    override fun tick() {
        val catchSound = SoundEvents.ITEM_TRIDENT_RETURN
        if (this.inGroundTime > 4) {
            this.dealtDamage = true
        }

        if (this.isOnFire) {
            this.burn(1)
        } else if (!this.isFireImmune && this.world.doesAreaContainFireSource(this.boundingBox.expand(1.25))) {
            this.setOnFireFor(8)
            this.burn(1)
        }

        if (this.dealtDamage || this.isNoClip) {
            loyaltyBehavior(catchSound)
        }
        super.tick()
    }

    private fun loyaltyBehavior(catchSound : SoundEvent){
        val owner = this.owner
        if (owner != null) {
            val loyalty = (this.dataTracker.get<Byte>(getLoyalty()) as Byte).toInt()
            when {
                loyalty > 0 && !this.isOwnerAlive() -> {
                    if (!this.world.isClient && this.pickupType == PickupPermission.ALLOWED) {
                        this.dropStack(this.asItemStack(), 0.1f)
                    }
                    this.remove()
                }
                loyalty > 0 -> {
                    this.isNoClip = true
                    val pos = Vec3d(owner.x - this.x, owner.y + owner.standingEyeHeight.toDouble() - this.y, owner.z - this.z)
                    this.y += pos.y * 0.015 * loyalty.toDouble()
                    if (this.world.isClient) {
                        this.prevRenderY = this.y
                    }

                    val loyaltyPart = 0.05 * loyalty.toDouble()
                    this.velocity = this.velocity.multiply(0.95).add(pos.normalize().multiply(loyaltyPart))
                    if (this.loyaltyTick == 0) {
                        this.playSound(catchSound, 10.0f, 1.0f)
                    }
                    ++this.loyaltyTick
                }
            }
        }
    }

    override fun getEntityCollision(vec3d_1: Vec3d, vec3d_2: Vec3d): EntityHitResult? {
        return if (this.dealtDamage) null else super.getEntityCollision(vec3d_1, vec3d_2)
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        val hitSound = SoundEvents.ITEM_TRIDENT_HIT
        val hitThunderSound = SoundEvents.ITEM_TRIDENT_THUNDER
        effectiveOnEntityHit(entityHitResult, hitSound, hitThunderSound)
    }

    protected open fun effectiveOnEntityHit(entityHitResult: EntityHitResult, hitSound : SoundEvent, hitThunderSound : SoundEvent){
        val entityHit = entityHitResult.entity
        val owner = this.owner
        var effectiveAttackDamage=this.attackDamage-1
        val damageSource = DamageSource.trident(this, owner)
        this.dealtDamage = true

        // Enchanted damage calculation
        if (entityHit is LivingEntity) {
            effectiveAttackDamage += EnchantmentHelper.getAttackDamage(this.asItemStack(), entityHit.group)
            if (this.isOnFire){
                entityHit.setOnFireFor(4)
            }
        }

        // Hit and damage an entity
        if (entityHit.damage(damageSource, effectiveAttackDamage) && entityHit is LivingEntity) {
            if (owner is LivingEntity) {
                EnchantmentHelper.onUserDamaged(entityHit, owner)
                EnchantmentHelper.onTargetDamaged(owner, entityHit)
            }
            this.onHit(entityHit)
        }

        // Turn backwards after hitting an entity
        this.velocity = this.velocity.multiply(-0.01, -0.01, -0.01)

        // Channeling enchantment behaviour
        if (!channelingEnchant(entityHit, owner, hitThunderSound, this.asItemStack())) {
            this.playSound(hitSound, 1.0f, 1.0f)
        }
    }

    private fun isOwnerAlive(): Boolean {
        val owner = this.owner
        return when {
            owner != null && owner.isAlive -> !(owner is ServerPlayerEntity && owner.isSpectator)
            else -> false
        }
    }

    override fun onPlayerCollision(player: PlayerEntity) {
        val owner = this.owner
        if (owner == null || owner.uuid === player.uuid) {
            super.onPlayerCollision(player)
        }
    }

    override fun readCustomDataFromTag(compoundTag_1: CompoundTag) {
        super.readCustomDataFromTag(compoundTag_1)
        this.istack = ItemStack.fromTag(compoundTag_1.getCompound("Spear"))

        this.dealtDamage = compoundTag_1.getBoolean("DealtDamage")
        this.dataTracker.set(getLoyalty(), EnchantmentHelper.getLoyalty(this.istack).toByte())
        this.setItem(istack)
    }

    override fun writeCustomDataToTag(compoundTag_1: CompoundTag) {
        super.writeCustomDataToTag(compoundTag_1)
        compoundTag_1.put("Spear", this.getItem().toTag(CompoundTag()))
        compoundTag_1.putBoolean("DealtDamage", this.dealtDamage)
    }

    override fun age() {
        val loyalty = (this.dataTracker.get<Byte>(getLoyalty()) as Byte).toInt()
        if (this.pickupType != PickupPermission.ALLOWED || loyalty <= 0) {
            super.age()
        }
    }

    @Environment(EnvType.CLIENT)
    override fun shouldRenderFrom(double_1: Double, double_2: Double, double_3: Double): Boolean = true

    @Environment(EnvType.CLIENT)
    override fun getStack(): ItemStack {
        val stack = this.getItem()
        return if (stack.isEmpty) ItemStack(this.getDefaultItem()) else stack
    }

    protected fun getItem(): ItemStack = this.getDataTracker().get<ItemStack>(ITEM) as ItemStack

    protected fun getDefaultItem(): Item = this.istack.item

    fun setItem(itemStack_1: ItemStack) =
            this.getDataTracker().set(ITEM, SystemUtil.consume(itemStack_1.copy(), { itemStack_1x -> itemStack_1x.count = 1 }))

    private var damagedTick = 0
    override fun damage(damageSource: DamageSource, damage: Float): Boolean {
        return if (this.isInvulnerableTo(damageSource)) {
            false
        } else {
            this.scheduleVelocityUpdate()
            if (!world.isClient) {
                damagedTick++
                if (damagedTick > 40) {
                    damagedTick = 0
                    if (istack.damage < istack.maxDamage) {
                        istack.damage(1, random, null)
                        this.setItem(istack)
                    } else {
                        this.remove()
                    }
                }
            }
            true
        }
    }

}