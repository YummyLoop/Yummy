package yummyloop.common.inventory

import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.collection.DefaultedList

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
                ItemStack.areItemsEqual(it, this.getStack(i)) && ItemStack.areNbtEqual(it, this.getStack(i))
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

fun Inventory.toTag(tag : NbtCompound) : NbtCompound {
    val list = DefaultedList.ofSize(this.size(), ItemStack.EMPTY)
    for (i in 0 until this.size()) list[i] = this.getStack(i)
    Inventories.writeNbt(tag, list)
    return tag
}

fun Inventory.fromTag(tag : NbtCompound){
    val list = DefaultedList.ofSize(this.size(), ItemStack.EMPTY)
    Inventories.readNbt(tag, list)
    for (i in 0 until this.size()) this.setStack(i, list[i])
}