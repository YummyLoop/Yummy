package net.examplemod.block.test

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.util.collection.DefaultedList

interface ImplementedInventory : Inventory {

    companion object {
        /** Creates an inventory from the item list. */
        fun of(items: DefaultedList<ItemStack>) = object : ImplementedInventory {
            override val items: DefaultedList<ItemStack>
                get() = items
        }

        /** Creates a new inventory with the specified size. */
        fun ofSize(size: Int): ImplementedInventory = of(DefaultedList.ofSize(size, ItemStack.EMPTY))
    }

    /**
     * Retrieves the item list of this inventory.
     * Must return the same instance every time it's called.
     */
    val items: DefaultedList<ItemStack>

    /** Returns the inventory size. */
    override fun size(): Int = items.size

    /** Retrieves the item in the slot. */
    override fun getStack(slot: Int): ItemStack = items[slot]

    /** Clears the inventory. */
    override fun clear() = items.clear()

    /** @return true if the player can use the inventory, false otherwise. */
    override fun canPlayerUse(player: PlayerEntity?): Boolean = true

    /**
     * Removes all items from an inventory slot.
     * @param slot The slot to remove from.
     */
    override fun removeStack(slot: Int): ItemStack? = Inventories.removeStack(items, slot)

    /**
     * Checks if the inventory is empty.
     * @return true if this inventory has only empty stacks, false otherwise.
     */
    override fun isEmpty(): Boolean = items.all { it.isEmpty }


    /**
     * Removes items from an inventory slot.
     * @param slot  The slot to remove from.
     * @param count How many items to remove. If there are less items in the slot than what are requested,
     * takes all items in that slot.
     */
    override fun removeStack(slot: Int, count: Int): ItemStack? {
        val result: ItemStack = Inventories.splitStack(items, slot, count)
        if (!result.isEmpty) {
            markDirty()
        }
        return result
    }


    /**
     * Replaces the current stack in an inventory slot with the provided stack.
     * @param slot  The inventory slot of which to replace the itemstack.
     * @param stack The replacing itemstack. If the stack is too big for
     * this inventory ([Inventory.getMaxCountPerStack]),
     * it gets resized to this inventory's maximum amount.
     */
    override fun setStack(slot: Int, stack: ItemStack) {
        items[slot] = stack
        if (stack.count > maxCountPerStack) {
            stack.count = maxCountPerStack
        }
    }


    /**
     * Marks the state as dirty.
     * Must be called after changes in the inventory, so that the game can properly save
     * the inventory contents and notify neighboring blocks of inventory changes.
     */
    override fun markDirty() {
        // Override if you want behavior.
    }
}


