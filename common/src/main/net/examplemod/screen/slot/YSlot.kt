package net.examplemod.screen.slot

import net.minecraft.inventory.Inventory
import net.minecraft.screen.slot.Slot

class YSlot(inventory: Inventory?, index: Int, x: Int, y: Int) : Slot(inventory, index, x, y) {
    val visible: Boolean = true
    val defaultX = x
    val defaultY = y

    init {

    }
}