package yummyloop.yummy.old.mixin.client.render.entity;

import kotlin.jvm.internal.Intrinsics;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.ArmorStandArmorEntityModel;
import net.minecraft.client.render.entity.model.ArmorStandEntityModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yummyloop.yummy.old.item.armor.render.SpecialArmorFeatureRenderer;

@Mixin({net.minecraft.client.render.entity.ArmorStandEntityRenderer.class})
public abstract class ArmorStandEntityRenderer extends LivingEntityRenderer {
    @Inject(
            at = {@At("RETURN")},
            method = "<init>(Lnet/minecraft/client/render/entity/EntityRenderDispatcher;)V",
            cancellable = true
    )
    private void onArmorStandEntityRenderer(EntityRenderDispatcher entityRenderDispatcher, CallbackInfo info) {
        this.addFeature((FeatureRenderer)(new SpecialArmorFeatureRenderer((FeatureRendererContext)this, (BipedEntityModel)(new ArmorStandArmorEntityModel(0.5F)), (BipedEntityModel)(new ArmorStandArmorEntityModel(1.0F)))));
    }

    public ArmorStandEntityRenderer(@NotNull EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, (EntityModel)(new ArmorStandEntityModel()), 0.0F);
        Intrinsics.checkParameterIsNotNull(entityRenderDispatcher, "entityRenderDispatcher");
    }
}
