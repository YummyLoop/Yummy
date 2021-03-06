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

@Mixin({net.minecraft.client.render.entity.ZombieBaseEntityRenderer.class})
public abstract class ZombieBaseEntityRenderer extends BipedEntityRenderer {
    @Inject(
            at = {@At("RETURN")},
            method = "<init>",
            cancellable = true
    )
    private void onZombieBaseEntityRenderer(EntityRenderDispatcher entityRenderDispatcher, ZombieEntityModel zombieEntityModel_1, ZombieEntityModel zombieEntityModel_2, ZombieEntityModel zombieEntityModel_3, CallbackInfo info) {
        this.addFeature((FeatureRenderer)(new SpecialArmorFeatureRenderer((FeatureRendererContext)this, (BipedEntityModel)zombieEntityModel_2, (BipedEntityModel)zombieEntityModel_3)));
    }

    public ZombieBaseEntityRenderer(@NotNull EntityRenderDispatcher entityRenderDispatcher, @NotNull ZombieEntityModel zombieEntityModel_1, @NotNull ZombieEntityModel zombieEntityModel_2, @NotNull ZombieEntityModel zombieEntityModel_3) {
        super(entityRenderDispatcher, (BipedEntityModel)zombieEntityModel_1, 0.5F);
        Intrinsics.checkParameterIsNotNull(entityRenderDispatcher, "entityRenderDispatcher");
        Intrinsics.checkParameterIsNotNull(zombieEntityModel_1, "zombieEntityModel_1");
        Intrinsics.checkParameterIsNotNull(zombieEntityModel_2, "zombieEntityModel_2");
        Intrinsics.checkParameterIsNotNull(zombieEntityModel_3, "zombieEntityModel_3");
    }
}
