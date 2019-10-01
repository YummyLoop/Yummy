package yummyloop.example.mixin.world;

import com.mojang.datafixers.DataFixer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldSaveHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yummyloop.example.util.data.LevelDataManager;

import java.io.File;


@Mixin(WorldSaveHandler.class)
public final class WorldSaveHandlerMixin {
    @Inject(
            at = @At("RETURN"),
            method = "<init>(Ljava/io/File;Ljava/lang/String;Lnet/minecraft/server/MinecraftServer;Lcom/mojang/datafixers/DataFixer;)V"
    )
    private void onWorldSaveHandler(File savesDir, String worldName, MinecraftServer server, DataFixer dataFixer, CallbackInfo info) {
        LevelDataManager.ini(new File(savesDir, worldName), server, dataFixer);
    }
}
