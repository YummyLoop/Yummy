package yummyloop.common.nbt

import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.util.collection.DefaultedList
import yummyloop.common.Common.LOG
import yummyloop.common.inventory.getCompressedInventory
import yummyloop.common.inventory.getSortedInventory

fun NbtCompound.getCompressedInventory(): Inventory? {
    val inv = this.getInventory() ?: return null
    return inv.getCompressedInventory()
}

fun NbtCompound.getSortedInventory(): Inventory? {
    val inv = this.getInventory() ?: return null
    return inv.getSortedInventory()
}

fun NbtCompound.getInventory(): Inventory? {
    val listTag: NbtList = this.getList("Items", 10)
    if (!listTag.isEmpty()) {
        var size = 69

        try {
            size = Regex("""\d+""").find(listTag.last().toString())?.value?.toInt()!! + 1
        } catch (e: Exception) {
            LOG.warn("Failed to find regex pattern in inventory NBT")
        }

        val stackList = DefaultedList.ofSize(size, ItemStack.EMPTY).also { Inventories.readNbt(this, it) }
        return SimpleInventory(*stackList.toTypedArray())
    }
    return null
}