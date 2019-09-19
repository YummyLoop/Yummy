package yummyloop.example.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(MinecraftClient.class)
public final class ExampleMixin {
    @Inject(
            at = @At("HEAD"),
            method = "init()V"
    )
    private void init(CallbackInfo info) {
        String var2 = "This line is printed by an example mod mixin!";
        System.out.println(var2);
    }
}