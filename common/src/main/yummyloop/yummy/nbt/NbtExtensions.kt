package yummyloop.yummy.nbt

import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.util.collection.DefaultedList
import yummyloop.yummy.LOG
import yummyloop.yummy.inventory.getCompressedInventory
import yummyloop.yummy.inventory.getSortedInventory

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
        var size = 90

        try {
            size = Regex("""\d+""").find(listTag.last().toString())?.value?.toInt()!! + 1
        } catch (e: Exception) {
            LOG.warn("Failed to find regex pattern in inventory NBT")
        }

        val stackList = DefaultedList.ofSize(size, ItemStack.EMPTY)
        Inventories.fromTag(this, stackList)
        return SimpleInventory(*stackList.toTypedArray())
    }
    return null
}