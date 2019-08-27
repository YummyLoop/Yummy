package yummyloop.example.item.backpack

import net.minecraft.container.ContainerType
import net.minecraft.container.GenericContainer
import net.minecraft.container.Slot
import net.minecraft.container.SlotActionType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import yummyloop.example.item.Items
import net.minecraft.item.Items as VanillaItems

class Cont(containerType_1 : ContainerType<*>, int_1 : Int, playerInventory_1 : PlayerInventory, inventory_1 : Inventory, int_2 : Int) :
        GenericContainer(containerType_1 , int_1, playerInventory_1,  inventory_1, int_2) {

    constructor(int_1 : Int, playerInventory_1 : PlayerInventory, inventory_1 : Inventory) :
            this(ContainerType.GENERIC_9X6, int_1, playerInventory_1, inventory_1, 6)

    /*
    override fun canInsertIntoSlot(slot_1: Slot?): Boolean {
        return slot_1?.stack?.item != Items.backpack
    }

    override fun canInsertIntoSlot(itemStack_1: ItemStack?, slot_1: Slot?): Boolean {
        return itemStack_1?.item != Items.backpack
    }

    override fun onSlotClick(int_1: Int, int_2: Int, slotActionType_1: SlotActionType?, player: PlayerEntity?): ItemStack {
        val stack = player?.inventory?.cursorStack
        val item = stack?.item
        return if (item == Items.backpack || stack == ItemStack.EMPTY) {
            ItemStack.EMPTY
        } else {
            super.onSlotClick(int_1, int_2, slotActionType_1, player)
        }
    }

    override fun transferSlot(player: PlayerEntity?, int_1: Int): ItemStack {
        return if (player?.inventory?.cursorStack?.item == Items.backpack) {
            ItemStack.EMPTY
        } else {
            super.transferSlot(player, int_1)
        }
    }

    override fun dropInventory(player: PlayerEntity?, world_1: World?, inventory_1: Inventory?) {
        if (player?.inventory?.cursorStack?.item == Items.backpack) {
            return
        } else {
            super.dropInventory(player, world_1, inventory_1)
        }
    }

    override fun canUse(player: PlayerEntity?): Boolean {
        return if (player?.inventory?.cursorStack?.item == Items.backpack){
            false
        }else {
            super.canUse(player)
        }
    }
    */
}