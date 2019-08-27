package yummyloop.example.item.backpack

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.container.*
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.BasicInventory
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.DefaultedList

class Cont2(containerType : ContainerType<*>, int_1 : Int, private val playerInventory : PlayerInventory, private val inventory : Inventory, private val rows : Int) :
        Container(containerType , int_1) {

    override fun canUse(playerEntity_1: PlayerEntity): Boolean {
        return this.inventory.canPlayerUseInv(playerEntity_1)
    }

    init { // Look at GenericContainer.java
        checkContainerSize(inventory, this.rows* 9)
        inventory.onInvOpen(playerInventory.player)
        val int_3 = (this.rows - 4) * 18

        var int_8: Int = 0
        var column: Int
        // Inventory size
        while (int_8 < this.rows) {
            column = 0
            while (column < 9) {
                this.addSlot(Slot(inventory, column + int_8 * 9, 8 + column * 18, 18 + int_8 * 18))
                ++column
            }
            ++int_8
        }

        int_8 = 0
        // Player inventory
        while (int_8 < 3) {
            column = 0
            while (column < 9) {
                this.addSlot(Slot(playerInventory, column + int_8 * 9 + 9, 8 + column * 18, 103 + int_8 * 18 + int_3))
                ++column
            }
            ++int_8
        }

        // Hot Bar
        int_8 = 0
        while (int_8 < 9) {
            this.addSlot(Slot(playerInventory, int_8, 8 + int_8 * 18, 161 + int_3))
            ++int_8
        }
    }

    override fun close(playerEntity_1: PlayerEntity) {
        super.close(playerEntity_1)
        stack.removeSubTag("Items")
        val compoundTag = Inventories.toTag(CompoundTag(), this.getStackList(this.inventory), false)
        if (!compoundTag.isEmpty) {
            stack.putSubTag("Items", compoundTag)
        }
        this.inventory.onInvClose(playerEntity_1)
    }

    private fun getStackList(inventory : Inventory): DefaultedList<ItemStack>? {
        val list : DefaultedList<ItemStack> = DefaultedList.of()
        for ( i in 0..inventory.invSize){
            list.add(inventory.getInvStack(i))
        }
        return list
    }

    fun getInventory(): Inventory {
        return this.inventory
    }

    @Environment(EnvType.CLIENT)
    fun getRows(): Int {
        return this.rows
    }





    private val stack: ItemStack =  playerInventory.player.mainHandStack

    constructor(int_1 : Int, playerInventory_1 : PlayerInventory, inventory_1 : Inventory) :
            this(ContainerType.GENERIC_9X6, int_1, playerInventory_1, inventory_1, 6)
    
    override fun onSlotClick(int_1: Int, int_2: Int, slotActionType: SlotActionType?, player: PlayerEntity?): ItemStack {
        //return if(int_1 > 0 && this.getSlot(int_1).stack == player?.getStackInHand(Hand.MAIN_HAND)) {
        return if(int_1 > 0 && this.getSlot(int_1).stack.item == stack.item) {
            ItemStack.EMPTY
        } else {
            super.onSlotClick(int_1, int_2, slotActionType, player)
        }
    }

    /* Functions that seem useless
    override fun canInsertIntoSlot(slot_1: Slot?): Boolean
    override fun canInsertIntoSlot(itemStack_1: ItemStack?, slot_1: Slot?): Boolean
    override fun canUse(player: PlayerEntity?): Boolean
    override fun dropInventory(player: PlayerEntity?, world_1: World?, inventory: Inventory?)
     */
}