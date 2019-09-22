package yummyloop.example.item.spear

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import net.fabricmc.fabric.api.client.render.EntityRendererRegistry
import net.minecraft.client.item.TooltipContext
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.*
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.TridentItem
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.stat.Stats
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import yummyloop.example.item.ItemGroup
import yummyloop.example.util.registry.ClientManager
import yummyloop.example.util.registry.RegistryManager
import net.minecraft.item.ItemGroup as VanillaItemGroup

open class Spear(itemName: String, settings : Settings) : TridentItem(settings) {
    private val tooltip = ArrayList<Text>()
    protected val attackDamage = 4.5
    protected val attackSpeed = 1.1
    protected val velocityMod = 0.85F

    companion object{
        private var ini = false
    }

    init {
        register(itemName)
        if (!ini) {
            ini = true
            SpearEntity
            ClientManager.registerEntityRenderer(SpearEntity::class.java) { entityRenderDispatcher: EntityRenderDispatcher, context: EntityRendererRegistry.Context -> ThrownItemEntityRenderer(entityRenderDispatcher, context, this) }
        }
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
    fun addTooltip(tooltip : String) : Spear {
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

    override fun use(world_1: World?, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = player.getStackInHand(hand)
        return when {
            stack.damage >= stack.maxDamage -> TypedActionResult(ActionResult.FAIL, stack)
            else -> {
                player.setCurrentHand(hand)
                TypedActionResult(ActionResult.SUCCESS, stack)
            }
        }
    }

    override fun onStoppedUsing(stack: ItemStack, world: World?, player: LivingEntity?, useTimeLeft: Int) {
        if (player is PlayerEntity) {
            val useTime = this.getMaxUseTime(stack) - useTimeLeft
            if (useTime < 10) return
            val riptideLevel = EnchantmentHelper.getRiptide(stack)

            if (!world!!.isClient) {
                stack.damage(1, player, { playerEntity -> playerEntity.sendToolBreakStatus(playerEntity.activeHand) })

                if (riptideLevel == 0 || !player.isInsideWaterOrRain) {
                    throwProjectile(player, stack)
                }
            }

            player.incrementStat(Stats.USED.getOrCreateStat(this))

            // Riptide enchant action
            if (riptideLevel > 0 && player.isInsideWaterOrRain) {
                ripTideEnchantAction(player, riptideLevel)
            }
        }
    }

    protected fun getThrownEntity(player : PlayerEntity, stack: ItemStack): ProjectileEntity {
        return SpearEntity(player.world, player, stack)
    }

    protected fun getThrowSound(): SoundEvent {
        return SoundEvents.ITEM_TRIDENT_THROW
    }

    protected fun throwProjectile(player : PlayerEntity, stack: ItemStack){
        val thrownEntity = getThrownEntity(player, stack)
        // set projectile velocity
        thrownEntity.setProperties(player, player.pitch, player.yaw, 0.0f, this.velocityMod*(2.5f + EnchantmentHelper.getRiptide(stack).toFloat() * 0.5f), 1.0f)
        when {
            player.abilities.creativeMode -> thrownEntity.pickupType = ProjectileEntity.PickupPermission.CREATIVE_ONLY
            else -> player.inventory.removeOne(stack)
        }
        player.world.spawnEntity(thrownEntity)
        player.world.playSoundFromEntity(null, thrownEntity, getThrowSound(), SoundCategory.PLAYERS, 1.0f, 1.0f)
    }

    protected fun ripTideEnchantAction(player : PlayerEntity, riptideLevel : Int) {
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

        player.world.playSoundFromEntity(null, player, soundEvent, SoundCategory.PLAYERS, 1.0f, 1.0f)
    }
}