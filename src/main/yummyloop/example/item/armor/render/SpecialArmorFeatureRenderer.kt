package yummyloop.example.item.armor.render

import com.mojang.blaze3d.platform.GlStateManager
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.model.Cuboid
import net.minecraft.client.render.entity.feature.ArmorBipedFeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.BipedEntityModel
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.ZombieVillagerEntity
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.item.ItemStack
import yummyloop.example.item.SpecialArmorItem
import yummyloop.example.item.armor.ArmorWithBody
import yummyloop.example.item.armor.ArmorWithLeftArm
import yummyloop.example.item.armor.ArmorWithRightArm

@Environment(EnvType.CLIENT)
class SpecialArmorFeatureRenderer<T : LivingEntity, M : BipedEntityModel<T>, A : BipedEntityModel<T>>(
        featureRendererContext_1: FeatureRendererContext<T, M>,
        model1: A,
        model2: A) :
        ArmorBipedFeatureRenderer<T, M, A>(featureRendererContext_1, model1, model2) {

    private var stack : ItemStack = ItemStack.EMPTY
    private var renderStack : ItemStack = ItemStack.EMPTY

    override fun render(var1: T, var2: Float, var3: Float, var4: Float, var5: Float, var6: Float, var7: Float, var8: Float) {
        this.renderArmor(var1, var2, var3, var4, var5, var6, var7, var8, EquipmentSlot.CHEST)
        this.renderArmor(var1, var2, var3, var4, var5, var6, var7, var8, EquipmentSlot.LEGS)
        this.renderArmor(var1, var2, var3, var4, var5, var6, var7, var8, EquipmentSlot.FEET)
        this.renderArmor(var1, var2, var3, var4, var5, var6, var7, var8, EquipmentSlot.HEAD)
    }

    private fun renderArmor(livingEntity: T, float_1: Float, float_2: Float, float_3: Float, float_4: Float, float_5: Float, float_6: Float, float_7: Float, equipmentSlot: EquipmentSlot) {
        stack = livingEntity.getEquippedStack(equipmentSlot)
        if (stack.item is SpecialArmorItem) {
            val armor = stack.item as SpecialArmorItem
            if (armor.slotType == equipmentSlot) {
                val model = this.getArmor(equipmentSlot)
                (this.model as BipedEntityModel<T>).setAttributes(model)
                model.method_17086(livingEntity, float_1, float_2, float_3)

                // Resets color
                GlStateManager.color4f(1F, 1F, 1F, 1F)

                // Render
                specialRender(model, livingEntity, float_1, float_2, float_4, float_5, float_6, float_7, equipmentSlot)
            }
        }
    }

    companion object {
        private const val rad = 57.295776F
        private var mirror = false
    }

    // Render method
    private fun specialRender(model : A, player: T, float_1: Float, float_2: Float, float_3: Float, float_4: Float, float_5: Float, scale: Float, slot: EquipmentSlot) {
        model.method_17087(player, float_1, float_2, float_3, float_4, float_5, scale)

        when (slot) {
            EquipmentSlot.HEAD -> {
                glMatrix {
                    if (player is VillagerEntity || player is ZombieVillagerEntity) {
                        GlStateManager.translatef(0.0f, -0.15f, 0.0f)
                    }
                    renderStack = stack
                    mirror = false
                    renderPart(player, model.head, scale, slot)
                }
            }
            EquipmentSlot.CHEST -> {
                if (stack.item is ArmorWithBody) {
                    renderStack = ItemStack((stack.item as ArmorWithBody).bodyItem)
                    renderStack.tag = stack.tag
                    mirror = false
                    renderPart(player, model.body, scale, slot)
                }
                if (stack.item is ArmorWithRightArm) {
                    renderStack = ItemStack((stack.item as ArmorWithRightArm).rightArmItem)
                    renderStack.tag = stack.tag
                    mirror = (stack.item as ArmorWithRightArm).mirrorRightArm
                    renderPart(player, model.rightArm, scale, slot)
                }
                if (stack.item is ArmorWithLeftArm) {
                    renderStack = ItemStack((stack.item as ArmorWithLeftArm).leftArmItem)
                    renderStack.tag = stack.tag
                    mirror = (stack.item as ArmorWithLeftArm).mirrorLeftArm
                    renderPart(player, model.leftArm, scale, slot)
                }
            }
            EquipmentSlot.LEGS -> {
                renderStack = stack
                mirror = false
                renderPart(player, model.rightLeg, scale, slot)
                mirror = true
                renderPart(player, model.leftLeg, scale, slot)
            }
            EquipmentSlot.FEET -> {
                renderStack = stack
                mirror = false
                renderPart(player, model.rightLeg, scale, slot)
                mirror = true
                renderPart(player, model.leftLeg, scale, slot)
            }
            else -> {}
        }
        //println("pitch " + a.pitch + " roll " + a.roll + " yaw " + a.yaw)
        //println("x " + a.x + " y " + a.y + " z " + a.z)
        //println("rotation points: x " + a.rotationPointX + " y " + a.rotationPointY + " z " + a.rotationPointZ)
    }

    private fun renderPart(player: T, part : Cuboid, scale: Float, slot: EquipmentSlot) {
        glMatrix {
            GlStateManager.translatef(part.x, part.y, part.z)

            if (player.isInSneakingPose) {
                if(slot == EquipmentSlot.FEET || slot == EquipmentSlot.LEGS) {
                    GlStateManager.translatef(0.0f,0.015f, 0f)
                    part.pitch = part.pitch * 0.845F
                }else{
                    GlStateManager.translatef(0.0f, 0.2f, 0.0f)
                }
            }

            if (part.pitch == 0.0f && part.yaw == 0.0f && part.roll == 0.0f) {
                if (part.rotationPointX == 0.0f && part.rotationPointY == 0.0f && part.rotationPointZ == 0.0f) {
                    renderItem(player, part, scale, slot)
                } else {
                    glMatrix {
                        GlStateManager.translatef(part.rotationPointX * scale, part.rotationPointY * scale, part.rotationPointZ * scale)
                        renderItem(player, part, scale, slot)
                    }
                }
            } else {
                glMatrix {
                    GlStateManager.translatef(part.rotationPointX * scale, part.rotationPointY * scale, part.rotationPointZ * scale)

                    if (part.roll  != 0.0f) GlStateManager.rotatef(part.roll * rad, 0.0f, 0.0f, 1.0f)
                    if (part.yaw   != 0.0f) GlStateManager.rotatef(part.yaw * rad, 0.0f, 1.0f, 0.0f)
                    if (part.pitch != 0.0f) GlStateManager.rotatef(part.pitch * rad, 1.0f, 0.0f, 0.0f)

                    renderItem(player, part, scale, slot)
                }
            }
        }
    }

    private fun renderItem(player: T, part : Cuboid, scale: Float, slot: EquipmentSlot){
        glMatrix {
            // Fix part location
            if (player.isInSneakingPose && (slot == EquipmentSlot.FEET || slot == EquipmentSlot.LEGS)) {
                GlStateManager.translatef(-part.rotationPointX * scale, -part.rotationPointY * scale, 0F)
            } else {
                GlStateManager.translatef(-part.rotationPointX * scale, -part.rotationPointY * scale, -part.rotationPointZ * scale)
            }

            // Defaults
            GlStateManager.translatef(0F, -0.25F, 0F)
            GlStateManager.scalef((if (mirror) -1 else 1 ) * -0.625f, -0.625f, 0.625f)

            MinecraftClient.getInstance().firstPersonRenderer.renderItem(player, renderStack, ModelTransformation.Type.HEAD)
        }
    }

    private fun glMatrix( operations :()-> Unit){
        GlStateManager.pushMatrix()
        operations()
        GlStateManager.popMatrix()
    }
}

