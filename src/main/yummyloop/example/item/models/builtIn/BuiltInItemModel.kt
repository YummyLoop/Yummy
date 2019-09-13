package yummyloop.example.item.models.builtIn

import com.mojang.blaze3d.platform.GlStateManager
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.model.BakedModel
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.client.render.item.ItemRenderer as VanillaItemRenderer

interface BuiltInItemModel {
    @Environment(EnvType.CLIENT)
    fun render(stack: ItemStack, bakedModel : BakedModel)

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
}

/*
@Environment(EnvType.CLIENT)
override fun render(stack: ItemStack, bakedModel : BakedModel){
    //val banner = BannerBlockEntity()
    val model = ShieldEntityModel()
    //val model = Model1()
    //if (stack.getSubTag("BlockEntityTag") != null) {// Banner ... not done yet
    //    banner.deserialize(stack, ShieldItem.getColor(stack))
    //    bindTexture(TextureCache.SHIELD.get(banner.patternCacheKey, banner.patterns, banner.patternColors))
    //}
    GlStateManager.pushMatrix()

    //GlStateManager.scalef(1.0f, -1.0f, -1.0f)
    model.renderItem()
    /*if (stack.hasEnchantmentGlint()) {
        //renderGlint(this.textureManager, { this.renderModelWithTint(bakedModel, -8372020) }, 8)
        //renderEnchantmentGlint(Runnable { model.renderItem() })
    }*/

    GlStateManager.popMatrix()
}
*/