package yummyloop.yummy.old.mixin.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yummyloop.yummy.old.render.firstPerson.RenderHand;

@Mixin({net.minecraft.client.render.FirstPersonRenderer.class})
public abstract class FirstPersonRenderer {
    @Final
    @Shadow
    private MinecraftClient client;
    @Shadow
    private ItemStack mainHand;
    @Shadow
    private ItemStack offHand;
    @Shadow
    private float equipProgressMainHand;
    @Shadow
    private float prevEquipProgressMainHand;
    @Shadow
    private float equipProgressOffHand;
    @Shadow
    private float prevEquipProgressOffHand;
    @Final
    @Shadow
    private EntityRenderDispatcher renderManager;
    @Final
    @Shadow
    private ItemRenderer itemRenderer;

    protected FirstPersonRenderer() {
    }

    @Shadow
    protected abstract void renderArmHoldingItem(float var1, float var2, @NotNull Arm var3);

    @Inject(
            at = {@At("HEAD")},
            method = "renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;F)V",
            cancellable = true
    )
    private void onRenderFirstPersonItem(@NotNull AbstractClientPlayerEntity playerEntity, float float_1, float float_2, @NotNull Hand hand, float handSwingProgress, @NotNull ItemStack stack, float handSwapProgress, @NotNull CallbackInfo info) {
        Intrinsics.checkParameterIsNotNull(playerEntity, "playerEntity");
        Intrinsics.checkParameterIsNotNull(hand, "hand");
        Intrinsics.checkParameterIsNotNull(stack, "stack");
        Intrinsics.checkParameterIsNotNull(info, "info");
        if (stack.getItem() instanceof RenderHand) {
            boolean mainHand = hand == Hand.MAIN_HAND;
            Arm var10000;
            if (mainHand) {
                var10000 = playerEntity.getMainArm();
            } else {
                var10000 = playerEntity.getMainArm();
                Intrinsics.checkExpressionValueIsNotNull(var10000, "playerEntity.mainArm");
                var10000 = var10000.getOpposite();
            }

            Arm arm = var10000;
            if (!playerEntity.isInvisible()) {
                GlStateManager.pushMatrix();
                Intrinsics.checkExpressionValueIsNotNull(arm, "arm");
                this.renderArmHoldingItem(handSwapProgress, handSwingProgress, arm);
                GlStateManager.popMatrix();
            }
        }
    }
}
