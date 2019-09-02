package yummyloop.example.block.entity

import com.mojang.blaze3d.platform.GLX
import com.mojang.blaze3d.platform.GlStateManager
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.block.entity.BellModel
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.entity.model.ShulkerEntityModel
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.item.ItemStack
import net.minecraft.item.Items as VanillaItems
import net.minecraft.predicate.entity.DistancePredicate.y
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import kotlin.math.sin

@Environment(EnvType.CLIENT)
class TemplateBlockEntityRenderer :
        BlockEntityRenderer<TemplateBlockEntity>() {
    companion object{
        private val stack = ItemStack(VanillaItems.JUKEBOX, 1)

        private val BELL_BODY_TEXTURE = Identifier("textures/entity/bell/bell_body.png")
        private val model = BellModel()
    }

    override fun render(blockEntity: TemplateBlockEntity, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int) {

        GlStateManager.pushMatrix()
        GlStateManager.enableRescaleNormal()
        this.bindTexture(BELL_BODY_TEXTURE)
        GlStateManager.translatef(x.toFloat(), y.toFloat(), z.toFloat())

        model.method_17070(0F, 0F, 0.0625f)
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f)

        // Calculate the current offset in the y value
        val offset = sin((blockEntity.world!!.time + partialTicks) / 8.0) / 4.0
        // Move the item
        GlStateManager.translated(x + 0.5, y + 1.25 + offset, z + 0.5)

        // Rotate the item
        GlStateManager.rotatef((blockEntity.world!!.time + partialTicks) * 4, 0f, 1f, 0f)

        val light = blockEntity.world!!.getLightmapIndex(blockEntity.pos.up(), 0)
        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, (light and 0xFFFF).toFloat(), (light shr 16 and 0xFFFF).toFloat())

        MinecraftClient.getInstance().itemRenderer.renderItem(stack, ModelTransformation.Type.GROUND);
        //end of jukebox

        GlStateManager.popMatrix()
    }
}