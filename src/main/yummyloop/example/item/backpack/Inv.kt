package yummyloop.example.item.backpack

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.BasicInventory
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.DefaultedList

class Inv(private val itemStack : ItemStack, contents: DefaultedList<ItemStack>) : BasicInventory(*(contents.toTypedArray())) {
    init {

    }

    override fun onInvClose(playerEntity_1: PlayerEntity?) {
        super.onInvClose(playerEntity_1)
        itemStack.removeSubTag("Items")
        val compoundTag = Inventories.toTag(CompoundTag(), this.getStackList(), false)
        if (!compoundTag.isEmpty) {
            itemStack.putSubTag("Items", compoundTag)
        }
    }

    private fun BasicInventory.getStackList(): DefaultedList<ItemStack>? {
        val list : DefaultedList<ItemStack> = DefaultedList.of()
        for ( i in 0..this.invSize){
            list.add(this.getInvStack(i))
        }
        return list
    }

    override fun isValidInvStack(int_1: Int, itemStack_1: ItemStack?): Boolean {
        if (itemStack == itemStack_1) return false
        return true
    }
}