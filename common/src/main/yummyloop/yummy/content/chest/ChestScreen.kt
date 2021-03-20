package yummyloop.yummy.content.chest

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import yummyloop.common.client.Texture
import yummyloop.common.client.screen.ingame.ContainerScreen
import yummyloop.common.screen.handler.ContainerScreenHandler

@Environment(EnvType.CLIENT)
open class ChestScreen<H>(
    handler: H,
    inventory: PlayerInventory,
    title: Text,
) : ContainerScreen<H>(handler, inventory, title) where H : ContainerScreenHandler {
    override fun getBackgroundTexture(): Texture = Texture("minecraft", "textures/gui/container/generic_54.png")
}