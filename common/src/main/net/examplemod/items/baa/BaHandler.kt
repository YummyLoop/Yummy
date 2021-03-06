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
        if (stack != ItemStack.EMPTY) this.itemStack = stack
    }

    companion object {
        var rType: RegistrySupplier<ScreenHandlerType<out BaHandler>?>? = null
    }

    private var itemStack = buf.readItemStack()
    private var isOffHand = buf.readBoolean()
    private var invSize = 9
    private var localInventory = SimpleInventory(invSize)
    private var playerSlotWithStack = playerInventory.getSlotWithStack(itemStack)

    override fun canUse(player: PlayerEntity?): Boolean = true

    init {
        fromTag()

        // The Inventory
        for (m in 0..2) for (l in 0..2) this.addSlot(Slot(localInventory, l + m * 3, 62 + l * 18, 17 + m * 18))

        //The player inventory
        for (m in 0..2) for (l in 0..8) this.addSlot(Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18))

        //The player Hotbar
        var tempSlot: Slot
        for (m in 0..8) {
            tempSlot =
                if (playerInventory.selectedSlot == m && !isOffHand) {
                    object : Slot(playerInventory, m, 8 + m * 18, 142) {
                        override fun canInsert(stack: ItemStack?): Boolean = false
                        override fun canTakeItems(playerEntity: PlayerEntity?): Boolean = false
                    }
                } else {
                    Slot(playerInventory, m, 8 + m * 18, 142)
                }
            this.addSlot(tempSlot)
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
        return playerInventory.getStack(playerSlotWithStack).item == itemStack.item
    }

    override fun close(player: PlayerEntity?) {
        saveTag()
        super.close(player)
    }

    private fun fromTag() {
        val itemStackList = DefaultedList.ofSize(invSize, ItemStack.EMPTY)
        Inventories.fromTag(itemStack.orCreateTag, itemStackList)
        localInventory = SimpleInventory(*itemStackList.map { it }.toTypedArray())
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