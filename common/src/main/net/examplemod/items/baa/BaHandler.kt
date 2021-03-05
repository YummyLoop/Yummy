package net.examplemod.items.baa

import me.shedaniel.architectury.registry.RegistrySupplier
import net.examplemod.LOG
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.Slot
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.collection.DefaultedList

class BaHandler(id: Int, playerInventory: PlayerInventory, player: PlayerEntity, private val stack: ItemStack) :
    ScreenHandler(this.rType!!.get(), id) {
    companion object {
        var rType: RegistrySupplier<ScreenHandlerType<out ScreenHandler>?>? = null
    }

    constructor(syncId: Int, playerInventory: PlayerInventory) : this(syncId,
        playerInventory,
        playerInventory.player,
        ItemStack.EMPTY) {
    }

    override fun canUse(player: PlayerEntity?): Boolean {
        return true
    }

    private var invSize = 9
    private var inv = SimpleInventory(invSize)


    init {
        LOG.info("at the handler:" + stack.item.toString())

        if (player is ServerPlayerEntity) {
            LOG.info("tag " + stack.tag.toString())

            val itemStackList = DefaultedList.ofSize(invSize, ItemStack.EMPTY)
            Inventories.fromTag(stack.orCreateTag, itemStackList)

            inv = SimpleInventory(*itemStackList.map { it }.toTypedArray())

            //inv.setStack(0, ItemStack(Items.ENCHANTED_GOLDEN_APPLE))
        }

        for (m in 0..2) for (l in 0..2) this.addSlot(Slot(inv, l + m * 3, 62 + l * 18, 17 + m * 18))

        //The player inventory
        for (m in 0..2) for (l in 0..8) this.addSlot(Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18))
        //The player Hotbar
        for (m in 0..8) {
            if (playerInventory.selectedSlot == m) continue
            this.addSlot(Slot(playerInventory, m, 8 + m * 18, 142))
        }
    }

    override fun close(player: PlayerEntity?) {

        if (player is ServerPlayerEntity) {
            LOG.info("its closing !")

            val myList = DefaultedList.ofSize(invSize, ItemStack.EMPTY)
            for (i in 0 until invSize) {
                myList[i] = inv.getStack(i)
            }

            Inventories.toTag(stack.tag, myList)

            //SimpleInventory(*itemStackList)

            //SimpleInventory()
            //inv.setStack(0, ItemStack(Items.ENCHANTED_GOLDEN_APPLE))
        }
        super.close(player)
    }

    // Shift + Player Inv Slot
    override fun transferSlot(player: PlayerEntity?, invSlot: Int): ItemStack {
        var newStack: ItemStack = ItemStack.EMPTY
        val slot: Slot = this.slots[invSlot]
        if (slot.hasStack()) {
            val originalStack: ItemStack = slot.stack
            newStack = originalStack.copy()
            if (invSlot < inv.size()) {
                if (!this.insertItem(originalStack, inv.size(), this.slots.size, true)) {
                    return ItemStack.EMPTY
                }
            } else if (!this.insertItem(originalStack, 0, inv.size(), false)) {
                return ItemStack.EMPTY
            }
            if (originalStack.isEmpty) {
                slot.stack = ItemStack.EMPTY
            } else {
                slot.markDirty()
            }
        }
        return newStack
    }
}