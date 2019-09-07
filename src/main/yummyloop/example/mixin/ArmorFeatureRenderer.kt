package yummyloop.example.mixin

import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.BipedEntityModel
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ArmorItem
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer as VanillaArmorFeatureRenderer
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(VanillaArmorFeatureRenderer::class)
abstract class ArmorFeatureRenderer<T : LivingEntity, M : BipedEntityModel<T>, A : BipedEntityModel<T>>(
        featureRendererContext : FeatureRendererContext<T, M> ,
        bipedEntityModel1 : A,
        bipedEntityModel2 : A
) : FeatureRenderer<T, M>(featureRendererContext) {

    @Inject(at = [At("HEAD")], method = ["renderArmor"], cancellable = true)
    private fun onRenderArmor(livingEntity_1: T, float_1: Float, float_2: Float, float_3: Float, float_4: Float, float_5: Float, float_6: Float, float_7: Float, slot: EquipmentSlot, info: CallbackInfo) {
        val stack = livingEntity_1.getEquippedStack(slot)
        if (stack.item is ArmorItem) {
            val armor = stack.item as ArmorItem
            if (armor.slotType == slot) {
                val model = this.getArmor(slot)
                (this.model as BipedEntityModel<T>).setAttributes(model)
                model.method_17086(livingEntity_1, float_1, float_2, float_3)
                this.method_4170(model, slot)

                GlStateManager.color4f(1F, 1F, 1F, 1F)

                // Render
                model.method_17088(livingEntity_1, float_1, float_2, float_4, float_5, float_6, float_7)

            }
            info.cancel()
        }
    }

    @Shadow
    abstract fun getArmor(equipmentSlot_1: EquipmentSlot): A

    @Shadow
    protected abstract fun method_4170(var1: A, var2: EquipmentSlot)

}