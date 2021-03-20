package yummyloop.yummy.content.chest

import me.shedaniel.architectury.registry.RegistrySupplier
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.Slot
import yummyloop.common.screen.defaultTransferSlot

class ChestScreenHandler(
    syncId: Int, playerInventory: PlayerInventory, buf: PacketByteBuf,
    var inventory: Inventory = SimpleInventory(54),
) : ScreenHandler(rType?.get(), syncId) {
    private var columns: Int = 9
    private var rows: Int = 6
    private val invSize: Int = columns * rows
    private var slotSize: Int = 18
    private val offsetY: Int = this.rows * slotSize

    companion object {
        var rType: RegistrySupplier<ScreenHandlerType<out ScreenHandler>>? = null
    }

    init {
        //checkSize(inventory, 54)

        inventory.onOpen(playerInventory.player)

        //Our inventory
        for (r in 0 until rows) for (c in 0 until columns)
            addSlot(Slot(inventory, c + r * columns, 8 + c * slotSize, 18 + r * slotSize))

        //The player inventory
        for (r in 0 until 3) for (c in 0 until 9)
            addSlot(Slot(playerInventory, c + r * 9 + 9, 8 + c * slotSize, 31 + r * slotSize + offsetY))

        //The player Hotbar
        for (c in 0 until 9)
            addSlot(Slot(playerInventory, c, 8 + c * slotSize, 35 + 3 * slotSize + offsetY))

    }

    override fun close(player: PlayerEntity?) {
        super.close(player)
        inventory.onClose(player)
    }

    override fun canUse(player: PlayerEntity?): Boolean {
        return inventory.canPlayerUse(player)
    }

    override fun transferSlot(player: PlayerEntity?, invSlot: Int): ItemStack {
        return defaultTransferSlot(player, invSlot, inventory, this::insertItem)
    }
}

