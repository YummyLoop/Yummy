package yummyloop.example.item.backpack

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.block.BlockState
import net.minecraft.client.gui.screen.ingame.ContainerScreen54
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.DyeableItem
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.text.LiteralText
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import yummyloop.example.item.Item
import yummyloop.example.item.ItemGroup
import net.minecraft.item.ItemGroup as VanillaItemGroup

class Backpack(modId: String, name: String, var rows : Int, settings : Settings) :
        Item(modId, name, settings), DyeableItem , RenderHand/*, BuiltInItemModel*/ {
    constructor(modId : String, itemName : String, rows : Int) :
            this(modId, itemName, rows, Settings().group(VanillaItemGroup.MISC))
    constructor(modId : String, itemName : String, rows : Int, group : ItemGroup) :
            this(modId, itemName, rows, Settings().group(group.getGroup()))

    init {
        this.addPropertyGetter(Identifier("using")) { itemStack_1, _, livingEntity_1 -> if (livingEntity_1 != null && livingEntity_1.activeItem == itemStack_1) 1.0f else 0.0f }
    }

    /*
    @Environment(EnvType.CLIENT)
    override fun render(stack: ItemStack, bakedModel : BakedModel){
        //val banner = BannerBlockEntity()
        val model = ShieldEntityModel()
        //val model = Model1()
        //if (stack.getSubTag("BlockEntityTag") != null) {// Banner ... not done yet
        //    banner.deserialize(stack, ShieldItem.getColor(stack))
        //    bindTexture(TextureCache.SHIELD.get(banner.patternCacheKey, banner.patterns, banner.patternColors))
        //}
        GlStateManager.pushMatrix()

        //GlStateManager.scalef(1.0f, -1.0f, -1.0f)
        model.renderItem()
        /*if (stack.hasEnchantmentGlint()) {
            //renderGlint(this.textureManager, { this.renderModelWithTint(bakedModel, -8372020) }, 8)
            //renderEnchantmentGlint(Runnable { model.renderItem() })
        }*/

        GlStateManager.popMatrix()
    }
*/
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
            ContainerProviderRegistry.INSTANCE.openContainer(BContainer.id, player) { buf ->
                buf.writeInt(this.rows)
                buf.writeString(itemStack.name.string)
            }

            player.setCurrentHand(hand)
            TypedActionResult(ActionResult.SUCCESS, itemStack)
        } else {
            TypedActionResult(ActionResult.FAIL, itemStack)
        }
    }

    override fun useOnBlock(itemUsageContext_1: ItemUsageContext?): ActionResult {
        return ActionResult.PASS
    }

    override fun useOnEntity(itemStack_1: ItemStack?, playerEntity_1: PlayerEntity?, livingEntity_1: LivingEntity?, hand_1: Hand?): Boolean {
        return false
    }

    override fun getUseAction(itemStack_1: ItemStack?): UseAction {
        return UseAction.BLOCK
    }

    override fun getMaxUseTime(itemStack_1: ItemStack?): Int {
        return 0
    }

    override fun onStoppedUsing(itemStack_1: ItemStack?, world_1: World?, livingEntity_1: LivingEntity?, int_1: Int) {
    }

    override fun finishUsing(itemStack_1: ItemStack?, world_1: World?, livingEntity_1: LivingEntity?): ItemStack? {
        //println("is the container closed?")
        return itemStack_1//ItemStack(Items.IRON_BARS)
    }

    override fun isUsedOnRelease(itemStack_1: ItemStack?): Boolean {
        return false
    }

    override fun canMine(blockState_1: BlockState?, world_1: World?, blockPos_1: BlockPos?, playerEntity_1: PlayerEntity?): Boolean {
        return true // gets stuck if false
    }
}