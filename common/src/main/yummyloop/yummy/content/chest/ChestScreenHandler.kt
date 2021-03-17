package yummyloop.yummy.content.chest

import me.shedaniel.architectury.registry.RegistrySupplier
import net.minecraft.advancement.criterion.InventoryChangedCriterion
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.Slot


class ChestScreenHandler(
    syncId: Int, playerInventory: PlayerInventory, buf: PacketByteBuf,
    var inventory: Inventory = SimpleInventory(9),
) : ScreenHandler(type?.get(), syncId) {
    companion object {
        var type: RegistrySupplier<ScreenHandlerType<out ScreenHandler>>? = null
    }

    override fun canUse(player: PlayerEntity?): Boolean {
        return inventory.canPlayerUse(player)
    }

    // Shift + Player Inv Slot
    override fun transferSlot(player: PlayerEntity?, invSlot: Int): ItemStack {
        var newStack: ItemStack = ItemStack.EMPTY
        val slot: Slot = this.slots[invSlot]
        if (slot.hasStack()) {
            val originalStack: ItemStack = slot.stack
            newStack = originalStack.copy()
            if (invSlot < inventory.size()) {
                if (!this.insertItem(originalStack, inventory.size(), this.slots.size, true)) {
                    return ItemStack.EMPTY
                }
            } else if (!this.insertItem(originalStack, 0, inventory.size(), false)) {
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

    //This constructor gets called from the BlockEntity on the server without calling the other constructor first, the server knows the inventory of the container
    //and can therefore directly provide it as an argument. This inventory will then be synced to the client.
    init {
        checkSize(inventory, 9)
        //some inventories do custom logic when a player opens it.
        inventory.onOpen(playerInventory.player)

        //This will place the slot in the correct locations for a 3x3 Grid. The slots exist on both server and client!
        //This will not render the background of the slots however, this is the Screens job
        //Our inventory
        for (m in 0..2) for (l in 0..2) this.addSlot(Slot(inventory, l + m * 3, 62 + l * 18, 17 + m * 18))

        //The player inventory
        for (m in 0..2) for (l in 0..8) this.addSlot(Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18))

        //The player Hotbar
        for (m in 0..8) this.addSlot(Slot(playerInventory, m, 8 + m * 18, 142))
    }

    override fun close(player: PlayerEntity?) {
        super.close(player)
        inventory.onClose(player)
    }
}

