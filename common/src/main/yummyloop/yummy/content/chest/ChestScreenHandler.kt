package yummyloop.yummy.content.chest

import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandlerType
import yummyloop.common.screen.handler.ContainerScreenHandler

class ChestScreenHandler(
    syncId: Int, playerInventory: PlayerInventory, buf: PacketByteBuf,
    size: Int = buf.readInt(),
    inventory: Inventory = SimpleInventory(size),
) : ContainerScreenHandler(rType?.get()!!, syncId, playerInventory, buf, size, inventory) {
    
    companion object {
        var rType: RegistrySupplier<ScreenHandlerType<out ChestScreenHandler>>? = null
    }
}

