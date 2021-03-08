package net.examplemod.items.baa

import me.shedaniel.architectury.registry.RegistrySupplier
import net.examplemod.screen.handler.ItemScreenHandler
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandlerType

class BaHandler(
    syncId: Int, playerInventory: PlayerInventory, buf: PacketByteBuf,
    itemStack: ItemStack = buf.readItemStack(),
    isOffHand: Boolean = buf.readBoolean(),
) : ItemScreenHandler(this.rType!!.get(), syncId, playerInventory, itemStack, isOffHand) {

    companion object {
        var rType: RegistrySupplier<ScreenHandlerType<out BaHandler>>? = null
    }
}