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

    val client: MinecraftClient
        inline get() = MinecraftClient.getInstance()
    val itemRenderer: ItemRenderer
        inline get() = client.itemRenderer
    val textRenderer: TextRenderer
        inline get() = client.textRenderer
    val textureManager: TextureManager
        inline get() = client.textureManager

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

    inline operator fun invoke(supplier: Render.() -> Unit) {
        push()
        supplier.invoke(this)
        pop()
    }

    inline operator fun invoke(matrix: MatrixStack, supplier: Render.() -> Unit) {
        push(matrix)
        supplier.invoke(this)
        pop(matrix)
    }

    inline fun <reified N> translate(x: N, y: N, z: N) where N : Number {
        if (x is Float && y is Float && z is Float) renderSystem.translatef(x, y, z)
        else if (x is Double && y is Double && z is Double) renderSystem.translated(x, y, z)
        else renderSystem.translated(x.toDouble(), y.toDouble(), z.toDouble())
    }

    inline fun <reified N> scale(x: N, y: N, z: N) where N : Number {
        if (x is Float && y is Float && z is Float) renderSystem.scalef(x, y, z)
        else if (x is Double && y is Double && z is Double) renderSystem.scaled(x, y, z)
        else renderSystem.scaled(x.toDouble(), y.toDouble(), z.toDouble())
    }

    fun bindTexture(id: Identifier) = textureManager.bindTexture(id)

    fun bindTexture(texture: Texture) = bindTexture(texture.get())

    object Item {
        var zOffset: Float
            inline get() = itemRenderer.zOffset
            inline set(value) {
                itemRenderer.zOffset = value
            }

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