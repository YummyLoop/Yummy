package yummyloop.yummy.old.mixin.client.render.entity;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yummyloop.yummy.old.item.armor.render.SpecialArmorFeatureRenderer;

@Mixin({net.minecraft.client.render.entity.PlayerEntityRenderer.class})
public abstract class PlayerEntityRenderer extends LivingEntityRenderer {
    @Inject(
            at = {@At("RETURN")},
            method = "<init>(Lnet/minecraft/client/render/entity/EntityRenderDispatcher;Z)V",
            cancellable = true
    )
    private void onPlayerEntityRenderer(EntityRenderDispatcher entityRenderDispatcher, boolean var2, CallbackInfo info) {
        this.addFeature(new SpecialArmorFeatureRenderer(this, new BipedEntityModel(0.5F), new BipedEntityModel(1.0F)));
    }

    public PlayerEntityRenderer(EntityRenderDispatcher entityRenderDispatcher, boolean var2) {
        super(entityRenderDispatcher, new PlayerEntityModel(0.0F, var2), 0.5F);
    }
}
