package yummyloop.example.item.backpack

import net.minecraft.container.ContainerType
import net.minecraft.container.GenericContainer
import net.minecraft.container.SlotActionType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand

class Cont(containerType_1 : ContainerType<*>, int_1 : Int, playerInventory_1 : PlayerInventory, inventory_1 : Inventory, rows : Int) :
        GenericContainer(containerType_1 , int_1, playerInventory_1,  inventory_1, rows) {

    constructor(int_1 : Int, playerInventory_1 : PlayerInventory, inventory_1 : Inventory) :
            this(ContainerType.GENERIC_9X6, int_1, playerInventory_1, inventory_1, 6)

    override fun onSlotClick(int_1: Int, int_2: Int, slotActionType: SlotActionType?, player: PlayerEntity?): ItemStack {
        return if(int_1 > 0 && this.getSlot(int_1).stack == player?.getStackInHand(Hand.MAIN_HAND)) {
            ItemStack.EMPTY;
        } else {
            super.onSlotClick(int_1, int_2, slotActionType, player)
        }
    }

    /* Functions that seem useless
    override fun canInsertIntoSlot(slot_1: Slot?): Boolean
    override fun canInsertIntoSlot(itemStack_1: ItemStack?, slot_1: Slot?): Boolean
    override fun canUse(player: PlayerEntity?): Boolean
    override fun dropInventory(player: PlayerEntity?, world_1: World?, inventory_1: Inventory?)
     */
}