package net.examplemod.items.baa

import me.shedaniel.architectury.registry.RegistrySupplier
import net.examplemod.screen.defaultTransferSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.Slot
import net.minecraft.screen.slot.SlotActionType
import net.minecraft.util.collection.DefaultedList

class BaHandler(syncId: Int, val playerInventory: PlayerInventory, buf: PacketByteBuf) :
    ScreenHandler(this.rType!!.get(), syncId) {

    constructor(syncId: Int, playerInventory: PlayerInventory, buf: PacketByteBuf, stack: ItemStack)
            : this(syncId, playerInventory, buf) {
        //LOG.info("stacks b: ${itemStack.item}, f: ${stack.item}") //the buffer is probably unnecessary?
        if (stack != ItemStack.EMPTY) this.itemStack = stack
    }

    companion object {
        var rType: RegistrySupplier<ScreenHandlerType<out BaHandler>?>? = null
    }

    private var itemStack = buf.readItemStack()
    private var isOffHand = buf.readBoolean()
    private var columns = 9
    private var rows = 6
    private var invSize = columns * rows
    private var localInventory = SimpleInventory(invSize)

    override fun canUse(player: PlayerEntity?): Boolean = true

    init {
        fromTag()
        val offsetY = (this.rows - 4) * 18
        // The Inventory
        for (r in 0 until rows) for (c in 0 until columns) addSlot(
            object : Slot(localInventory, c + r * columns, 8 + c * 18, 18 + r * 18) {
                override fun canInsert(stack: ItemStack?): Boolean =
                    !(stack!!.isItemEqual(this@BaHandler.itemStack))
            }
        )

        //The player inventory
        for (r in 0 until 3) for (c in 0 until 9)
            addSlot(Slot(playerInventory, c + r * 9 + 9, 8 + c * 18, 103 + r * 18 + offsetY))

        //The player Hotbar
        var tempSlot: Slot
        for (c in 0 until 9) {
            tempSlot =
                if (playerInventory.selectedSlot == c && !isOffHand) {
                    object : Slot(playerInventory, c, 8 + c * 18, 161 + offsetY) {
                        override fun canInsert(stack: ItemStack?): Boolean = false
                        override fun canTakeItems(playerEntity: PlayerEntity?): Boolean = false
                    }
                } else {
                    Slot(playerInventory, c, 8 + c * 18, 161 + offsetY)
                }
            addSlot(tempSlot)
        }
    }

    override fun onSlotClick(i: Int, j: Int, actionType: SlotActionType?, playerEntity: PlayerEntity?): ItemStack {
        saveTag()
        return super.onSlotClick(i, j, actionType, playerEntity)
    }

    override fun onContentChanged(inventory: Inventory?) {
        saveTag()
        super.onContentChanged(inventory)
    }

    fun itemStackExists(): Boolean {
        val handStack: ItemStack =
            if (isOffHand) playerInventory.player.offHandStack else playerInventory.player.mainHandStack

        if (!handStack.isItemEqual(itemStack)
            || !handStack.orCreateTag.contains("uuid")
            || !itemStack.orCreateTag.contains("uuid")
        ) return false

        val handStackUuid = handStack.orCreateTag.getUuid("uuid")
        val stackUuid = itemStack.orCreateTag.getUuid("uuid")
        return handStackUuid.compareTo(stackUuid) == 0
    }

    override fun close(player: PlayerEntity?) {
        saveTag()
        super.close(player)
    }

    private fun fromTag() {
        val itemStackList = DefaultedList.ofSize(invSize, ItemStack.EMPTY)
        Inventories.fromTag(itemStack.orCreateTag, itemStackList)
        localInventory = SimpleInventory(*itemStackList.toTypedArray())
    }

    private fun saveTag() {
        val myList = DefaultedList.ofSize(invSize, ItemStack.EMPTY)
        for (i in 0 until invSize) {
            myList[i] = localInventory.getStack(i)
        }
        Inventories.toTag(itemStack.orCreateTag, myList)
    }

    // Shift + Player Inv Slot
    override fun transferSlot(player: PlayerEntity?, invSlot: Int): ItemStack {
        return defaultTransferSlot(player, invSlot, localInventory, this::insertItem)
    }
}