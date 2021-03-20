package yummyloop.common.screen.handler

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

abstract class ContainerScreenHandler(
    type: ScreenHandlerType<*>,
    syncId: Int, val playerInventory: PlayerInventory, buf: PacketByteBuf,
    val columns: Int = buf.readInt(),
    val rows: Int = buf.readInt(),
    var inventory: Inventory = SimpleInventory(columns * rows),
) : ScreenHandler(type, syncId) {
    protected val invSize: Int = columns * rows
    protected val slotSize: Int = 18
    protected val offsetY: Int = this.rows * slotSize

    init {
        //checkSize(inventory, 27)

        inventory.onOpen(playerInventory.player)

        addInventorySlots()

       addPlayerInventorySlots()

        addPlayerInventoryHotBar()

    }

    /** The container inventory slots */
    protected fun addInventorySlots(){
        for (r in 0 until rows) for (c in 0 until columns)
            addSlot(Slot(inventory, c + r * columns, 8 + c * slotSize, 18 + r * slotSize))
    }

    /** The player inventory slots */
    protected fun addPlayerInventorySlots(){
        for (r in 0 until 3) for (c in 0 until 9)
            addSlot(Slot(playerInventory, c + r * 9 + 9, 8 + c * slotSize, 31 + r * slotSize + offsetY))
    }

    /** The player Hotbar slots */
    protected fun addPlayerInventoryHotBar(){
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

