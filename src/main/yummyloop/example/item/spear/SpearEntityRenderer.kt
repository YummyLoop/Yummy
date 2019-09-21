package yummyloop.example.item.spear

import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import yummyloop.example.item.Items

class SpearEntityRenderer : EntityRenderer<SpearEntity> {
    constructor(e1 : EntityRenderDispatcher) : super(e1)

    companion object{
        val defaultItem = Items["spear"]
    }

    val stack = ItemStack(defaultItem)

    override fun getTexture(var1: SpearEntity?): Identifier? {
        return Identifier("minecraft:textures/item/iron_ingot.png") // Is ignored
    }

    override fun render(entity: SpearEntity, x: Double, y: Double, z: Double, float_1: Float, float_2: Float) {
        glMatrix {
            GlStateManager.translatef(x.toFloat(), y.toFloat(), z.toFloat())
            GlStateManager.rotatef(MathHelper.lerp(float_2, entity.prevYaw, entity.yaw) - 90.0f, 0.0f, 1.0f, 0.0f)
            GlStateManager.rotatef(MathHelper.lerp(float_2, entity.prevPitch, entity.pitch) + 90.0f, 0.0f, 0.0f, 1.0f)

            glMatrix {
                GlStateManager.translatef(0F, 0.4F,0F)
                GlStateManager.rotatef(180F,0F,1F, 0F)
                MinecraftClient.getInstance().itemRenderer.renderItem(this.stack, ModelTransformation.Type.HEAD)
            }
        }
    }

    private fun glMatrix(op : () -> Unit){
        GlStateManager.pushMatrix()
        op()
        GlStateManager.popMatrix()
    }
}