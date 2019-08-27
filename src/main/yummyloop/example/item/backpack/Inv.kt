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

    override fun onInvOpen(player: PlayerEntity?) {
        if (player==null) return
    }

   /* override fun canPlayerUseInv(player: PlayerEntity?): Boolean {
        return player?.inventory?.cursorStack?.item != Items.backpack
    }*/

    override fun onInvClose(player: PlayerEntity?) {
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

    /*
    override fun isValidInvStack(int_1: Int, itemStack_1: ItemStack?): Boolean {
        if (itemStack.item == itemStack_1?.item) return false
        return true
    }
    */
}