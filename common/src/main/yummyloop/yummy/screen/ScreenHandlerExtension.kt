package yummyloop.yummy.screen

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.Slot


/**
 * Usage : return defaultTransferSlot(player, invSlot, inventory, this::insertItem)
 */
fun ScreenHandler.defaultTransferSlot(
    player: PlayerEntity?,
    invSlot: Int,
    inv: Inventory,
    insertItem: (stack: ItemStack, startIndex: Int, endIndex: Int, fromLast: Boolean) -> Boolean,
): ItemStack {
    var newStack: ItemStack = ItemStack.EMPTY
    val slot: Slot = this.slots[invSlot]
    if (slot.hasStack()) {
        val originalStack: ItemStack = slot.stack
        newStack = originalStack.copy()
        if (invSlot < inv.size()) {
            if (!insertItem(originalStack, inv.size(), this.slots.size, true)) {
                return ItemStack.EMPTY
            }
        } else if (!insertItem(originalStack, 0, inv.size(), false)) {
            return ItemStack.EMPTY
        }

        if (originalStack.isEmpty) {
            slot.stack = ItemStack.EMPTY
        } else {
            slot.markDirty()
        }

        if (newStack.count == originalStack.count) {
            return ItemStack.EMPTY
        }

        slot.onTakeItem(player, originalStack)
    }
    return newStack
}