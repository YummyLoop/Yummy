package yummyloop.example.mixin.armor.render

import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.BipedEntityModel
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import yummyloop.example.item.armor.SpecialArmor
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer as VanillaArmorFeatureRenderer

@Mixin(VanillaArmorFeatureRenderer::class)
abstract class ArmorFeatureRenderer<T : LivingEntity, M : BipedEntityModel<T>, A : BipedEntityModel<T>>(
        featureRendererContext : FeatureRendererContext<T, M> ,
        bipedEntityModel1 : A,
        bipedEntityModel2 : A
) : FeatureRenderer<T, M>(featureRendererContext) {

    @Inject(at = [At("HEAD")], method = ["renderArmor"], cancellable = true)
    private fun onRenderArmor(livingEntity_1: T, float_1: Float, float_2: Float, float_3: Float, float_4: Float, float_5: Float, float_6: Float, float_7: Float, slot: EquipmentSlot, info: CallbackInfo) {
        val stack = livingEntity_1.getEquippedStack(slot)
        if (stack.item is SpecialArmor) {
            info.cancel()
        }
    }
}