package yummyloop.yummy.old.item.backpack

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.DyeableItem
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.*
import net.minecraft.world.World
import yummyloop.yummy.old.item.Item
import yummyloop.yummy.old.render.firstPerson.RenderHand
import yummyloop.yummy.old.util.extensions.openContainer
import net.minecraft.item.ItemGroup as VanillaItemGroup

class Backpack(name: String, var rows : Int, settings : Settings) :
        Item(name, settings), DyeableItem , RenderHand/*, BuiltInItemModel*/ {
    constructor(itemName : String, rows : Int) :
            this(itemName, rows, Settings().group(VanillaItemGroup.MISC))
    constructor(itemName : String, rows : Int, group : VanillaItemGroup?) :
            this(itemName, rows, Settings().group(group))

    init {
        this.addPropertyGetter(Identifier("using")) { itemStack, _, livingEntity -> if (livingEntity != null && livingEntity.activeItem == itemStack) 1.0f else 0.0f }
    }

    //---------------------------------------------------------

    override fun use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack?> {
        if (world.isClient) return TypedActionResult(ActionResult.PASS, player.getStackInHand(hand))

        val itemStack = player.getStackInHand(hand)

        return if (itemStack.count == 1) {

            // Fix name swap // minecraft tries to use items in both hands
            if (hand == Hand.OFF_HAND && player.getStackInHand(Hand.MAIN_HAND).item is Backpack) {
                if (player.getStackInHand(Hand.MAIN_HAND).count == 1) {
                    return TypedActionResult(ActionResult.FAIL, itemStack)
                }
            }
            // Open inventory
            if (this.rows < 1 || this.rows > 6) this.rows = 6
           /* player.openContainer(BContainer.id) { buf: PacketByteBuf ->
                buf.writeInt(this.rows)
                buf.writeString(itemStack.name.string)
            }*/

            player.setCurrentHand(hand)
            TypedActionResult(ActionResult.SUCCESS, itemStack)
        } else {
            TypedActionResult(ActionResult.FAIL, itemStack)
        }
    }

    override fun useOnBlock(itemUsageContext: ItemUsageContext?): ActionResult = ActionResult.PASS
    override fun useOnEntity(itemStack: ItemStack?, playerEntity: PlayerEntity?, livingEntity: LivingEntity?, hand: Hand?): Boolean = false
    override fun getUseAction(itemStack: ItemStack?): UseAction = UseAction.BLOCK
    override fun getMaxUseTime(itemStack: ItemStack?): Int = 0
    override fun onStoppedUsing(itemStack: ItemStack?, world: World?, livingEntity: LivingEntity?, int_1: Int) = Unit
    override fun finishUsing(itemStack: ItemStack?, world: World?, livingEntity: LivingEntity?): ItemStack? = itemStack
}