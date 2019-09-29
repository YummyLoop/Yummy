package yummyloop.example.entity

import com.google.common.collect.Lists
import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.advancement.criterion.Criterions
import net.minecraft.block.BlockState
import net.minecraft.client.network.packet.EntitySpawnS2CPacket
import net.minecraft.client.network.packet.GameStateChangeS2CPacket
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.*
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.mob.EndermanEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.Projectile
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.Packet
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.TagHelper
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.*
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import java.util.*
import kotlin.experimental.and
import kotlin.experimental.or

abstract class AbstractProjectileEntity : Entity, Projectile {

    protected constructor(entityType: EntityType<out AbstractProjectileEntity>, world: World)
            : super(entityType, world) {
        this.pickupPermission = PickupPermission.DISALLOWED
        this.damage = 2.0
        this.sound = this.getHitSound()
    }
    protected constructor(entityType: EntityType<out AbstractProjectileEntity>, x: Double, y: Double, z: Double, world: World)
            : this(entityType, world) {
        this.setPosition(x, y, z)
    }

    protected constructor(entityType: EntityType<out AbstractProjectileEntity>, livingEntity: LivingEntity, world: World)
            : this(entityType, livingEntity.x, livingEntity.y + livingEntity.standingEyeHeight.toDouble() - 0.10000000149011612, livingEntity.z, world) {
        this.owner = livingEntity
        if (livingEntity is PlayerEntity) {
            this.pickupPermission = PickupPermission.ALLOWED
        }
    }

    protected constructor(entityType: EntityType<out AbstractProjectileEntity>, livingEntity: LivingEntity, world: World, stack : ItemStack)
            : this(entityType,livingEntity,world)

    private var inBlockState: BlockState? = null
    protected var inGround: Boolean = false
    protected var inGroundTime: Int = 0
    var pickupPermission: PickupPermission
    var shake: Int = 0
    var ownerUuid: UUID? = null
    protected var life: Int = 0
    protected var flyingTick: Int = 0
    protected var damage: Double
    private var punchLevel: Int = 0
    private var sound: SoundEvent? = null
    private var piercedEntities: IntOpenHashSet? = null
    private var piercingKilledEntities: MutableList<Entity>? = null
    protected var dragInWater: Float = 0.6f
    protected var dragInAir : Float = 0.99f

    var owner: Entity?
        get() = if (this.ownerUuid != null && this.world is ServerWorld) {
            (this.world as ServerWorld).getEntity(this.ownerUuid)
        } else null
        set(entity) {
            this.ownerUuid = entity?.uuid
            if (entity is PlayerEntity) {
                this.pickupPermission = if (entity.abilities.creativeMode) PickupPermission.CREATIVE_ONLY else PickupPermission.ALLOWED
            }
        }

    var isCritical: Boolean
        get() = (this.dataTracker.get(PROJECTILE_FLAGS) and 1) != 0.toByte()
        set(value) = this.setProjectileFlag(1, value)

    var isShotFromCrossbow: Boolean
        get() = (this.dataTracker.get(PROJECTILE_FLAGS) and 4) != 0.toByte()
        set(value) = this.setProjectileFlag(4, value)

    var pierceLevel: Byte
        get() = this.dataTracker.get(PIERCE_LEVEL)
        set(value) = this.dataTracker.set(PIERCE_LEVEL, value)

    var isNoClip: Boolean
        get() = if (!this.world.isClient) {
            this.noClip
        } else {
            (this.dataTracker.get(PROJECTILE_FLAGS) and 2) != 0.toByte()
        }
        set(value) {
            this.noClip = value
            this.setProjectileFlag(2, value)
        }

    protected abstract fun asItemStack(): ItemStack

    protected open fun getHitSound(): SoundEvent = SoundEvents.ENTITY_ARROW_HIT
    override fun getEyeHeight(entityPose: EntityPose?, entityDimensions: EntityDimensions): Float = 0.0f
    override fun isAttackable(): Boolean = false
    final override fun canClimb(): Boolean = false
    final override fun createSpawnPacket(): Packet<*> = EntitySpawnS2CPacket(this, this.owner?.entityId ?: 0)
    @Environment(EnvType.CLIENT)
    override fun shouldRenderAtDistance(distance: Double): Boolean = true

    @Environment(EnvType.CLIENT)
    override fun updateTrackedPositionAndAngles(x: Double, y: Double, z: Double, pitch: Float, yaw: Float, i1: Int, b1: Boolean) {
        this.setPosition(x, y, z)
        this.setRotation(pitch, yaw)
    }

    @Environment(EnvType.CLIENT)
    override fun setVelocityClient(x: Double, y: Double, z: Double) {
        this.setVelocity(x, y, z)
        if (this.prevPitch == 0.0f && this.prevYaw == 0.0f) {
            val xzNorm = MathHelper.sqrt(x * x + z * z)
            this.pitch = Math.toDegrees(MathHelper.atan2(y, xzNorm.toDouble())).toFloat()
            this.yaw = Math.toDegrees(MathHelper.atan2(x, z)).toFloat()
            this.prevPitch = this.pitch
            this.prevYaw = this.yaw
            this.setPositionAndAngles(this.x, this.y, this.z, this.yaw, this.pitch)
            this.life = 0
        }
    }

    fun setProperties(entity: Entity, pitch: Float, yaw: Float, roll: Float, zModifier: Float, globalModifier: Float) {
        val x = -MathHelper.sin(yaw * 0.017453292f) * MathHelper.cos(pitch * 0.017453292f)
        val y = -MathHelper.sin(pitch * 0.017453292f)
        val z = MathHelper.cos(yaw * 0.017453292f) * MathHelper.cos(pitch * 0.017453292f)
        this.setVelocity(x.toDouble(), y.toDouble(), z.toDouble(), zModifier, globalModifier)
        this.velocity = this.velocity.add(entity.velocity.x, if (entity.onGround) 0.0 else entity.velocity.y, entity.velocity.z)
    }

    override fun setVelocity(x: Double, y: Double, z: Double, zModifier: Float, globalModifier: Float) {
        val velocity = Vec3d(x, y, z).normalize().add(this.random.nextGaussian() * 0.007499999832361937 * globalModifier.toDouble(), this.random.nextGaussian() * 0.007499999832361937 * globalModifier.toDouble(), this.random.nextGaussian() * 0.007499999832361937 * globalModifier.toDouble()).multiply(zModifier.toDouble())
        this.velocity = velocity
        val norm = MathHelper.sqrt(squaredHorizontalLength(velocity))
        this.yaw = Math.toDegrees(MathHelper.atan2(velocity.x, velocity.z)).toFloat()
        this.pitch = Math.toDegrees(MathHelper.atan2(velocity.y, norm.toDouble())).toFloat()
        this.prevYaw = this.yaw
        this.prevPitch = this.pitch
        this.life = 0
    }

    override fun tick() {
        super.tick()
        if (this.shake > 0) --this.shake

        this.setInitialProjectilePitchYaw()
        this.inGround = this.inGround || this.didCollideWithBlock()
        this.extinguishWhenWet()

        if (this.inGround && !noClip) { // Should be on the ground
            handleProjectileGroundBehavior()
        } else { // Its flying
            this.inGroundTime = 0
            ++this.flyingTick
            handleProjectileHit()
            if (this.isCritical) this.spawnCriticalParticles()
            this.handleProjectilePitchYaw()
            this.handleEnvironmentDrag(this.dragInAir, this.dragInWater)
            this.setNextPosition()
            this.handleGravity()
            this.checkBlockCollision()
        }
    }

    private fun handleProjectileHit(){
        val currentPosition = Vec3d(this.x, this.y, this.z)
        var nextPosition = currentPosition.add(velocity)
        var hitResult: HitResult? = this.hitResult(currentPosition, nextPosition)
        if ((hitResult as HitResult).type != HitResult.Type.MISS) {
            nextPosition = hitResult.pos
        }

        while (!this.removed) {
            var entityHitResult = this.getEntityCollision(currentPosition, nextPosition)
            if (entityHitResult != null) {
                hitResult = entityHitResult
            }

            if (hitResult != null && hitResult.type == HitResult.Type.ENTITY) {
                val entityHit = (hitResult as EntityHitResult).entity
                val owner = this.owner
                if (entityHit is PlayerEntity && owner is PlayerEntity && !owner.shouldDamagePlayer(entityHit)) {
                    hitResult = null
                    entityHitResult = null
                }
            }

            if (hitResult != null && !noClip) {
                this.onHit((hitResult as HitResult?)!!)
                this.velocityDirty = true
            }

            if (entityHitResult == null || this.pierceLevel <= 0) {
                break
            }
            hitResult = null
        }
    }

    private fun handleProjectileGroundBehavior(){
        val position = BlockPos(this.x, this.y, this.z)
        val currentBlock = this.world.getBlockState(position)
        // if not on the ground
        if (this.inBlockState !== currentBlock && this.world.doesNotCollide(this.boundingBox.expand(0.06))) {
            this.inGround = false
            this.scaleVelocityByRandomFactor(0.2f)
            this.life = 0
            this.flyingTick = 0
        } else { //if did not move
            if (!this.world.isClient) {
                this.age()
            }
        }
        ++this.inGroundTime
    }

    protected open fun age() {
        ++this.life
        if (this.life >= 1200) {
            despawn()
        }
    }

    protected fun despawn(){
        this.remove()
    }

    protected fun onHit(hitResult: HitResult) {
        val hitResultType = hitResult.type
        if (hitResultType == HitResult.Type.ENTITY) {
            this.onEntityHit(hitResult as EntityHitResult)
        } else if (hitResultType == HitResult.Type.BLOCK) {
            val blockHitResult = hitResult as BlockHitResult
            val blockState = this.world.getBlockState(blockHitResult.blockPos)
            this.inBlockState = blockState
            val blockHitPos = blockHitResult.pos.subtract(this.x, this.y, this.z)
            this.velocity = blockHitPos
            val normalizedBlockHitPos = blockHitPos.normalize().multiply(0.05000000074505806)
            this.x -= normalizedBlockHitPos.x
            this.y -= normalizedBlockHitPos.y
            this.z -= normalizedBlockHitPos.z
            if (!isSilent) this.playSound(this.sound, 1.0f, 1.2f / (this.random.nextFloat() * 0.2f + 0.9f))
            this.inGround = true
            if (this.shake<=0) this.shake = 7
            this.isCritical = false
            this.pierceLevel = 0.toByte()
            this.sound=getHitSound()
            this.isShotFromCrossbow = false
            this.clearPiercingStatus()
            blockState.onProjectileHit(this.world, blockState, blockHitResult, this)
        }
    }

    private fun clearPiercingStatus() {
        if (this.piercingKilledEntities != null) {
            this.piercingKilledEntities!!.clear()
        }

        if (this.piercedEntities != null) {
            this.piercedEntities!!.clear()
        }
    }

    protected open fun onEntityHit(entityHitResult: EntityHitResult) {
        val entityHit = entityHitResult.entity
        val velocity = this.velocity.length().toFloat()
        var damage = MathHelper.ceil((velocity.toDouble() * this.damage).coerceAtLeast(0.0))
        val owner = this.owner
        val damageSource: DamageSource
        val fireTime = entityHit.method_20802() // getFireTime

        if (this.pierceLevel > 0) {
            if (this.piercedEntities == null) {
                this.piercedEntities = IntOpenHashSet(5)
            }

            if (this.piercingKilledEntities == null) {
                this.piercingKilledEntities = Lists.newArrayListWithCapacity(5)
            }

            if (this.piercedEntities!!.size >= this.pierceLevel + 1) {
                //this.remove()
                this.pickupPermission=PickupPermission.DISALLOWED
                return
            }

            this.piercedEntities!!.add(entityHit.entityId)
        }

        if (this.isCritical) {
            damage += this.random.nextInt(damage / 2 + 2)
        }

        if (owner == null) {
            damageSource = DamageSource.thrownProjectile(this, this)
        } else {
            damageSource = DamageSource.thrownProjectile(this, owner)
            if (owner is LivingEntity) {
                owner.onAttacking(entityHit)
            }
        }

        if (this.isOnFire && entityHit !is EndermanEntity) {
            entityHit.setOnFireFor(5)
        }

        if (entityHit.damage(damageSource, damage.toFloat())) {
            if (entityHit is LivingEntity) {
                /* if (!this.world.isClient && this.pierceLevel <= 0) {
                    entityHit.stuckArrows = entityHit.stuckArrows + 1
                }*/
                if (this.punchLevel > 0) {
                    val xzVelocity = this.velocity.multiply(1.0, 0.0, 1.0).normalize().multiply(this.punchLevel.toDouble() * 0.6)
                    if (xzVelocity.lengthSquared() > 0.0) {
                        entityHit.addVelocity(xzVelocity.x, 0.1, xzVelocity.z)
                    }
                }

                if (!this.world.isClient && owner is LivingEntity) {
                    EnchantmentHelper.onUserDamaged(entityHit, owner)
                    EnchantmentHelper.onTargetDamaged(owner as LivingEntity?, entityHit)
                }

                this.onHit(entityHit)
                if (owner != null && entityHit !== owner && entityHit is PlayerEntity && owner is ServerPlayerEntity) {
                    owner.networkHandler.sendPacket(GameStateChangeS2CPacket(6, 0.0f))
                }

                if (!entityHit.isAlive() && this.piercingKilledEntities != null) {
                    this.piercingKilledEntities!!.add(entityHit)
                }

                if (!this.world.isClient && owner is ServerPlayerEntity) {
                    val serverPlayerEntity = owner as ServerPlayerEntity?
                    if (this.piercingKilledEntities != null && this.isShotFromCrossbow) {
                        Criterions.KILLED_BY_CROSSBOW.trigger(serverPlayerEntity!!, this.piercingKilledEntities, this.piercingKilledEntities!!.size)
                    } else if (!entityHit.isAlive() && this.isShotFromCrossbow) {
                        Criterions.KILLED_BY_CROSSBOW.trigger(serverPlayerEntity!!, listOf<Entity>(entityHit), 0)
                    }
                }
            }

            if (!isSilent) this.playSound(this.sound, 1.0f, 1.2f / (this.random.nextFloat() * 0.2f + 0.9f))
            if (this.pierceLevel <= 0 && entityHit !is EndermanEntity) {
                //this.remove()
                this.pickupPermission=PickupPermission.DISALLOWED
            }
        } else {
            entityHit.method_20803(fireTime) // SetFire time
            this.velocity = this.velocity.multiply(-0.1)
            this.yaw += 180.0f
            this.prevYaw += 180.0f
            this.flyingTick = 0
            if (!this.world.isClient && this.velocity.lengthSquared() < 1.0E-7) {
                if (this.pickupPermission == PickupPermission.ALLOWED) {
                    this.dropStack(this.asItemStack(), 0.1f)
                    despawn()
                }else{
                    //this.remove()
                    this.pickupPermission=PickupPermission.DISALLOWED
                }
            }
        }
    }

    protected open fun onHit(livingEntity: LivingEntity) {}

    protected open fun getEntityCollision(currentPosition: Vec3d, nextPosition: Vec3d): EntityHitResult? {
        return ProjectileUtil.getEntityCollision(
                this.world,
                this,
                currentPosition,
                nextPosition,
                this.boundingBox.stretch(this.velocity).expand(1.0)
        ) { entity ->
            !entity.isSpectator &&
            entity.isAlive &&
            entity.collides() &&
            (entity !== this.owner || this.flyingTick >= 5) &&
            (this.piercedEntities == null || !this.piercedEntities!!.contains(entity.entityId))
        }
    }

    override fun onPlayerCollision(playerEntity: PlayerEntity) {
        if (!this.world.isClient && (this.inGround || this.isNoClip) && this.shake <= 0) {
            var canPickup = this.pickupPermission == PickupPermission.ALLOWED ||
                            this.pickupPermission == PickupPermission.CREATIVE_ONLY && playerEntity.abilities.creativeMode ||
                            this.isNoClip && this.owner?.uuid === playerEntity.uuid
            if (this.pickupPermission == PickupPermission.ALLOWED && !playerEntity.inventory.insertStack(this.asItemStack())) {
                canPickup = false
            }

            if (canPickup) {
                playerEntity.sendItemPickup(this, 1)
                despawn()
            }
        }
    }

    fun setDamage(livingEntity: LivingEntity, modifier: Float) {
        val powerLevel = EnchantmentHelper.getEquipmentLevel(Enchantments.POWER, livingEntity)
        val punchLevel = EnchantmentHelper.getEquipmentLevel(Enchantments.PUNCH, livingEntity)
        this.damage = (modifier * 2.0f).toDouble() + this.random.nextGaussian() * 0.25 + (this.world.difficulty.id.toFloat() * 0.11f).toDouble()
        if (powerLevel > 0) {
            this.damage = this.damage + powerLevel.toDouble() * 0.5 + 0.5
        }

        if (punchLevel > 0) {
            this.punchLevel=punchLevel
        }

        if (EnchantmentHelper.getEquipmentLevel(Enchantments.FLAME, livingEntity) > 0) {
            this.setOnFireFor(100)
        }
    }

    enum class PickupPermission {
        DISALLOWED,
        ALLOWED,
        CREATIVE_ONLY;

        companion object {

            fun fromOrdinal(i: Int): PickupPermission {
                return if (i < 0 || i > values().size) {
                    values()[0]
                } else {
                    values()[i]
                }
            }
        }
    }

    companion object {
        private val PROJECTILE_FLAGS: TrackedData<Byte> = DataTracker.registerData(AbstractProjectileEntity::class.java, TrackedDataHandlerRegistry.BYTE)
        protected val OPTIONAL_UUID: TrackedData<Optional<UUID>> = DataTracker.registerData(AbstractProjectileEntity::class.java, TrackedDataHandlerRegistry.OPTIONAL_UUID)
        private val PIERCE_LEVEL: TrackedData<Byte> = DataTracker.registerData(AbstractProjectileEntity::class.java, TrackedDataHandlerRegistry.BYTE)
    }

    override fun initDataTracker() {
        this.dataTracker.startTracking(PROJECTILE_FLAGS, 0.toByte())
        this.dataTracker.startTracking(OPTIONAL_UUID, Optional.empty())
        this.dataTracker.startTracking(PIERCE_LEVEL, 0.toByte())
    }

    private fun setProjectileFlag(index: Int, value: Boolean) {
        val flags = this.dataTracker.get(PROJECTILE_FLAGS) as Byte
        if (value) {// Set bit to 1
            this.dataTracker.set(PROJECTILE_FLAGS, (flags or index.toByte()))
        } else { // Set bit to 0
            this.dataTracker.set(PROJECTILE_FLAGS, (flags and index.inv().toByte()))
        }
    }

    public override fun writeCustomDataToTag(compoundTag_1: CompoundTag) {
        compoundTag_1.putShort("life", this.life.toShort())
        if (this.inBlockState != null) {
            compoundTag_1.put("inBlockState", TagHelper.serializeBlockState(this.inBlockState))
        }

        compoundTag_1.putByte("shake", this.shake.toByte())
        compoundTag_1.putByte("inGround", (if (this.inGround) 1 else 0).toByte())
        compoundTag_1.putByte("pickup", this.pickupPermission.ordinal.toByte())
        compoundTag_1.putDouble("damage", this.damage)
        compoundTag_1.putBoolean("crit", this.isCritical)
        compoundTag_1.putByte("PierceLevel", this.pierceLevel)
        if (this.ownerUuid != null) {
            compoundTag_1.putUuid("OwnerUUID", this.ownerUuid!!)
        }

        compoundTag_1.putString("SoundEvent", Registry.SOUND_EVENT.getId(this.sound)!!.toString())
        compoundTag_1.putBoolean("ShotFromCrossbow", this.isShotFromCrossbow)
    }

    public override fun readCustomDataFromTag(compoundTag_1: CompoundTag) {
        this.life = compoundTag_1.getShort("life").toInt()
        if (compoundTag_1.containsKey("inBlockState", 10)) {
            this.inBlockState = TagHelper.deserializeBlockState(compoundTag_1.getCompound("inBlockState"))
        }

        this.shake = (compoundTag_1.getByte("shake") and 255.toByte()).toInt()
        this.inGround = compoundTag_1.getByte("inGround").toInt() == 1
        if (compoundTag_1.containsKey("damage", 99)) {
            this.damage = compoundTag_1.getDouble("damage")
        }

        if (compoundTag_1.containsKey("pickup", 99)) {
            this.pickupPermission = PickupPermission.fromOrdinal(compoundTag_1.getByte("pickup").toInt())
        } else if (compoundTag_1.containsKey("player", 99)) {
            this.pickupPermission = if (compoundTag_1.getBoolean("player")) PickupPermission.ALLOWED else PickupPermission.DISALLOWED
        }

        this.isCritical = compoundTag_1.getBoolean("crit")
        this.pierceLevel = compoundTag_1.getByte("PierceLevel")
        if (compoundTag_1.hasUuid("OwnerUUID")) {
            this.ownerUuid = compoundTag_1.getUuid("OwnerUUID")
        }

        if (compoundTag_1.containsKey("SoundEvent", 8)) {
            this.sound = Registry.SOUND_EVENT.getOrEmpty(Identifier(compoundTag_1.getString("SoundEvent"))).orElse(this.getHitSound()) as SoundEvent
        }

        this.isShotFromCrossbow = compoundTag_1.getBoolean("ShotFromCrossbow")
    }

}
