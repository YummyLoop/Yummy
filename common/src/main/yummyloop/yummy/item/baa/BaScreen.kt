package yummyloop.yummy.item.baa

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import yummyloop.common.client.Texture
import yummyloop.common.client.screen.ingame.ScreenWithPlayerInventory

@Environment(EnvType.CLIENT)
class BaScreen<H>(
    handler: H,
    inventory: PlayerInventory,
    title: Text,
) : ScreenWithPlayerInventory<H>(handler, inventory, title) where H : ScreenHandler {
    override fun getBackgroundTexture(): Texture {
        return Texture("minecraft", "textures/gui/container/generic_54.png")
        // ("minecraft", "textures/gui/container/dispenser.png")
        // ("yummy", "textures/gui/9x9_wood.png")
    }
}