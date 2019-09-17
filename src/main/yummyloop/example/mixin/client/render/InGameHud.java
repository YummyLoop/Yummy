package yummyloop.example.mixin.client.render;

import java.util.Random;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.At.Shift;

@Mixin({net.minecraft.client.gui.hud.InGameHud.class})
public abstract class InGameHud extends DrawableHelper {
    @Final
    @Shadow
    private Random random;
    @Final
    @Shadow
    private MinecraftClient client;
    @Shadow
    private int field_2014;
    @Shadow
    private int field_2033;
    @Shadow
    private long field_2012;
    @Shadow
    private long field_2032;
    @Shadow
    private int scaledWidth;
    @Shadow
    private int scaledHeight;
    @Shadow
    private int ticks;

    @Shadow
    @NotNull
    protected abstract LivingEntity getRiddenEntity();

    @Shadow
    protected abstract int method_1744(@NotNull LivingEntity var1);

    @Shadow
    @NotNull
    protected abstract PlayerEntity getCameraPlayer();

    @Redirect(
            method = "renderStatusBars",
            slice = @Slice(
                    from = @At(
                            target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
                            shift = Shift.BY,
                            by = 0,
                            value = "INVOKE_STRING",
                            args = {"ldc=food"}
                    ),
                    to = @At(
                            target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
                            value = "INVOKE_STRING",
                            args = {"ldc=air"}
                    )
            ),
            at = @At(
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;blit(IIIIII)V",
                    value = "INVOKE"
            ),
            expect = 0
    )
    private void onRenderHungerBar(net.minecraft.client.gui.hud.InGameHud a, int i1, int i2, int i3, int i4, int i5, int i6) {
        // Comment this to not render the hunger bar
        this.blit(i1, i2, i3, i4, i5, i6);
    }
}