package net.examplemod.items.baa

import net.examplemod.client.gui.screen.ingame.ScreenWithPlayerInventory
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

@Environment(EnvType.CLIENT)
class BaScreen(
    handler: BaHandler,
    inventory: PlayerInventory,
    title: Text,
) : ScreenWithPlayerInventory<BaHandler>(handler, inventory, title) {

    override fun getBackgroundTexture(): Texture {
        return Texture("minecraft", "textures/gui/container/generic_54.png")
        // ("minecraft", "textures/gui/container/dispenser.png")
        // ("yummy", "textures/gui/9x9_wood.png")
    }

    override fun tick() {
        super.tick()
        if (!handler.itemStackExists()) {
            onClose()
        }
    }
}