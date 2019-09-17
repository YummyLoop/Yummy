package yummyloop.example.mixin.client.render.entity.feature

import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer as VanillaElytraFeatureRenderer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.entity.LivingEntity
import net.minecraft.item.DyeableItem
import net.minecraft.item.ItemStack
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.LocalCapture
import java.awt.Color


@Mixin(VanillaElytraFeatureRenderer::class)
class ElytraFeatureRenderer<T : LivingEntity, M : EntityModel<T>> {
    @Inject(
            at = [
                At("INVOKE",
                        target = "enableBlend()V",
                        ordinal = 0
                )
            ],
            method = ["Lnet/minecraft/client/render/entity/feature/ElytraFeatureRenderer;method_17161(Lnet/minecraft/entity/LivingEntity;FFFFFFF)V"],
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private fun onElytraRender(livingEntity_1: T, float_1: Float, float_2: Float, float_3: Float, float_4: Float, float_5: Float, float_6: Float, float_7: Float, info: CallbackInfo, itemStack_1 : ItemStack) {
        val icolor =(itemStack_1.item as DyeableItem).getColor(itemStack_1)
        if (icolor != 10511680) {
            val color = Color(icolor)
            //color = color.brighter()
            GlStateManager.color4f(color.red / 255.0f, color.green / 255.0f, color.blue / 255.0f, 1f)
            //println(" color:" + color.red.toFloat() + " " + color.green.toFloat() + " " + color.blue.toFloat())
        }
    }
}