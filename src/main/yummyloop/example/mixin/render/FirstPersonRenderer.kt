package yummyloop.example.mixin.render

import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.FirstPersonRenderer as VanillaFirstPersonRenderer
import net.minecraft.client.render.item.ItemRenderer as VanillaItemRenderer
import net.minecraft.item.ItemStack
import net.minecraft.util.Arm
import net.minecraft.util.Hand
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import yummyloop.example.item.backpack.RenderHand

@Mixin(VanillaFirstPersonRenderer::class)
abstract class FirstPersonRenderer {
    @Shadow private val client: MinecraftClient? = null
    @Shadow private var mainHand: ItemStack? = null
    @Shadow private var offHand: ItemStack? = null
    @Shadow private var equipProgressMainHand: Float = 0F
    @Shadow private var prevEquipProgressMainHand: Float = 0F
    @Shadow private var equipProgressOffHand: Float = 0F
    @Shadow private var prevEquipProgressOffHand: Float = 0F
    @Shadow private val renderManager: EntityRenderDispatcher? = null
    @Shadow private val itemRenderer: VanillaItemRenderer? = null

    @Shadow
    abstract fun renderArmHoldingItem(float_1: Float, float_2: Float, arm_1: Arm)

    @Inject(at = [At("HEAD")], method = ["Lnet/minecraft/client/render/FirstPersonRenderer;renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;F)V"], cancellable = true)
    fun onRenderFirstPersonItem(playerEntity: AbstractClientPlayerEntity, float_1: Float, float_2: Float, hand: Hand, handSwingProgress: Float, stack: ItemStack, handSwapProgress: Float, info: CallbackInfo) {
        if (stack.item is RenderHand) {
            val mainHand = hand == Hand.MAIN_HAND
            val arm = if (mainHand) playerEntity.mainArm else playerEntity.mainArm.opposite

            if (!playerEntity.isInvisible) {
                GlStateManager.pushMatrix()
                this.renderArmHoldingItem(handSwapProgress, handSwingProgress, arm)
                GlStateManager.popMatrix()
            }
        }
    }

}
