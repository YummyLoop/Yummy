package yummyloop.yummy.content.chest

import me.shedaniel.architectury.registry.RegistrySupplier
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandlerType
import yummyloop.common.screen.handler.ContainerScreenHandler

class ChestScreenHandler(
    syncId: Int, playerInventory: PlayerInventory, buf: PacketByteBuf,
    columns: Int = buf.readInt(),
    rows: Int = buf.readInt(),
    inventory: Inventory = SimpleInventory(columns * rows),
) : ContainerScreenHandler(rType?.get()!!, syncId, playerInventory, buf, columns, rows, inventory) {
    
    companion object {
        var rType: RegistrySupplier<ScreenHandlerType<out ChestScreenHandler>>? = null
    }
}

