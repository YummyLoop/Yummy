package yummyloop.example.item

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import com.mojang.blaze3d.platform.GlStateManager
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.item.TooltipContext
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.*
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.TridentItem
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.stat.Stats
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import yummyloop.example.ExampleMod
import yummyloop.example.util.registry.RegistryManager
import net.minecraft.item.ItemGroup as VanillaItemGroup
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.item.Item
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.hit.EntityHitResult


open class Spear(itemName: String, settings : Settings) : TridentItem(settings) {
    private val tooltip = ArrayList<Text>()
    protected val attackDamage = 4.5
    protected val attackSpeed = 1.1
    protected val velocityMod = 0.85F
    protected val projectileEntity = {entityType : EntityType<out ProjectileEntity>, world : World, livingEntity : LivingEntity, stack : ItemStack ->
        SpearEntity(entityType, world, livingEntity, stack)
    }

    init {
        register(itemName)
    }
    constructor(itemName : String) :
            this(itemName, Settings().group(VanillaItemGroup.MISC))
    constructor(itemName : String, group : ItemGroup?) :
            this(itemName, Settings().group(group?.getGroup()))
    constructor(itemName : String, group : ItemGroup?, maxCount : Int) :
            this(itemName, Settings().group(group?.getGroup()).maxCount(maxCount))

    private fun register (itemName : String) {
        RegistryManager.register(this, itemName)
    }

    override fun appendTooltip(itemStack: ItemStack?, world: World?, tooltip: MutableList<Text>?, tooltipContext: TooltipContext?) {
        tooltip?.addAll(this.tooltip)
    }

    /**
     * Appends a tooltip to the item
     * @param tooltip example: item.tutorial.fabric_item.tooltip
     */
    fun addTooltip(tooltip : String) : Spear{
        this.tooltip.add(TranslatableText(tooltip))
        return this
    }

    override fun getModifiers(equipmentSlot_1: EquipmentSlot?): Multimap<String, EntityAttributeModifier> {
        val multimap = HashMultimap.create<String, EntityAttributeModifier>()
        if (equipmentSlot_1 == EquipmentSlot.MAINHAND) {
            multimap.put(EntityAttributes.ATTACK_DAMAGE.id, EntityAttributeModifier(Item.ATTACK_DAMAGE_MODIFIER_UUID, "Tool modifier", (attackDamage-1), EntityAttributeModifier.Operation.ADDITION))
            multimap.put(EntityAttributes.ATTACK_SPEED.id, EntityAttributeModifier(Item.ATTACK_SPEED_MODIFIER_UUID, "Tool modifier", (attackSpeed-1.1-2.9000000953674316), EntityAttributeModifier.Operation.ADDITION))
        }
        return multimap
    }

    override fun onStoppedUsing(stack: ItemStack, world: World?, player: LivingEntity?, useTimeLeft: Int) {
        if (player is PlayerEntity) {
            val useTime = this.getMaxUseTime(stack) - useTimeLeft
            if (useTime < 10) return
            val riptideLevel = EnchantmentHelper.getRiptide(stack)
            if (riptideLevel > 0 && !player.isInsideWaterOrRain) return
            if (!world!!.isClient) {
                stack.damage(1, player, { playerEntity -> playerEntity.sendToolBreakStatus(playerEntity.activeHand) })

                if (riptideLevel == 0) {
                    val thrownEntity = this.projectileEntity(ExampleMod.spearType, world, player, stack)
                    // set projectile velocity
                    thrownEntity.setProperties(player, player.pitch, player.yaw, 0.0f, this.velocityMod*(2.5f + riptideLevel.toFloat() * 0.5f), 1.0f)
                    if (player.abilities.creativeMode) {
                        thrownEntity.pickupType = ProjectileEntity.PickupPermission.CREATIVE_ONLY
                    }

                    world.spawnEntity(thrownEntity)
                    world.playSoundFromEntity(null, thrownEntity, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0f, 1.0f)
                    if (!player.abilities.creativeMode) {
                        player.inventory.removeOne(stack)
                    }
                }
            }

            player.incrementStat(Stats.USED.getOrCreateStat(this))
            if (riptideLevel <= 0) return
            val playerYaw = player.yaw
            val playerPitch = player.pitch
            var velocityX = -MathHelper.sin(playerYaw * 0.017453292f) * MathHelper.cos(playerPitch * 0.017453292f)
            var velocityY = -MathHelper.sin(playerPitch * 0.017453292f)
            var velocityZ = MathHelper.cos(playerYaw * 0.017453292f) * MathHelper.cos(playerPitch * 0.017453292f)
            val absoluteVelocity = MathHelper.sqrt(velocityX * velocityX + velocityY * velocityY + velocityZ * velocityZ)
            val velocityModifier = this.velocityMod * 3f * ((1.0f + riptideLevel.toFloat()) / 4.0f)
            velocityX *= velocityModifier / absoluteVelocity
            velocityY *= velocityModifier / absoluteVelocity
            velocityZ *= velocityModifier / absoluteVelocity
            player.addVelocity(velocityX.toDouble(), velocityY.toDouble(), velocityZ.toDouble())
            player.method_6018(20)
            if (player.onGround) {
                player.move(MovementType.SELF, Vec3d(0.0, 1.1999999284744263, 0.0))
            }

            val soundEvent: SoundEvent = when {
                riptideLevel >= 3 -> SoundEvents.ITEM_TRIDENT_RIPTIDE_3
                riptideLevel == 2 -> SoundEvents.ITEM_TRIDENT_RIPTIDE_2
                else -> SoundEvents.ITEM_TRIDENT_RIPTIDE_1
            }

            world.playSoundFromEntity(null, player, soundEvent, SoundCategory.PLAYERS, 1.0f, 1.0f)
        }
    }

    // Mostly the same as TridentEntity
    class SpearEntity : ProjectileEntity {
        companion object{
            private val loyalty: TrackedData<Byte>? =
                    DataTracker.registerData<Byte>(SpearEntity::class.java, TrackedDataHandlerRegistry.BYTE)

            private val defaultItem = Items["Spear"]
        }

        var stack: ItemStack = ItemStack(defaultItem)
        private var dealtDamage = false
        var tick: Int = 0

        constructor(entityType_1 : EntityType<out ProjectileEntity>, world_1 : World)
                : super(entityType_1, world_1)

        constructor(entityType : EntityType<out ProjectileEntity> ,world : World, livingEntity : LivingEntity, stack : ItemStack)
                : super(entityType, livingEntity, world){
            this.stack = stack.copy()
            this.dataTracker.set(loyalty, EnchantmentHelper.getLoyalty(stack).toByte())
        }
        constructor(entityType : EntityType<out ProjectileEntity>, world_1: World, double_1: Double, double_2: Double, double_3: Double)
                : super(entityType, double_1, double_2, double_3, world_1)

        override fun asItemStack(): ItemStack {
            return stack.copy()
        }

        override fun initDataTracker() {
            super.initDataTracker()
            this.dataTracker.startTracking(loyalty, 0.toByte())
        }

        override fun tick() {
            val catchSound = SoundEvents.ITEM_TRIDENT_RETURN
            effectiveTick(catchSound)
        }

        private fun effectiveTick(catchSound : SoundEvent){
            if (this.inGroundTime > 4) {
                this.dealtDamage = true
            }

            if (this.dealtDamage || this.isNoClip) {
                val owner = this.owner
                if (owner != null) {
                    val loyalty = (this.dataTracker.get<Byte>(loyalty) as Byte).toInt()
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
                            if (this.tick == 0) {
                                this.playSound(catchSound, 10.0f, 1.0f)
                            }
                            ++this.tick
                        }
                    }
                }
            }
            super.tick()
        }

        override fun getEntityCollision(vec3d_1: Vec3d, vec3d_2: Vec3d): EntityHitResult? {
            return if (this.dealtDamage) null else super.getEntityCollision(vec3d_1, vec3d_2)
        }

        override fun onEntityHit(entityHitResult_1: EntityHitResult) {
            val hitSound = SoundEvents.ITEM_TRIDENT_HIT
            val hitThunderSound = SoundEvents.ITEM_TRIDENT_THUNDER
            effectiveOnEntityHit(entityHitResult_1, hitSound, hitThunderSound)
        }

        private fun effectiveOnEntityHit(entityHitResult: EntityHitResult, hitSound : SoundEvent, hitThunderSound : SoundEvent){
            val entityHit = entityHitResult.entity
            var attackDamage = 8.0f
            if (entityHit is LivingEntity) {
                attackDamage += EnchantmentHelper.getAttackDamage(this.stack, entityHit.group)
            }

            val owner = this.owner
            val damageSource = DamageSource.trident(this, owner)
            this.dealtDamage = true
            if (entityHit.damage(damageSource, attackDamage) && entityHit is LivingEntity) {
                if (owner is LivingEntity) {
                    EnchantmentHelper.onUserDamaged(entityHit, owner)
                    EnchantmentHelper.onTargetDamaged(owner, entityHit)
                }
                this.onHit(entityHit)
            }

            this.velocity = this.velocity.multiply(-0.01, -0.1, -0.01)

            if (this.world is ServerWorld && this.world.isThundering && EnchantmentHelper.hasChanneling(this.stack)) {
                val blockPos = entityHit.blockPos
                if (this.world.isSkyVisible(blockPos)) {
                    val lightningEntity = LightningEntity(this.world, blockPos.x.toDouble() + 0.5, blockPos.y.toDouble(), blockPos.z.toDouble() + 0.5, false)
                    lightningEntity.setChanneller(if (owner is ServerPlayerEntity) owner else null)
                    (this.world as ServerWorld).addLightning(lightningEntity)
                    this.playSound(hitThunderSound, 5.0f, 1.0f)
                    return
                }
            }
            this.playSound(hitSound, 1.0f, 1.0f)
        }

        private fun isOwnerAlive(): Boolean {
            val owner = this.owner
            return when {
                owner != null && owner.isAlive -> !(owner is ServerPlayerEntity && owner.isSpectator)
                else -> false
            }
        }

        override fun onPlayerCollision(playerEntity_1: PlayerEntity?) {
            val owner = this.owner
            if (owner == null || owner.uuid === playerEntity_1!!.uuid) {
                super.onPlayerCollision(playerEntity_1)
            }
        }

        override fun readCustomDataFromTag(compoundTag_1: CompoundTag) {
            super.readCustomDataFromTag(compoundTag_1)
            if (compoundTag_1.containsKey("Spear", 10)) {
                this.stack = ItemStack.fromTag(compoundTag_1.getCompound("Spear"))
            }

            this.dealtDamage = compoundTag_1.getBoolean("DealtDamage")
            this.dataTracker.set(loyalty, EnchantmentHelper.getLoyalty(this.stack).toByte())
        }

        override fun writeCustomDataToTag(compoundTag_1: CompoundTag) {
            super.writeCustomDataToTag(compoundTag_1)
            compoundTag_1.put("Spear", this.stack.toTag(CompoundTag()))
            compoundTag_1.putBoolean("DealtDamage", this.dealtDamage)
        }

        override fun age() {
            val loyalty = (this.dataTracker.get<Byte>(loyalty) as Byte).toInt()
            if (this.pickupType != PickupPermission.ALLOWED || loyalty <= 0) {
                super.age()
            }
        }

        override fun getDragInWater(): Float {
            return 0.99f
        }

        @Environment(EnvType.CLIENT)
        override fun shouldRenderFrom(double_1: Double, double_2: Double, double_3: Double): Boolean {
            return true
        }

        override fun getSound(): SoundEvent? {
            return SoundEvents.ITEM_TRIDENT_HIT_GROUND
        }

    }

    class SpearEntityRenderer : EntityRenderer<SpearEntity> {
        constructor(e1 : EntityRenderDispatcher) : super(e1)

        companion object{
            val defaultItem = Items["spear"]
        }

        val stack = ItemStack(defaultItem)

        override fun getTexture(var1: SpearEntity?): Identifier? {
            return Identifier("minecraft:textures/item/iron_ingot.png") // Is ignored
        }

        override fun render(entity: SpearEntity, x: Double, y: Double, z: Double, float_1: Float, float_2: Float) {
            glMatrix {
                GlStateManager.translatef(x.toFloat(), y.toFloat(), z.toFloat())
                GlStateManager.rotatef(MathHelper.lerp(float_2, entity.prevYaw, entity.yaw) - 90.0f, 0.0f, 1.0f, 0.0f)
                GlStateManager.rotatef(MathHelper.lerp(float_2, entity.prevPitch, entity.pitch) + 90.0f, 0.0f, 0.0f, 1.0f)

                glMatrix {
                    GlStateManager.translatef(0F, 0.4F,0F)
                    GlStateManager.scalef(1F, -1F, 1F)
                    MinecraftClient.getInstance().itemRenderer.renderItem(this.stack, ModelTransformation.Type.NONE)
                }
            }
        }

        private fun glMatrix(op : () -> Unit){
            GlStateManager.pushMatrix()
            op()
            GlStateManager.popMatrix()
        }
    }
}