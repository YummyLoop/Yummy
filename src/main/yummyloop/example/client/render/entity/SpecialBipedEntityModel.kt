package yummyloop.example.client.render.entity

import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.MinecraftClient
import net.minecraft.client.model.Cuboid
import net.minecraft.client.render.entity.model.BipedEntityModel
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.render.model.json.Transformation
import net.minecraft.client.util.math.Vector3f
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import kotlin.math.abs

class SpecialBipedEntityModel<T : LivingEntity>(scale : Float, private val yRotationOffset : Float, textureWidth : Int, textureHeight : Int) : BipedEntityModel<T>(){
    constructor(): this(0.0f)
    constructor(scale: Float): this(scale, 0.0f, 64, 32)

    companion object {
        private const val rad = 57.295776F
    }

    // Render method
    fun specialRender(player: T, float_1: Float, float_2: Float, float_3: Float, float_4: Float, float_5: Float, float_6: Float, slot: EquipmentSlot) {
        this.method_17087(player, float_1, float_2, float_3, float_4, float_5, float_6)

        when (slot) {
            EquipmentSlot.HEAD -> {
                renderHead(player,slot)
            }
            EquipmentSlot.CHEST -> {
                //renderChest(player, slot)
                GlStateManager.pushMatrix()
                //GlStateManager.rotatef(90F, 1f, 0f, 0f)
                //GlStateManager.translatef(0.0F, 0.25F, -0.1F)

                renderA(float_6, player, slot)
                GlStateManager.popMatrix()

            }
            EquipmentSlot.LEGS -> {
                renderRightLeg(player, slot,true)

            }
            EquipmentSlot.FEET -> {
                renderRightLeg(player, slot,true)

            }
            else -> {}
        }

        //println("pitch " + a.pitch + " roll " + a.roll + " yaw " + a.yaw)
        //println("x " + a.x + " y " + a.y + " z " + a.z)
        //println("rotation points: x " + a.rotationPointX + " y " + a.rotationPointY + " z " + a.rotationPointZ)
    }

    private fun renderRightLeg(player: T, slot: EquipmentSlot, mirror: Boolean){
        renderLeg(player, slot, rightLeg, false)
        if (mirror) {
            renderLeg(player, slot, leftLeg, true)
        }
    }

    private fun renderLeg(player: T, slot: EquipmentSlot, part : Cuboid, isMirror: Boolean){
        val mirror = if (isMirror) 1 else -1

        GlStateManager.pushMatrix()

        if (player.isInSneakingPose) {
            GlStateManager.translatef(0.0f, 0.0f, 0.25f)
        }

        //Reference point / left / down / ?
        GlStateManager.translatef(0F, 0.75F, 0F)
        GlStateManager.rotatef(part.pitch* rad, 1f, 0f, 0f)
        GlStateManager.rotatef(part.yaw* rad, 0f, 1f, 0f)
        GlStateManager.rotatef(part.roll* rad, 0f, 0f, 1f)
        GlStateManager.scalef(0.625f * mirror, -0.625f, 0.625f) // Default head scale

        // Transformation - negate the effects of the head reference y
        val transformation = Transformation(
                Vector3f(0F,0F,0F), //rotation
                Vector3f(0F,1.6F,0F), //translation / ?, up, ?
                Vector3f(1F,1F,1F) //scale
        )
        ModelTransformation.applyGl(transformation,false)

        MinecraftClient.getInstance().firstPersonRenderer.renderItem(player, player.getEquippedStack(slot), ModelTransformation.Type.HEAD)

        GlStateManager.popMatrix()
    }

    private fun renderHead(player: T, slot: EquipmentSlot){
        val part = head

        GlStateManager.pushMatrix()

        if (player.isInSneakingPose) {
            GlStateManager.translatef(0.0F, 0.25F, 0.0F)
        }

        GlStateManager.translatef(0F, -0.3F, 0F)
        GlStateManager.scalef(-0.625f, -0.625f, 0.625f)

        ModelTransformation.applyGl(Transformation(
                /*rotation     */Vector3f(0F, -part.yaw * rad,0F),
                /*translation  */Vector3f(0F,0F,0F),
                /*scale        */Vector3f(1F,1F,1F)
        ),false)

        ModelTransformation.applyGl(Transformation(
                /*rotation     */Vector3f(-part.pitch * rad, 0F,0F),
                /*translation  */Vector3f(0F, abs(part.pitch) * -0.3F, -part.pitch * 0.3F),
                /*scale        */Vector3f(1F,1F,1F)
        ),false)

        MinecraftClient.getInstance().firstPersonRenderer.renderItem(player, player.getEquippedStack(slot), ModelTransformation.Type.HEAD)

        GlStateManager.popMatrix()
    }

    private fun renderChest(player: T, slot: EquipmentSlot){
        val part = body

        GlStateManager.pushMatrix()

        if (player.isInSneakingPose) {
            GlStateManager.translatef(0.0F, 0.25F, -0.1F)
        }

        GlStateManager.translatef(0F, -0.25F, 0F)
        GlStateManager.scalef(-0.625f, -0.625f, 0.625f)

        ModelTransformation.applyGl(Transformation(
                /*rotation     */Vector3f(0F, -part.yaw * rad,0F),
                /*translation  */Vector3f(0F,0F,0F),
                /*scale        */Vector3f(1F,1F,1F)
        ),false)

        ModelTransformation.applyGl(Transformation(
                /*rotation     */Vector3f(-part.pitch * rad, 0F,0F),
                /*translation  */Vector3f(0F, 0F, 0F),
                /*scale        */Vector3f(1F,1F,1F)
        ),false)

        ModelTransformation.applyGl(Transformation(
                /*rotation     */Vector3f(0F, 0F,-part.roll * rad),
                /*translation  */Vector3f(0F, 0F, 0F),
                /*scale        */Vector3f(1F,1F,1F)
        ),false)

        MinecraftClient.getInstance().firstPersonRenderer.renderItem(player, player.getEquippedStack(slot), ModelTransformation.Type.HEAD)

        GlStateManager.popMatrix()
    }

    private fun renderArm(player: T, slot: EquipmentSlot){
        val part = rightArm
        val isSwimming = player.isSwimming
        val swimming = if (isSwimming) -1 else 1

        GlStateManager.pushMatrix()

        if (player.isInSneakingPose) {
            GlStateManager.translatef(0.0F, 0.25F, -0.1F)
        }
        if (player.isSwimming){
            GlStateManager.rotatef(180F, 1f, 0f, 0f)
        }

        GlStateManager.scalef((-0.625f), (-0.625f), swimming * 0.625f)

        ModelTransformation.applyGl(Transformation(
                /*rotation     */Vector3f(0F,-part.yaw * rad,0F),
                /*translation  */Vector3f(0F,0F,0F),
                /*scale        */Vector3f(1F,1F,1F)
        ),false)

        ModelTransformation.applyGl(Transformation(
                /*rotation     */Vector3f(-part.pitch * rad , 0F,0F),
                /*translation  */Vector3f(0F, 0F, 0F),
                /*scale        */Vector3f(1F,1F,1F)
        ),false)

        ModelTransformation.applyGl(Transformation(
                /*rotation     */Vector3f(0F, 0F,part.roll * rad * swimming),
                /*translation  */Vector3f(0F, 0F,0F),
                /*scale        */Vector3f(1F,1F,1F)
        ),false)

        ModelTransformation.applyGl(Transformation(
                /*rotation     */Vector3f(0F, 0F,0F),
                /*translation  */Vector3f(0F,0F,0F),
                /*scale        */Vector3f(1f, 1f, 1f)
        ),false)

        MinecraftClient.getInstance().firstPersonRenderer.renderItem(player, player.getEquippedStack(slot), ModelTransformation.Type.HEAD)

        GlStateManager.popMatrix()
    }

    private fun renderA(scale: Float, player: T, slot: EquipmentSlot) {
        val part = rightArm

        glMatrix {
            GlStateManager.translatef(part.x, part.y, part.z)

            if (part.pitch == 0.0f && part.yaw == 0.0f && part.roll == 0.0f) {
                if (part.rotationPointX == 0.0f && part.rotationPointY == 0.0f && part.rotationPointZ == 0.0f) {
                    renderItem(scale, player, slot)
                } else {
                    glMatrix {
                        GlStateManager.translatef(part.rotationPointX * scale, part.rotationPointY * scale, part.rotationPointZ * scale)
                        renderItem(scale, player, slot)
                    }
                }
            } else {
                glMatrix {
                    GlStateManager.translatef(part.rotationPointX * scale, part.rotationPointY * scale, part.rotationPointZ * scale)

                    if (part.roll  != 0.0f) GlStateManager.rotatef(part.roll * rad  , 0.0f, 0.0f, 1.0f)
                    if (part.yaw   != 0.0f) GlStateManager.rotatef(part.yaw * rad  , 0.0f, 1.0f, 0.0f)
                    if (part.pitch != 0.0f) GlStateManager.rotatef(part.pitch * rad, 1.0f, 0.0f, 0.0f)

                    renderItem(scale, player, slot)
                }
            }
        }
    }

    private fun renderItem(scale: Float, player: T, slot: EquipmentSlot){
        val part = rightArm
        glMatrix {

            // Fix part location
            GlStateManager.translatef(-part.rotationPointX * scale, -part.rotationPointY * scale, -part.rotationPointZ * scale)

            // Defaults
            GlStateManager.translatef(0F, -0.25F, 0F)
            GlStateManager.scalef(-0.625f, -0.625f, 0.625f)


            /*GlStateManager.translatef(0.0f, -0.25f, 0.0f)
            GlStateManager.rotatef(180.0f, 0.0f, 1.0f, 0.0f)
            GlStateManager.scalef(0.625f, -0.625f, -0.625f)*/
            /*
        ModelTransformation.applyGl(Transformation(
                /*rotation     */Vector3f(-90F,0F,0F),
                /*translation  */Vector3f(0F,0F,0F),
                /*scale        */Vector3f(1F,1F,1F)
        ),false)
        GlStateManager.pushMatrix()
        ModelTransformation.applyGl(Transformation(//front
                /*rotation     */Vector3f(0F,0F,0F),
                /*translation  */Vector3f(0F,0.00001F,0F),
                /*scale        */Vector3f(1F,1F,1F)
        ),false)
        GlStateManager.pushMatrix()
        ModelTransformation.applyGl(Transformation(//up
                /*rotation     */Vector3f(0F,0F,0F),
                /*translation  */Vector3f(0F,0F,0.00001F),
                /*scale        */Vector3f(1F,1F,1F)
        ),false)
        GlStateManager.pushMatrix()
        ModelTransformation.applyGl(Transformation(//out -> right
                /*rotation     */Vector3f(0F,0F,0F),
                /*translation  */Vector3f(1F,0F,0F),
                /*scale        */Vector3f(1F,1F,1F)
        ),false)
*/


            MinecraftClient.getInstance().firstPersonRenderer.renderItem(player, player.getEquippedStack(slot), ModelTransformation.Type.HEAD)
        }
       /* GlStateManager.popMatrix()
        GlStateManager.popMatrix()
        GlStateManager.popMatrix()*/
    }

    private fun glMatrix( operations :()-> Unit){
        GlStateManager.pushMatrix()
        operations()
        GlStateManager.popMatrix()
    }
}