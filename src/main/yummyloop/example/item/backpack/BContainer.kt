package yummyloop.example.item.backpack

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.screen.ingame.ContainerScreen54
import net.minecraft.container.ContainerType
import net.minecraft.container.GenericContainer
import net.minecraft.container.SlotActionType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.BasicInventory
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.text.TranslatableText
import net.minecraft.util.DefaultedList
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.PacketByteBuf
import yummyloop.example.config.Config
import yummyloop.example.util.registry.ClientManager
import yummyloop.example.util.registry.RegistryManager

class BContainer(
        containerType: ContainerType<*>?,
        syncId: Int,
        player: PlayerEntity,
        inventory: Inventory) :
        GenericContainer(
                containerType,
                syncId,
                player.inventory,
                inventory,
                (inventory.invSize).div(9)
        ) {

    constructor(syncId : Int, player : PlayerEntity, buf: PacketByteBuf) :
            this(ContainerType.GENERIC_9X6, syncId, player, BasicInventory(buf.readInt()*9))

    companion object Register {
        private val name = this::class.qualifiedName!!.toLowerCase()
        val id = Identifier(Config.modId, name)
        init {
            RegistryManager.registerContainer(name) { syncId : Int, identifier : Identifier, player : PlayerEntity, buf : PacketByteBuf -> BContainer(syncId, player, buf) }
            ClientManager.registerScreen(name) { syncId : Int, identifier : Identifier, player : PlayerEntity, buf : PacketByteBuf -> Screen(syncId, player, buf) }
        }

        @Environment(EnvType.CLIENT)
        open class Screen(syncId: Int, player: PlayerEntity, buf: PacketByteBuf) :
                ContainerScreen54(BContainer(syncId, player, buf), player.inventory, TranslatableText(buf.readString()))
    }

    // Get the hand
    private val hand : Hand? = if (player.getStackInHand(Hand.MAIN_HAND).item is Backpack && player.getStackInHand(Hand.MAIN_HAND).count == 1){
        Hand.MAIN_HAND
    }else{
        Hand.OFF_HAND
    }
    private val stack: ItemStack =  player.getStackInHand(hand)

    init {
        // Load inventory from nbt tag
        val inventoryList = DefaultedList.ofSize(this.inventory.invSize, ItemStack.EMPTY);
        val compoundTag = stack.getSubTag("Items")
        if (compoundTag != null){
            Inventories.fromTag(compoundTag, inventoryList)

            for ((c, i) in inventoryList.withIndex()){
                this.inventory?.setInvStack(c,i)
            }
        }
        //this.inventory =  BasicInventory(*inventoryList.toTypedArray())
    }

    override fun canUse(playerEntity_1: PlayerEntity): Boolean {
        return this.inventory.canPlayerUseInv(playerEntity_1)
    }

    override fun close(player: PlayerEntity) {
        // Remove old nbt tag
        stack.removeSubTag("Items")
        // Save inventory to nbt tag
        val compoundTag = Inventories.toTag(CompoundTag(), this.getStackList(this.inventory), false)
        if (!compoundTag.isEmpty) {
            stack.putSubTag("Items", compoundTag)
        }
        super.close(player)
        this.inventory.onInvClose(player)
    }

    // See ShulkerBoxContainer
    override fun transferSlot(player: PlayerEntity?, slotNum: Int): ItemStack {
        var itemStackCopy = ItemStack.EMPTY
        val activeSlot = this.slotList[slotNum]
        if (activeSlot.hasStack()) {// Slot is not empty
            val itemStack = activeSlot.stack
            itemStackCopy = itemStack.copy()

            if (slotNum < this.inventory.invSize) {// index is not out of bounds ?
                if (!this.insertItem(itemStack, this.inventory.invSize, this.slotList.size, true)) {// if cannot insert item
                    return ItemStack.EMPTY
                }
            } else if (!this.insertItem(itemStack, 0, this.inventory.invSize, false)) {// if cannon insert item
                return ItemStack.EMPTY
            }

            if (itemStack.isEmpty) {// Slot has stack but stack is empty
                activeSlot.stack = ItemStack.EMPTY
            } else {
                activeSlot.markDirty()
            }
        }
        return itemStackCopy
    }

    override fun onSlotClick(int_1: Int, int_2: Int, slotActionType: SlotActionType, player: PlayerEntity): ItemStack {
        return if (int_1 > 0 && this.getSlot(int_1).stack == player.getStackInHand(hand)){
            ItemStack.EMPTY
        } else {
            super.onSlotClick(int_1, int_2, slotActionType, player)
        }
    }

    private fun getStackList(inventory : Inventory): DefaultedList<ItemStack>? {
        val list : DefaultedList<ItemStack> = DefaultedList.of()
        for ( i in 0 until inventory.invSize){
            list.add(inventory.getInvStack(i))
        }
        return list
    }
}

/*
    init { // Look at GenericContainer.java
        checkContainerSize(inventory, this.rows* 9)
        inventory?.onInvOpen(playerInventory.player)
        val twiceInventorySize = (this.rows - 4) * 18

        // Inventory size
        for (r in 0 until this.rows) {
            for (c in 0 until 9) {
                this.addSlot(BoxSlot(this.inventory, c + r * 9, 8 + c * 18, 18 + r * 18))
            }
        }
        // Player inventory
        for (r in 0 until 3) {
            for (c in 0 until 9) {
                this.addSlot(Slot(playerInventory, c + r * 9 + 9, 8 + c * 18, 103 + r * 18 + twiceInventorySize))
            }
        }
        // Hot Bar
        for (c in 0 until 9) {
            this.addSlot(Slot(playerInventory, c, 8 + c * 18, 161 + twiceInventorySize))
        }
    }

    private class BoxSlot(inventory_1: Inventory, int_1: Int, int_2: Int, int_3: Int) : Slot(inventory_1, int_1, int_2, int_3) {
        override fun canInsert(itemStack_1: ItemStack): Boolean {
            return itemStack_1.item !is Backpack
        }
    }
 */