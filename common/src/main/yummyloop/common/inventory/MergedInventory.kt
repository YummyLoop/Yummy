package yummyloop.common.inventory

import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory

open class MergedInventory(vararg inventories: Inventory) : IMergedInventory {

    final override val inventoryList = inventories.toMutableList()

    init {
        if (inventoryList.isEmpty()) inventoryList.add(SimpleInventory(1))
    }
}