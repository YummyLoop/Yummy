package yummyloop.yummy.old.mixin.client.render.entity;

import kotlin.jvm.internal.Intrinsics;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.ZombieEntityModel;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yummyloop.yummy.old.item.armor.render.SpecialArmorFeatureRenderer;

@Mixin({net.minecraft.client.render.entity.ZombiePigmanEntityRenderer.class})
public abstract class ZombiePigmanEntityRenderer extends BipedEntityRenderer {
    @Inject(
            at = {@At("RETURN")},
            method = "<init>(Lnet/minecraft/client/render/entity/EntityRenderDispatcher;)V",
            cancellable = true
    )
    private final void onZombiePigmanEntityRenderer(EntityRenderDispatcher entityRenderDispatcher, CallbackInfo info) {
        this.addFeature((FeatureRenderer)(new SpecialArmorFeatureRenderer((FeatureRendererContext)this, (BipedEntityModel)(new ZombieEntityModel(0.5F, true)), (BipedEntityModel)(new ZombieEntityModel(1.0F, true)))));
    }

    public ZombiePigmanEntityRenderer(@NotNull EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, (BipedEntityModel)(new ZombieEntityModel()), 0.5F);
        Intrinsics.checkParameterIsNotNull(entityRenderDispatcher, "entityRenderDispatcher");
    }
}
