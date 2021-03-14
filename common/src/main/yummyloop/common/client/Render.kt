package yummyloop.common.client

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.client.texture.TextureManager
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import com.mojang.blaze3d.systems.RenderSystem as renderSystem

@Environment(EnvType.CLIENT)
object Render {

    private val client: MinecraftClient by lazy { MinecraftClient.getInstance() }
    private val itemRenderer: ItemRenderer by lazy { client.itemRenderer }
    private val textRenderer: TextRenderer by lazy { client.textRenderer }
    private val textureManager: TextureManager by lazy { client.textureManager }


    fun push() = renderSystem.pushMatrix()
    fun pop() = renderSystem.popMatrix()

    fun push(matrix: MatrixStack) {
        push()
        matrix.push()
    }

    fun pop(matrix: MatrixStack) {
        matrix.pop()
        pop()
    }

    inline operator fun invoke(supplier: (Render) -> Unit) {
        push()
        supplier.invoke(this)
        pop()
    }

    inline operator fun invoke(matrix: MatrixStack, supplier: (Render) -> Unit) {
        push(matrix)
        supplier.invoke(this)
        pop(matrix)
    }

    inline fun <reified N> translate(x: N, y: N, z: N) where N : Number {
        if (x is Float && y is Float && z is Float) renderSystem.translatef(x, y, z)
        else if (x is Double && y is Double && z is Double) renderSystem.translated(x, y, z)
        else renderSystem.translated(x.toDouble(), y.toDouble(), z.toDouble())
    }

    fun bindTexture(id: Identifier) = textureManager.bindTexture(id)

    fun bindTexture(texture: Texture) = bindTexture(texture.get())

    object Item {
        fun inGuiWithOverrides(stack: ItemStack, x: Int, y: Int) {
            itemRenderer.renderInGuiWithOverrides(stack, x, y)
        }

        fun guiOverlay(renderer: TextRenderer, stack: ItemStack, x: Int, y: Int, countLabel: String? = null) {
            itemRenderer.renderGuiItemOverlay(renderer, stack, x, y, countLabel)
        }

        fun guiOverlay(stack: ItemStack, x: Int, y: Int, countLabel: String? = null) {
            itemRenderer.renderGuiItemOverlay(textRenderer, stack, x, y, countLabel)
        }
    }

}