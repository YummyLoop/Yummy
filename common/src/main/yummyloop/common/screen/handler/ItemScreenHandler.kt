package yummyloop.common.screen.handler

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.Slot
import net.minecraft.screen.slot.SlotActionType
import net.minecraft.util.collection.DefaultedList
import yummyloop.common.screen.defaultTransferSlot

abstract class ItemScreenHandler(
    handlerType: ScreenHandlerType<*>,
    syncId: Int,
    val playerInventory: PlayerInventory,
    val itemStack: ItemStack,
    val isOffHand: Boolean,
) : ScreenHandler(handlerType, syncId) {

    private var columns: Int = 9
    private var rows: Int = 6
    private val invSize: Int = columns * rows
    private var localInventory: Inventory = fromTag()
    private val offsetY: Int = (this.rows - 4) * 18

    fun getColumns(): Int = this.columns
    fun getRows(): Int = this.rows
    fun getInventorySize(): Int = this.invSize

    init {
        init()
    }

    /** Checks every tick? */
    override fun canUse(player: PlayerEntity?): Boolean = itemStackExists()

    fun init() {
        // If the itemStack does not contain UUID, somehow it happens the first try from the Offhand, seems fixed?
        if (!itemStack.orCreateNbt.contains("uuid")) close(playerInventory.player)


        // The Inventory
        for (r in 0 until rows) for (c in 0 until columns)
            addSlot(object : Slot(localInventory, c + r * columns, 8 + c * 18, 18 + r * 18) {
                override fun canInsert(stack: ItemStack?): Boolean =
                    !(stack!!.isItemEqual(itemStack))
            })

        //The player inventory
        for (r in 0 until 3) for (c in 0 until 9)
            addSlot(Slot(playerInventory, c + r * 9 + 9, 8 + c * 18, 103 + r * 18 + offsetY))

        //The player Hotbar
        for (c in 0 until 9) {
            if (isOffHand || playerInventory.selectedSlot != c) {
                addSlot(Slot(playerInventory, c, 8 + c * 18, 161 + offsetY))
            } else {
                addSlot(object : Slot(playerInventory, c, 8 + c * 18, 161 + offsetY) {
                    override fun canInsert(stack: ItemStack?): Boolean = false
                    override fun canTakeItems(playerEntity: PlayerEntity?): Boolean = false
                })
            }
        }
    }

    override fun onSlotClick(i: Int, j: Int, actionType: SlotActionType?, playerEntity: PlayerEntity?) {
        saveTag()
        if (isOffHand && actionType == SlotActionType.SWAP) {
            //return ItemStack.EMPTY
        }
        return super.onSlotClick(i, j, actionType, playerEntity)
    }

    override fun onContentChanged(inventory: Inventory?) {
        saveTag()
        super.onContentChanged(inventory)
    }

    /** It is called every tick the screen is open */
    private fun itemStackExists(): Boolean {
        val handStack: ItemStack =
            if (isOffHand) playerInventory.player.offHandStack else playerInventory.player.mainHandStack

        if (!handStack.isItemEqual(this.itemStack)
            || !handStack.orCreateNbt.contains("uuid")
            || !itemStack.orCreateNbt.contains("uuid")
        ) return false

        val handStackUuid = handStack.orCreateNbt.getUuid("uuid")
        val stackUuid = itemStack.orCreateNbt.getUuid("uuid")
        return handStackUuid.compareTo(stackUuid) == 0
    }

    override fun close(player: PlayerEntity?) {
        saveTag()
        super.close(player)
    }

    private fun fromTag(): Inventory {
        val itemStackList = DefaultedList.ofSize(invSize, ItemStack.EMPTY)
        Inventories.readNbt(this.itemStack.orCreateNbt, itemStackList)
        return SimpleInventory(*itemStackList.toTypedArray())
    }

    private fun saveTag() {
        val myList = DefaultedList.ofSize(invSize, ItemStack.EMPTY)
        for (i in 0 until invSize) {
            myList[i] = localInventory.getStack(i)
        }
        Inventories.writeNbt(this.itemStack.orCreateNbt, myList)
    }

    // Shift + Player Inv Slot
    override fun transferSlot(player: PlayerEntity?, invSlot: Int): ItemStack {
        return defaultTransferSlot(player, invSlot, localInventory, this::insertItem)
    }
}