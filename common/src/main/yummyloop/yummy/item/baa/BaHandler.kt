package yummyloop.yummy.item.baa

import me.shedaniel.architectury.registry.RegistrySupplier
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandlerType
import yummyloop.common.screen.handler.ItemScreenHandler

class BaHandler(
    syncId: Int, playerInventory: PlayerInventory, buf: PacketByteBuf,
    itemStack: ItemStack = buf.readItemStack(),
    isOffHand: Boolean = buf.readBoolean(),
) : ItemScreenHandler(this.rType!!.get(), syncId, playerInventory, itemStack, isOffHand) {

    companion object {
        var rType: RegistrySupplier<ScreenHandlerType<out BaHandler>>? = null
    }
}