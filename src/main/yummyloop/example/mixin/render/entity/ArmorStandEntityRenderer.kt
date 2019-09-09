package yummyloop.example.mixin.render.entity

import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.model.ArmorStandArmorEntityModel
import net.minecraft.client.render.entity.model.ArmorStandEntityModel
import net.minecraft.entity.decoration.ArmorStandEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import yummyloop.example.item.armor.render.SpecialArmorFeatureRenderer
import net.minecraft.client.render.entity.ArmorStandEntityRenderer as VanillaArmorStandEntityRenderer

@Mixin(VanillaArmorStandEntityRenderer::class)
abstract class ArmorStandEntityRenderer(entityRenderDispatcher : EntityRenderDispatcher) :
        LivingEntityRenderer<ArmorStandEntity, ArmorStandArmorEntityModel>(
                entityRenderDispatcher, ArmorStandEntityModel(), 0.0f) {

    @Inject(at = [At("RETURN")], method = ["<init>(Lnet/minecraft/client/render/entity/EntityRenderDispatcher;)V"], cancellable = true)
    private fun onArmorStandEntityRenderer(entityRenderDispatcher : EntityRenderDispatcher, info: CallbackInfo) {
        // Special armor feature renderer
        this.addFeature(SpecialArmorFeatureRenderer(this, ArmorStandArmorEntityModel(0.5F), ArmorStandArmorEntityModel(1.0f)))
    }
}
