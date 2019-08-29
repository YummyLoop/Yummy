package yummyloop.example.item.backpack

import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.block.BlockState
import net.minecraft.client.gui.screen.ingame.ContainerScreen54
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.text.LiteralText
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import yummyloop.example.item.Item
import yummyloop.example.item.ItemGroup
import net.minecraft.item.ItemGroup as VanillaItemGroup

class Backpack(modId: String, name: String, settings : Settings) : Item(modId, name, settings){
    constructor(modId : String, itemName : String) :
            this(modId, itemName, Settings().group(VanillaItemGroup.MISC))
    constructor(modId : String, itemName : String, group : ItemGroup) :
            this(modId, itemName, Settings().group(group.getGroup()))

    companion object{
        val containerId = Identifier("tutorial", "backpack1")
        val provider = ContainerProviderRegistry.INSTANCE.registerFactory(containerId) { syncId, _, player, buf -> BContainer(syncId, player, buf) }
        val screen = ScreenProviderRegistry.INSTANCE.registerFactory(containerId) { syncId, _, player, buf -> Screen(syncId, player, buf) }

        open class Screen(syncId: Int, player: PlayerEntity, buf: PacketByteBuf) :
                ContainerScreen54(BContainer(syncId, player, buf), player.inventory, LiteralText(buf.readString()))
    }

    override fun use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack?> {
        if (world.isClient) return TypedActionResult(ActionResult.PASS, player.getStackInHand(hand))

        //player.setCurrentHand(hand)
        val itemStack = player.getStackInHand(hand)

        return if (itemStack.count == 1) {
            ContainerProviderRegistry.INSTANCE.openContainer(containerId, player) { buf ->
                buf.writeString(itemStack.name.string)
            }

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
        return 20
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