package yummyloop.example.mixin.render

import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.model.PlayerEntityModel
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import yummyloop.example.item.armor.render.SpecialArmorFeatureRenderer
import yummyloop.example.item.armor.render.SpecialBipedEntityModel
import net.minecraft.client.render.entity.PlayerEntityRenderer as VanillaPlayerEntityRenderer

@Mixin(VanillaPlayerEntityRenderer::class)
abstract class PlayerEntityRenderer( entityRenderDispatcher : EntityRenderDispatcher, boolean : Boolean) :
        LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>(entityRenderDispatcher, PlayerEntityModel(0.0f, boolean), 0.5f) {

    @Inject(at = [At("RETURN")], method = ["<init>(Lnet/minecraft/client/render/entity/EntityRenderDispatcher;Z)V"], cancellable = true)
    private fun onPlayerEntityRenderer(entityRenderDispatcher : EntityRenderDispatcher, boolean : Boolean, info: CallbackInfo) {
        // Special armor feature renderer
        this.addFeature(SpecialArmorFeatureRenderer(this, SpecialBipedEntityModel(0.5f), SpecialBipedEntityModel(1.0f)))
    }
}
