package yummyloop.yummy.inventory

import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack

fun Inventory.getCompressedInventory(): Inventory {
    val list = mutableListOf<ItemStack>()
    for (i in 0 until this.size()) {
        if (this.getStack(i) != ItemStack.EMPTY) list.add(this.getStack(i))
    }
    if (list.isEmpty()) list.add(ItemStack.EMPTY)
    return SimpleInventory(*list.toTypedArray())
}

fun Inventory.getSortedInventory(): Inventory {
    val list = mutableSetOf<ItemStack>()
    for (i in 0 until this.size()) {
        if (this.getStack(i) != ItemStack.EMPTY) {
            val stack = list.find {
                ItemStack.areItemsEqual(it, this.getStack(i)) && ItemStack.areTagsEqual(it, this.getStack(i))
            }
            if (stack != null) {
                stack.count += this.getStack(i).count
            } else {
                list.add(this.getStack(i))
            }
        }
    }
    if (list.isEmpty()) list.add(ItemStack.EMPTY)
    return SimpleInventory(*list.toTypedArray())
}