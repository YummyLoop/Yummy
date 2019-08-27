package yummyloop.example.item.backpack

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.container.Container
import net.minecraft.container.ContainerType
import net.minecraft.container.Slot
import net.minecraft.container.SlotActionType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.DefaultedList

class Cont2(containerType : ContainerType<*>, int_1 : Int, private val playerInventory : PlayerInventory, private val inventory : Inventory, private val rows : Int) :
        Container(containerType , int_1) {
    private val stack: ItemStack =  playerInventory.player.mainHandStack

    constructor(int_1 : Int, playerInventory_1 : PlayerInventory, inventory_1 : Inventory) :
            this(ContainerType.GENERIC_9X6, int_1, playerInventory_1, inventory_1, 6)

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
                this.addSlot(BoxSlot(inventory, column + int_8 * 9, 8 + column * 18, 18 + int_8 * 18))
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
            this.addSlot(HotBarSlot(playerInventory, int_8, 8 + int_8 * 18, 161 + int_3))
            ++int_8
        }
    }

    override fun canUse(playerEntity_1: PlayerEntity): Boolean {
        return this.inventory.canPlayerUseInv(playerEntity_1)
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
        for ( i in 0 until inventory.invSize){
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

    override fun onSlotClick(int_1: Int, int_2: Int, slotActionType: SlotActionType?, player: PlayerEntity?): ItemStack {
        //return if(int_1 > 0 && this.getSlot(int_1).stack == player?.getStackInHand(Hand.MAIN_HAND)) {
        //return if (int_1 > 0 && this.getSlot(int_1).stack.item is Backpack) {
        return if (int_1 >= ((this.rows + 3) * 9) ) {// If its the HotBar
            ItemStack.EMPTY
        } else {
            super.onSlotClick(int_1, int_2, slotActionType, player)
        }
    }

    private class BoxSlot(inventory_1: Inventory, int_1: Int, int_2: Int, int_3: Int) : Slot(inventory_1, int_1, int_2, int_3) {
        override fun canInsert(itemStack_1: ItemStack): Boolean {
            return itemStack_1.item !is Backpack
        }
    }
    private class HotBarSlot(inventory_1: Inventory, int_1: Int, int_2: Int, int_3: Int) : Slot(inventory_1, int_1, int_2, int_3) {
        override fun canInsert(itemStack_1: ItemStack): Boolean {
            return itemStack_1.item !is Backpack
        }

        override fun canTakeItems(playerEntity_1: PlayerEntity?): Boolean {
            return this.stack.item !is Backpack // gets stuck when this changes for some reason
            //return false
        }
    }
}