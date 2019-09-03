package yummyloop.example.item

import com.mojang.blaze3d.platform.GlStateManager
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

interface CustomItemDynamicRenderer {
    @Environment(EnvType.CLIENT)
    fun render(stack: ItemStack)
    fun enableRender() {
        ItemDynamicRenderer.list.add(this)
    }
    @Environment(EnvType.CLIENT)
    fun renderEnchantmentGlint(runnable_1: Runnable) {
        GlStateManager.color3f(0.5019608f, 0.2509804f, 0.8f)
        bindTexture(ItemRenderer.ENCHANTMENT_GLINT_TEX)
        ItemRenderer.renderGlint(MinecraftClient.getInstance().textureManager, runnable_1, 1)
    }

    @Environment(EnvType.CLIENT)
    fun bindTexture(id : Identifier?){
        MinecraftClient.getInstance().textureManager.bindTexture(id)
    }
}