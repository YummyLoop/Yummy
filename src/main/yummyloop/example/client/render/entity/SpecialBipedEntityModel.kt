package yummyloop.example.client.render.entity

import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.MinecraftClient
import net.minecraft.client.model.Cuboid
import net.minecraft.client.render.entity.model.BipedEntityModel
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity

class SpecialBipedEntityModel<T : LivingEntity>(scale : Float, private val yRotationOffset : Float, textureWidth : Int, textureHeight : Int) : BipedEntityModel<T>(){
    constructor(): this(0.0f)
    constructor(scale: Float): this(scale, 0.0f, 64, 32)

    companion object {
        private const val rad = 57.295776F
        private var mirror = false
    }

    // Render method
    fun specialRender(player: T, float_1: Float, float_2: Float, float_3: Float, float_4: Float, float_5: Float, scale: Float, slot: EquipmentSlot) {
        this.method_17087(player, float_1, float_2, float_3, float_4, float_5, scale)

        when (slot) {
            EquipmentSlot.HEAD -> {
                renderPart(player, head, scale, slot)
            }
            EquipmentSlot.CHEST -> {
                //renderPart(player, body, scale, slot)
                renderPart(player, rightArm, scale, slot)

            }
            EquipmentSlot.LEGS -> {
                renderPart(player, rightLeg, scale, slot)
                mirror=true
                renderPart(player, leftLeg, scale, slot)
                mirror=false

            }
            EquipmentSlot.FEET -> {
                renderPart(player, rightLeg, scale, slot)
                mirror=true
                renderPart(player, leftLeg, scale, slot)
                mirror=false

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

                    if (part.roll  != 0.0f) GlStateManager.rotatef(part.roll * rad  , 0.0f, 0.0f, 1.0f)
                    if (part.yaw   != 0.0f) GlStateManager.rotatef(part.yaw * rad  , 0.0f, 1.0f, 0.0f)
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

            MinecraftClient.getInstance().firstPersonRenderer.renderItem(player, player.getEquippedStack(slot), ModelTransformation.Type.HEAD)
        }
    }

    private fun glMatrix( operations :()-> Unit){
        GlStateManager.pushMatrix()
        operations()
        GlStateManager.popMatrix()
    }
}