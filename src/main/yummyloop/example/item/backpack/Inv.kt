package yummyloop.example.item.backpack

import net.minecraft.container.Slot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.BasicInventory
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.util.DefaultedList
import net.minecraft.util.Hand
import yummyloop.example.item.Items
import kotlin.properties.Delegates
import net.minecraft.item.Items as VanillaItems

class Inv(private val itemStack : ItemStack, contents: DefaultedList<ItemStack>) : BasicInventory(*(contents.toTypedArray())) {
    //var slot = 0
    //val copy = itemStack.copy()
    init {

    }

    override fun onInvOpen(player: PlayerEntity?) {
        if (player==null) return
        //slot = player.inventory.getSlotWithStack(itemStack)
        //itemStack.putSubTag("update", CompoundTag())
        //itemStack.count=0
        //player.setStackInHand(Hand.MAIN_HAND, ItemStack(VanillaItems.AIR))
        //player.inventory.setInvStack(slot, ItemStack.EMPTY)
    }

   /* override fun canPlayerUseInv(player: PlayerEntity?): Boolean {
        return player?.inventory?.cursorStack?.item != Items.backpack
    }*/

    override fun onInvClose(player: PlayerEntity?) {
        //player?.dropSelectedItem(true)
        //player?.inventory?.setInvStack(slot, copy)
        itemStack.removeSubTag("Items")
        val compoundTag = Inventories.toTag(CompoundTag(), this.getStackList(), false)
        if (!compoundTag.isEmpty) {
            itemStack.putSubTag("Items", compoundTag)
        }
        //copy.putSubTag("update", CompoundTag())
        //copy.removeSubTag("update")
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