package yummyloop.yummy.old.mixin.client.render.entity.feature;

import kotlin.jvm.internal.Intrinsics;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yummyloop.yummy.old.item.armor.SpecialArmor;

@Mixin({net.minecraft.client.render.entity.feature.ArmorFeatureRenderer.class})
public abstract class ArmorFeatureRenderer extends FeatureRenderer {
    @Inject(
            at = {@At("HEAD")},
            method = "renderArmor",
            cancellable = true
    )
    private void onRenderArmor(LivingEntity livingEntity_1, float float_1, float float_2, float float_3, float float_4, float float_5, float float_6, float float_7, EquipmentSlot slot, CallbackInfo info) {
        ItemStack stack = livingEntity_1.getEquippedStack(slot);
        Intrinsics.checkExpressionValueIsNotNull(stack, "stack");
        if (stack.getItem() instanceof SpecialArmor) {
            info.cancel();
        }
    }

    public ArmorFeatureRenderer(@NotNull FeatureRendererContext featureRendererContext, @NotNull BipedEntityModel bipedEntityModel1, @NotNull BipedEntityModel bipedEntityModel2) {
        super(featureRendererContext);
        Intrinsics.checkParameterIsNotNull(featureRendererContext, "featureRendererContext");
        Intrinsics.checkParameterIsNotNull(bipedEntityModel1, "bipedEntityModel1");
        Intrinsics.checkParameterIsNotNull(bipedEntityModel2, "bipedEntityModel2");
    }
}