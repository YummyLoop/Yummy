package yummyloop.common.nbt

import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.util.collection.DefaultedList
import yummyloop.common.Common.LOG
import yummyloop.common.inventory.getCompressedInventory
import yummyloop.common.inventory.getSortedInventory

fun CompoundTag.getCompressedInventory(): Inventory? {
    val inv = this.getInventory() ?: return null
    return inv.getCompressedInventory()
}

fun CompoundTag.getSortedInventory(): Inventory? {
    val inv = this.getInventory() ?: return null
    return inv.getSortedInventory()
}

fun CompoundTag.getInventory(): Inventory? {
    val listTag: ListTag = this.getList("Items", 10)
    if (!listTag.isEmpty()) {
        var size = 69

        try {
            size = Regex("""\d+""").find(listTag.last().toString())?.value?.toInt()!! + 1
        } catch (e: Exception) {
            LOG.warn("Failed to find regex pattern in inventory NBT")
        }

        val stackList = DefaultedList.ofSize(size, ItemStack.EMPTY).also { Inventories.fromTag(this, it) }
        return SimpleInventory(*stackList.toTypedArray())
    }
    return null
}