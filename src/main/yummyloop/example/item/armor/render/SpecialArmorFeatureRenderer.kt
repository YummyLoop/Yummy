package yummyloop.example.item.armor.render

import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.render.entity.feature.ArmorBipedFeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.BipedEntityModel
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import yummyloop.example.item.SpecialArmorItem

class SpecialArmorFeatureRenderer<T : LivingEntity, M : BipedEntityModel<T>>(
        featureRendererContext_1: FeatureRendererContext<T, M>,
        model1: M,
        model2: M) :
        ArmorBipedFeatureRenderer<T, M, M>(featureRendererContext_1, model1, model2) {

    @Suppress("UNCHECKED_CAST")
    constructor(featureRendererContext_1 : FeatureRendererContext<T, M>) :
            this (featureRendererContext_1,
                    SpecialBipedEntityModel<T>(0.5F) as M,
                    SpecialBipedEntityModel<T>(1F) as M
            )

    override fun render(var1: T, var2: Float, var3: Float, var4: Float, var5: Float, var6: Float, var7: Float, var8: Float) {
        this.renderArmor(var1, var2, var3, var4, var5, var6, var7, var8, EquipmentSlot.CHEST)
        this.renderArmor(var1, var2, var3, var4, var5, var6, var7, var8, EquipmentSlot.LEGS)
        this.renderArmor(var1, var2, var3, var4, var5, var6, var7, var8, EquipmentSlot.FEET)
        this.renderArmor(var1, var2, var3, var4, var5, var6, var7, var8, EquipmentSlot.HEAD)
    }

    private fun renderArmor(livingEntity: T, float_1: Float, float_2: Float, float_3: Float, float_4: Float, float_5: Float, float_6: Float, float_7: Float, equipmentSlot: EquipmentSlot) {
        val stack = livingEntity.getEquippedStack(equipmentSlot)
        if (stack.item is SpecialArmorItem) {
            val armor = stack.item as SpecialArmorItem
            if (armor.slotType == equipmentSlot) {
                val model = this.getArmor(equipmentSlot)
                (this.model as BipedEntityModel<T>).setAttributes(model)
                model.method_17086(livingEntity, float_1, float_2, float_3)

                // Resets color
                GlStateManager.color4f(1F, 1F, 1F, 1F)

                // Render
                if (model is SpecialBipedEntityModel<*>) {
                    @Suppress("UNCHECKED_CAST")
                    (model as SpecialBipedEntityModel<T>).specialRender(livingEntity, float_1, float_2, float_4, float_5, float_6, float_7, equipmentSlot)
                }
            }
        }
    }

}

