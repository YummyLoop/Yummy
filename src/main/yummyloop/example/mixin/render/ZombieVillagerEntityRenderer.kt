package yummyloop.example.mixin.render

import net.minecraft.client.render.entity.BipedEntityRenderer
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.model.ZombieVillagerEntityModel
import net.minecraft.entity.mob.ZombieVillagerEntity
import net.minecraft.resource.ReloadableResourceManager
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import yummyloop.example.item.armor.render.SpecialArmorFeatureRenderer
import net.minecraft.client.render.entity.ZombieVillagerEntityRenderer as VanillaZombieVillagerEntityRenderer

@Mixin(VanillaZombieVillagerEntityRenderer::class)
abstract class ZombieVillagerEntityRenderer(entityRenderDispatcher : EntityRenderDispatcher, reloadableResourceManager_1 : ReloadableResourceManager) :
        BipedEntityRenderer<ZombieVillagerEntity, ZombieVillagerEntityModel<ZombieVillagerEntity>>
        (entityRenderDispatcher, ZombieVillagerEntityModel(), 0.5f) {

    @Inject(at = [At("RETURN")], method = ["<init>"], cancellable = true)
    private fun onZombieVillagerEntityRenderer(entityRenderDispatcher : EntityRenderDispatcher, reloadableResourceManager_1 : ReloadableResourceManager, info: CallbackInfo) {
        // Special armor feature renderer
        this.addFeature(SpecialArmorFeatureRenderer(this, ZombieVillagerEntityModel(0.5F, true), ZombieVillagerEntityModel(1.0f, true)))
    }
}
