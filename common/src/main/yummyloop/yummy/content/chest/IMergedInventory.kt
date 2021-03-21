package yummyloop.yummy.content.chest

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

interface IMergedInventory : Inventory {

    val inventoryList: MutableList<Inventory>

    fun add(inv: Inventory): Boolean {
        if (inv != this) return inventoryList.add(inv)
        return false
    }
    fun remove(inv: Inventory): Boolean = inventoryList.remove(inv)
    fun removeLast(): Inventory = inventoryList.removeLast()

    override fun clear() = inventoryList.forEach { it.clear() }

    override fun size(): Int {
        var size = 0
        inventoryList.forEach { size += it.size() }
        return size
    }

    override fun isEmpty(): Boolean = inventoryList.all { it.isEmpty }

    /**
     * Fetches the stack currently stored at the given slot. If the slot is empty,
     * or is outside the bounds of this inventory, returns see [ItemStack.EMPTY].
     */
    override fun getStack(slot: Int): ItemStack {
        var index = 0

        for (i in inventoryList) {
            if (slot >= index + i.size()) {
                index += i.size()
                continue
            } else {
                return i.getStack(slot - index)
            }
        }

        return ItemStack.EMPTY
    }

    /**
     * Removes a specific number of items from the given slot.
     *
     * @return the removed items as a stack
     */
    override fun removeStack(slot: Int, amount: Int): ItemStack {
        var index = 0

        for (i in inventoryList) {
            if (slot >= index + i.size()) {
                index += i.size()
                continue
            } else {
                return i.removeStack(slot - index, amount)
            }
        }

        return ItemStack.EMPTY
    }

    /**
     * Removes the stack currently stored at the indicated slot.
     *
     * @return the stack previously stored at the indicated slot.
     */
    override fun removeStack(slot: Int): ItemStack {
        var index = 0

        for (i in inventoryList) {
            if (slot >= index + i.size()) {
                index += i.size()
                continue
            } else {
                return i.removeStack(slot - index)
            }
        }

        return ItemStack.EMPTY
    }

    override fun setStack(slot: Int, stack: ItemStack?) {
        var index = 0

        for (i in inventoryList) {
            if (slot >= index + i.size()) {
                index += i.size()
                continue
            } else {
                i.setStack(slot - index, stack)
                break
            }
        }
    }

    override fun markDirty() = inventoryList.forEach { it.markDirty() }

    override fun canPlayerUse(player: PlayerEntity?): Boolean = inventoryList.all { it.canPlayerUse(player) }

    override fun onOpen(player: PlayerEntity?) = inventoryList.forEach { it.onOpen(player) }

    override fun onClose(player: PlayerEntity?) = inventoryList.forEach { it.onClose(player) }

    /**
     * Returns the number of times the specified item occurs in this inventory across all stored stacks.
     */
    override fun count(item: Item?): Int {
        var count = 0
        inventoryList.forEach { count += it.count(item) }
        return count
    }

    /**
     * Determines whether this inventory contains any of the given candidate items.
     */
    override fun containsAny(items: MutableSet<Item>?): Boolean = inventoryList.any { it.containsAny(items) }

    /**
     * Returns the maximum number of items a stack can contain when placed inside this inventory.
     * No slots may have more than this number of items. It is effectively the
     * stacking limit for this inventory's slots.
     *
     * @return the max {@link ItemStack#getCount() count} of item stacks in this inventory
     */
    override fun getMaxCountPerStack(): Int {
        return 64
    }

    /**
     * Returns whether the given stack is a valid for the indicated slot position.
     */
    override fun isValid(slot: Int, stack: ItemStack?): Boolean {
        return true
    }
}