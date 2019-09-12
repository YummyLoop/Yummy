package yummyloop.example.mixin.entity.player

import net.minecraft.entity.player.HungerManager
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(HungerManager::class)
class Hunger {
    @Shadow private var foodLevel = 20
    @Shadow private var foodSaturationLevel = 5.0f
    @Shadow private var exhaustion: Float = 0F
    @Shadow private var foodStarvationTimer: Int = 0
    @Shadow private var prevFoodLevel = 20

    @Inject(at = [At("HEAD")], method = ["add"], cancellable = true)
    private fun onHungerAdd(hunger : Int, saturationModifier : Float, info: CallbackInfo) {
        this.foodLevel = this.foodLevel + hunger
        this.foodSaturationLevel = (this.foodSaturationLevel + hunger.toFloat() * saturationModifier * 2.0f).coerceAtMost(this.foodLevel.toFloat())
        info.cancel()
    }

    /*
    @Inject(at = [At("RETURN")], method = ["update"])
    private fun onUpdate(playerEntity_1 : PlayerEntity , info: CallbackInfo) {
        println("Hunger is $foodLevel")
    }
    */
}
