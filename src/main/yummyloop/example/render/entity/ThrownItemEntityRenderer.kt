package yummyloop.example.render.entity

import com.mojang.blaze3d.platform.GlStateManager
import net.fabricmc.fabric.api.client.render.EntityRendererRegistry
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.entity.Entity
import net.minecraft.entity.FlyingItemEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import yummyloop.example.item.entity.AbstractProjectileEntity
import net.minecraft.item.Item as VanillaItem

class ThrownItemEntityRenderer(e1: EntityRenderDispatcher, private val context: EntityRendererRegistry.Context, item : VanillaItem) : EntityRenderer<Entity>(e1) {
    val stack = ItemStack(item)

    override fun getTexture(entity: Entity?): Identifier? {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEX// Is ignored
    }

    override fun render(entity: Entity, x: Double, y: Double, z: Double, float_1: Float, float_2: Float) {
        glMatrix {
            GlStateManager.enableRescaleNormal()

            GlStateManager.translatef(x.toFloat(), y.toFloat(), z.toFloat())
            GlStateManager.rotatef(MathHelper.lerp(float_2, entity.prevYaw, entity.yaw) - 90.0f, 0.0f, 1.0f, 0.0f)
            GlStateManager.rotatef(MathHelper.lerp(float_2, entity.prevPitch, entity.pitch) + 90.0f, 0.0f, 0.0f, 1.0f)

            glMatrix {
                //GlStateManager.translatef(0F, 0.4F,0F)
                //GlStateManager.rotatef(180F,0F,1F, 0F)

                if (this.renderOutlines) {
                    GlStateManager.enableColorMaterial()
                    GlStateManager.setupSolidRenderingTextureCombine(this.getOutlineColor(entity))
                }
                GlStateManager.disableLighting()
                if (entity is ProjectileEntity) {
                    val shake = entity.shake - float_2
                    if (shake > 0.0f) {
                        val shakeRoll = -MathHelper.sin(shake * 2.5f) * (shake)
                        GlStateManager.rotatef(shakeRoll, 0.0f, 0.0f, 1.0f)
                    }
                } else if (entity is AbstractProjectileEntity) {
                    val shake = entity.shake - float_2
                    if (shake > 0.0f) {
                        val shakeRoll = -MathHelper.sin(shake * 2.5f) * (shake)
                        GlStateManager.rotatef(shakeRoll, 0.0f, 0.0f, 1.0f)
                    }
                }
                if (entity is FlyingItemEntity){
                    context.itemRenderer.renderItem((entity as FlyingItemEntity).stack, ModelTransformation.Type.HEAD)
                }else{
                    context.itemRenderer.renderItem(this.stack, ModelTransformation.Type.HEAD)
                }
                GlStateManager.enableLighting()
                if (this.renderOutlines) {
                    GlStateManager.tearDownSolidRenderingTextureCombine()
                    GlStateManager.disableColorMaterial()
                }
            }
            GlStateManager.disableRescaleNormal()
        }
        super.render(entity, x, y, z, float_1, float_2)
    }

    private fun glMatrix(op : () -> Unit){
        GlStateManager.pushMatrix()
        op()
        GlStateManager.popMatrix()
    }
}