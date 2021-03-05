package net.examplemod.items.baa

import net.examplemod.LOG
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text

class BaFactory(var stack : ItemStack = ItemStack.EMPTY) : NamedScreenHandlerFactory {
    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity): ScreenHandler? {
        LOG.info("at the factory:" + stack.item.toString())
        return BaHandler(syncId, inv, player, stack)
    }

    /**
     * Returns the title of this screen handler; will be a part of the open
     * screen packet sent to the client.
     */
    override fun getDisplayName(): Text {
        return Text.of("backpack?")
    }
}