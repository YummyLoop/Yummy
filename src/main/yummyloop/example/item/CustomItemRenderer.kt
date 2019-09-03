package yummyloop.example.item

import com.mojang.blaze3d.platform.GlStateManager
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.color.item.ItemColors
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.BakedQuad
import net.minecraft.client.render.item.ItemRenderer as VanillaItemRenderer
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import java.util.*

/* Requires :
 * "parent": "builtin/entity"
 * in the model.json
 * and enableRender()
 */
interface CustomItemRenderer {
    @Environment(EnvType.CLIENT)
    fun render(stack: ItemStack, bakedModel : BakedModel)
    fun enableRender() {
        ItemRenderer.list.add(this)
    }
    @Environment(EnvType.CLIENT)
    fun renderEnchantmentGlint(runnable_1: Runnable) {
        GlStateManager.color3f(0.5019608f, 0.2509804f, 0.8f)
        bindTexture(VanillaItemRenderer.ENCHANTMENT_GLINT_TEX)
        VanillaItemRenderer.renderGlint(MinecraftClient.getInstance().textureManager, runnable_1, 1)
    }

    @Environment(EnvType.CLIENT)
    fun bindTexture(id : Identifier?){
        MinecraftClient.getInstance().textureManager.bindTexture(id)
    }

    @Environment(EnvType.CLIENT)
    fun rgb(red : Float, green: Float, blue : Float, alpha: Float){
        GlStateManager.color4f(red, green, blue,alpha)
    }
    @Environment(EnvType.CLIENT)
    fun rgb(rgb : Triple<Float, Float, Float>, alpha: Float){
        GlStateManager.color4f(rgb.first, rgb.second, rgb.third,alpha)
    }

    fun toRGB(color : Int) : Triple<Float, Float, Float>{
        val red = (color shr 16 and 255).toFloat() / 255.0f
        val green = (color shr 8 and 255).toFloat() / 255.0f
        val blue = (color and 255).toFloat() / 255.0f
        return Triple(red, green, blue)
    }

    @Environment(EnvType.CLIENT)
    fun clearCurrentColor(){
        GlStateManager.clearCurrentColor()
    }

    @Environment(EnvType.CLIENT)
    fun renderItem(stack : ItemStack, bakedModel: BakedModel){
        this.renderItem(stack, bakedModel, -1)
    }
    @Environment(EnvType.CLIENT)
    fun renderItem(stack : ItemStack, bakedModel: BakedModel, color : Int){
        GlStateManager.translatef(-0.5f, -0.5f, -0.5f)
        this.renderModel(bakedModel, color, stack)
    }

    /* ItemRender default stuff--------------------------------------------------------*/
    // Todo : Look at Mixins to add new functions

    @Environment(EnvType.CLIENT)
    private fun renderModel(bakedModel: BakedModel, color: Int, stack: ItemStack) {
        val tessellator = Tessellator.getInstance()
        val bufferBuilder = tessellator.bufferBuilder
        bufferBuilder.begin(7, VertexFormats.POSITION_COLOR_UV_NORMAL)
        val random = Random()
        val seed = 42L

        for (direction in Direction.values()) {
            random.setSeed(seed)
            this.renderQuads(bufferBuilder, bakedModel.getQuads(null as BlockState?, direction, random), color, stack)
        }

        random.setSeed(seed)
        this.renderQuads(bufferBuilder, bakedModel.getQuads(null as BlockState?, null as Direction?, random), color, stack)
        tessellator.draw()
    }

    @Environment(EnvType.CLIENT)
    private fun renderQuads(bufferBuilder: BufferBuilder, bakedQuads: List<BakedQuad>, color0: Int, stack: ItemStack) {
        val notEmpty = color0 == -1 && !stack.isEmpty

        for (bakedQuad in bakedQuads) {
            var color = color0
            if (notEmpty && bakedQuad.hasColor()) {
                color = ItemColors().getColorMultiplier(stack, bakedQuad.colorIndex)// does this work?
                color = color or -16777216
            }
            this.renderQuad(bufferBuilder, bakedQuad, color)
        }
    }

    @Environment(EnvType.CLIENT)
    private fun renderQuad(bufferBuilder: BufferBuilder, bakedQuad: BakedQuad, color: Int) {
        bufferBuilder.putVertexData(bakedQuad.vertexData)
        bufferBuilder.setQuadColor(color)
        this.postNormalQuad(bufferBuilder, bakedQuad)
    }

    @Environment(EnvType.CLIENT)
    private fun postNormalQuad(bufferBuilder_1: BufferBuilder, bakedQuad_1: BakedQuad) {
        val vec3i = bakedQuad_1.face.vector
        bufferBuilder_1.postNormal(vec3i.x.toFloat(), vec3i.y.toFloat(), vec3i.z.toFloat())
    }
}