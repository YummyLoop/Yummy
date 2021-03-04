package net.examplemod.test.gui

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text

class Factory1 : NamedScreenHandlerFactory {
    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity?): ScreenHandler? {
        return ScreenHandler1(syncId, inv)
    }

    /**
     * Returns the title of this screen handler; will be a part of the open
     * screen packet sent to the client.
     */
    override fun getDisplayName(): Text {
        return Text.of("hello there")
    }
}