package yummyloop.example.mixin.client.render.entity

import net.minecraft.client.render.entity.BipedEntityRenderer
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.model.ZombieEntityModel
import net.minecraft.entity.mob.ZombiePigmanEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import yummyloop.example.item.armor.render.SpecialArmorFeatureRenderer
import net.minecraft.client.render.entity.ZombiePigmanEntityRenderer as VanillaZombiePigmanEntityRenderer

@Mixin(VanillaZombiePigmanEntityRenderer::class)
abstract class ZombiePigmanEntityRenderer(entityRenderDispatcher : EntityRenderDispatcher) :
        BipedEntityRenderer<ZombiePigmanEntity, ZombieEntityModel<ZombiePigmanEntity>>
        (entityRenderDispatcher, ZombieEntityModel(), 0.5f) {

    @Inject(at = [At("RETURN")], method = ["<init>(Lnet/minecraft/client/render/entity/EntityRenderDispatcher;)V"], cancellable = true)
    private fun onZombiePigmanEntityRenderer(entityRenderDispatcher : EntityRenderDispatcher, info: CallbackInfo) {
        // Special armor feature renderer
        this.addFeature(SpecialArmorFeatureRenderer(this, ZombieEntityModel(0.5F, true), ZombieEntityModel(1.0f, true)))
    }
}
