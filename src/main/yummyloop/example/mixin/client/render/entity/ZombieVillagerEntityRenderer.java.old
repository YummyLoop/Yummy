package yummyloop.example.mixin.client.render.entity;

import kotlin.jvm.internal.Intrinsics;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.ZombieVillagerEntityModel;
import net.minecraft.resource.ReloadableResourceManager;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yummyloop.example.item.armor.render.SpecialArmorFeatureRenderer;

@Mixin({net.minecraft.client.render.entity.ZombieVillagerEntityRenderer.class})
public abstract class ZombieVillagerEntityRenderer extends BipedEntityRenderer {
    @Inject(
            at = {@At("RETURN")},
            method = "<init>",
            cancellable = true
    )
    private void onZombieVillagerEntityRenderer(EntityRenderDispatcher entityRenderDispatcher, ReloadableResourceManager reloadableResourceManager_1, CallbackInfo info) {
        this.addFeature((new SpecialArmorFeatureRenderer((FeatureRendererContext)this, (BipedEntityModel)(new ZombieVillagerEntityModel(0.5F, true)), (new ZombieVillagerEntityModel(1.0F, true)))));
    }

    public ZombieVillagerEntityRenderer(@NotNull EntityRenderDispatcher entityRenderDispatcher, @NotNull ReloadableResourceManager reloadableResourceManager_1) {
        super(entityRenderDispatcher, (new ZombieVillagerEntityModel<>()), 0.5F);
        Intrinsics.checkParameterIsNotNull(entityRenderDispatcher, "entityRenderDispatcher");
        Intrinsics.checkParameterIsNotNull(reloadableResourceManager_1, "reloadableResourceManager_1");
    }
}
