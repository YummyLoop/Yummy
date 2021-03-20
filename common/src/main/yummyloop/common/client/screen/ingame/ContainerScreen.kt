package yummyloop.common.client.screen.ingame

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import yummyloop.common.client.Texture
import yummyloop.common.screen.handler.ContainerScreenHandler

@Environment(EnvType.CLIENT)
open class ContainerScreen<H>(
    handler: H,
    inventory: PlayerInventory,
    title: Text,
) : HandledScreen<H>(handler, inventory, title) where H : ContainerScreenHandler {
    private val columns = handler.columns
    private val rows = handler.rows

    init {
        //passEvents = false // should this be false ?
        backgroundWidth = 176
        backgroundHeight = 114 + rows * 18
        titleX = 8
        titleY = 6
        playerInventoryTitleX = 8
        playerInventoryTitleY = backgroundHeight - 94
    }

    protected open fun getBackgroundTexture(): Texture {
        return Texture("minecraft", "textures/gui/container/generic_54.png")
    }

    override fun drawBackground(matrices: MatrixStack?, delta: Float, mouseX: Int, mouseY: Int) {
        bindTexture(getBackgroundTexture())
        val x = (width - backgroundWidth) / 2
        val y = (height - backgroundHeight) / 2
        // Todo : divide the draw texture into four or five
        DrawableHelper.drawTexture(matrices,
            x,
            y,
            0F,
            0F,
            backgroundWidth,
            this.rows * 18 + 17,
            getBackgroundTexture().xSize,
            getBackgroundTexture().ySize
        )

        DrawableHelper.drawTexture(matrices,
            x,
            y + this.rows * 18 + 17,
            0F,
            126F,
            backgroundWidth,
            96,
            getBackgroundTexture().xSize,
            getBackgroundTexture().ySize
        )
    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(matrices)
        super.render(matrices, mouseX, mouseY, delta)
        drawMouseoverTooltip(matrices, mouseX, mouseY)
    }

    private fun bindTexture(texture: Texture) = MinecraftClient.getInstance().textureManager.bindTexture(texture.get())
}