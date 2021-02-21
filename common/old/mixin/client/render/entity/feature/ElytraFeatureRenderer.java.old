package yummyloop.yummy.old.mixin.client.render.entity.feature;

import com.mojang.blaze3d.platform.GlStateManager;
import java.awt.Color;
import kotlin.TypeCastException;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({net.minecraft.client.render.entity.feature.ElytraFeatureRenderer.class})
public final class ElytraFeatureRenderer {
    @Inject(
            at = {@At(
                    target = "Lcom/mojang/blaze3d/platform/GlStateManager;enableBlend()V",
                    ordinal = 0,
                    value = "INVOKE"
            )},
            method = "method_17161(Lnet/minecraft/entity/LivingEntity;FFFFFFF)V",
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void onElytraRender(LivingEntity livingEntity_1, float float_1, float float_2, float float_3, float float_4, float float_5, float float_6, float float_7, CallbackInfo info, ItemStack itemStack_1) {
        Item var10000 = itemStack_1.getItem();
        if (var10000 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.DyeableItem");
        } else {
            int icolor = ((DyeableItem)var10000).getColor(itemStack_1);
            if (icolor != 10511680) {
                Color color = new Color(icolor);
                GlStateManager.color4f((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, 1.0F);
            }
        }
    }
}