package yummyloop.example.mixin.render.entity

import net.minecraft.client.render.entity.BipedEntityRenderer
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.model.StrayEntityModel
import net.minecraft.entity.mob.AbstractSkeletonEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import yummyloop.example.item.armor.render.SpecialArmorFeatureRenderer
import net.minecraft.client.render.entity.SkeletonEntityRenderer as VanillaSkeletonEntityRenderer

@Mixin(VanillaSkeletonEntityRenderer::class)
abstract class SkeletonEntityRenderer(entityRenderDispatcher : EntityRenderDispatcher) :
        BipedEntityRenderer<AbstractSkeletonEntity, StrayEntityModel<AbstractSkeletonEntity>>
        (entityRenderDispatcher, StrayEntityModel(), 0.5f) {

    @Inject(at = [At("RETURN")], method = ["<init>"], cancellable = true)
    private fun onSkeletonEntityRenderer(entityRenderDispatcher : EntityRenderDispatcher, info: CallbackInfo) {
        // Special armor feature renderer
        this.addFeature(SpecialArmorFeatureRenderer(this, StrayEntityModel(0.5F, true), StrayEntityModel(1.0f, true)))
    }
}
