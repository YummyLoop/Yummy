package yummyloop.example.mixin.render.entity

import net.minecraft.client.render.entity.BipedEntityRenderer
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.model.ZombieEntityModel
import net.minecraft.entity.mob.ZombieEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import yummyloop.example.item.armor.render.SpecialArmorFeatureRenderer
import net.minecraft.client.render.entity.ZombieBaseEntityRenderer as VanillaZombieBaseEntityRenderer

@Mixin(VanillaZombieBaseEntityRenderer::class)
abstract class ZombieBaseEntityRenderer<T : ZombieEntity, M : ZombieEntityModel<T>>
constructor(entityRenderDispatcher : EntityRenderDispatcher, zombieEntityModel_1 : M, zombieEntityModel_2 : M, zombieEntityModel_3 : M) :
        BipedEntityRenderer<T, M> (entityRenderDispatcher, zombieEntityModel_1,0.5f) {

    @Inject(at = [At("RETURN")], method = ["<init>"], cancellable = true)
    private fun onZombieBaseEntityRenderer(entityRenderDispatcher : EntityRenderDispatcher, zombieEntityModel_1 : M, zombieEntityModel_2 : M, zombieEntityModel_3 : M, info: CallbackInfo) {
        // Special armor feature renderer
        this.addFeature(SpecialArmorFeatureRenderer(this, zombieEntityModel_2, zombieEntityModel_3))
    }
}
