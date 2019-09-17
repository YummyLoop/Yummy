package yummyloop.example.mixin.client.render.entity;

import kotlin.jvm.internal.Intrinsics;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.StrayEntityModel;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yummyloop.example.item.armor.render.SpecialArmorFeatureRenderer;

@Mixin({net.minecraft.client.render.entity.SkeletonEntityRenderer.class})
public abstract class SkeletonEntityRenderer extends BipedEntityRenderer {
    @Inject(
            at = {@At("RETURN")},
            method = "<init>",
            cancellable = true
    )
    private void onSkeletonEntityRenderer(EntityRenderDispatcher entityRenderDispatcher, CallbackInfo info) {
        this.addFeature((FeatureRenderer)(new SpecialArmorFeatureRenderer((FeatureRendererContext)this, (BipedEntityModel)(new StrayEntityModel(0.5F, true)), (BipedEntityModel)(new StrayEntityModel(1.0F, true)))));
    }

    public SkeletonEntityRenderer(@NotNull EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, (BipedEntityModel)(new StrayEntityModel()), 0.5F);
        Intrinsics.checkParameterIsNotNull(entityRenderDispatcher, "entityRenderDispatcher");
    }
}
