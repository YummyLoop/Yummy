package yummyloop.yummy.content.chest

import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory

class MergedInventory(vararg inventories: Inventory) : IMergedInventory {

    override val inventoryList = inventories.toMutableList()

    init {
        if (inventoryList.isEmpty()) inventoryList.add(SimpleInventory(1))
    }
}