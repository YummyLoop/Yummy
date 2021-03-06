package net.examplemod.items.baa

import me.shedaniel.architectury.registry.RegistrySupplier
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.Slot
import net.minecraft.screen.slot.SlotActionType
import net.minecraft.util.collection.DefaultedList

class BaHandler(syncId: Int, playerInventory: PlayerInventory, buf: PacketByteBuf) :
    ScreenHandler(this.rType!!.get(), syncId) {

    constructor(syncId: Int, playerInventory: PlayerInventory, buf: PacketByteBuf, stack: ItemStack)
            : this(syncId, playerInventory, buf) {
        if (stack != ItemStack.EMPTY) this.stack = stack
    }

    companion object {
        var rType: RegistrySupplier<ScreenHandlerType<out ScreenHandler>?>? = null
    }

    private var stack = buf.readItemStack()
    private var isOffHand = buf.readBoolean()
    private var invSize = 9
    private var inv = SimpleInventory(invSize)
    private var selectedSlotIndex = playerInventory.selectedSlot
    private var tempSlot = Slot(SimpleInventory(1), 0, 0, 0)

    override fun canUse(player: PlayerEntity?): Boolean = true

    init {
        fromTag()

        for (m in 0..2) for (l in 0..2) this.addSlot(Slot(inv, l + m * 3, 62 + l * 18, 17 + m * 18))
        //The player inventory
        for (m in 0..2) for (l in 0..8) this.addSlot(Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18))
        //The player Hotbar
        for (m in 0..8) {
            tempSlot =
                if (selectedSlotIndex == m && !isOffHand) {
                    object : Slot(playerInventory, m, 8 + m * 18, 142) {
                        override fun canInsert(stack: ItemStack?): Boolean = false
                        override fun canTakeItems(playerEntity: PlayerEntity?): Boolean = false
                    }
                } else {
                    Slot(playerInventory, m, 8 + m * 18, 142)
                }
            this.addSlot(tempSlot)
        }
    }

    override fun onSlotClick(i: Int, j: Int, actionType: SlotActionType?, playerEntity: PlayerEntity?): ItemStack {
        saveTag()
        return super.onSlotClick(i, j, actionType, playerEntity)
    }

    override fun onContentChanged(inventory: Inventory?) {
        saveTag()
        super.onContentChanged(inventory)
    }

    override fun close(player: PlayerEntity?) {
        saveTag()
        super.close(player)
    }

    private fun fromTag() {
        val itemStackList = DefaultedList.ofSize(invSize, ItemStack.EMPTY)
        Inventories.fromTag(stack.orCreateTag, itemStackList)
        inv = SimpleInventory(*itemStackList.map { it }.toTypedArray())
    }

    private fun saveTag() {
        val myList = DefaultedList.ofSize(invSize, ItemStack.EMPTY)
        for (i in 0 until invSize) {
            myList[i] = inv.getStack(i)
        }
        Inventories.toTag(stack.orCreateTag, myList)
    }

    // Shift + Player Inv Slot
    override fun transferSlot(player: PlayerEntity?, invSlot: Int): ItemStack {
        var newStack: ItemStack = ItemStack.EMPTY
        val slot: Slot = this.slots[invSlot]
        if (slot.hasStack()) {
            val originalStack: ItemStack = slot.stack
            newStack = originalStack.copy()
            if (invSlot < inv.size()) {
                if (!this.insertItem(originalStack, inv.size(), this.slots.size, true)) {
                    return ItemStack.EMPTY
                }
            } else if (!this.insertItem(originalStack, 0, inv.size(), false)) {
                return ItemStack.EMPTY
            }
            if (originalStack.isEmpty) {
                slot.stack = ItemStack.EMPTY
            } else {
                slot.markDirty()
            }
        }
        return newStack
    }
}