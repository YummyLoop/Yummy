package yummyloop.example.mixin.client.render

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Redirect
import org.spongepowered.asm.mixin.injection.Slice
import java.util.*
import net.minecraft.client.gui.hud.InGameHud as vanillaInGameHud


@Mixin(vanillaInGameHud::class)
abstract class InGameHud : DrawableHelper() {

    @Shadow private val random = Random()
    @Shadow private val client: MinecraftClient? = null
    @Shadow private var field_2014: Int = 0
    @Shadow private var field_2033: Int = 0
    @Shadow private var field_2012: Long = 0
    @Shadow private var field_2032: Long = 0
    @Shadow private var scaledWidth: Int = 0
    @Shadow private var scaledHeight: Int = 0
    @Shadow private var ticks: Int = 0
    @Shadow abstract fun getRiddenEntity() : LivingEntity
    @Shadow abstract fun method_1744(livingEntity_1: LivingEntity): Int
    @Shadow abstract fun getCameraPlayer(): PlayerEntity

    @Redirect(
            method = ["renderStatusBars"],
            slice = Slice(
                    from = At("INVOKE_STRING",
                            args = ["ldc=food"],
                            target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
                            shift = At.Shift.BY,
                            by = 0),
                    to =  At("INVOKE_STRING",
                            args = ["ldc=air"],
                            target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V")
            ),
            //at = At("FIELD", opcode = Opcodes.GETFIELD, ordinal = 0),
            at = At("INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;blit(IIIIII)V"),
            expect = 0
    )
    private fun onRenderHungerBar(a: vanillaInGameHud, i1:Int, i2:Int,i3:Int,i4:Int,i5:Int,i6:Int){
        // Disables Hunger bar rendering
        this.blit(i1, i2, i3, i4, i5, i6)
    }
}