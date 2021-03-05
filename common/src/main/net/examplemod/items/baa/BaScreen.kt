package net.examplemod.items.baa

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class BaScreen(
    handler: ScreenHandler,
    inventory: PlayerInventory,
    title: Text,
) : HandledScreen<ScreenHandler>(handler, inventory, title) {
    private val texture = Identifier("yummy", "textures/gui/9x9_wood.png")

    override fun drawBackground(matrices: MatrixStack?, delta: Float, mouseX: Int, mouseY: Int) {
        val minecraftClient = MinecraftClient.getInstance()
        minecraftClient.textureManager.bindTexture(texture)
        val x = (width - backgroundWidth) / 2
        val y = (height - backgroundHeight) / 2
        this.drawTexture(matrices,
            x,
            y,
            0,
            0,
            175,
            175
        )
    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(matrices)
        super.render(matrices, mouseX, mouseY, delta)
        drawMouseoverTooltip(matrices, mouseX, mouseY)
    }

    override fun init() {
        super.init()
        // Center the title
        //titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2
    }
}