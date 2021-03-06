package yummyloop.yummy.old.mixin.entity.player;

import net.minecraft.entity.player.HungerManager;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({HungerManager.class})
public final class Hunger {
    @Shadow
    private int foodLevel = 20;
    @Shadow
    private float foodSaturationLevel = 5.0F;
    @Shadow
    private float exhaustion;
    @Shadow
    private int foodStarvationTimer;
    @Shadow
    private int prevFoodLevel = 20;

    @Inject(
            at = @At(
                    target = "Lnet/minecraft/entity/player/HungerManager;foodSaturationLevel:F",
                    value = "FIELD",
                    opcode = Opcodes.PUTFIELD
            ),
            method = "add",
            cancellable = true
    )
    private void onHungerAdd(int hunger, float saturationModifier, CallbackInfo info) {
        this.foodLevel = hunger + this.prevFoodLevel;
    }

    /*
    @Inject(at = [At("HEAD")], method = ["update"])
    private fun onUpdate(playerEntity_1 : PlayerEntity, info: CallbackInfo) {
        if (foodLevel!=prevFoodLevel)
        println("Hunger is $foodLevel & $foodSaturationLevel & $exhaustion & $foodStarvationTimer")
    }
    */
}
