package yummyloop.yummy.old.mixin.server.world;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import net.minecraft.world.WorldSaveHandler;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yummyloop.yummy.old.util.data.DataManager;

import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {

    protected ServerWorldMixin(LevelProperties levelProperties_1, DimensionType dimensionType_1, BiFunction<World, Dimension, ChunkManager> biFunction_1, Profiler profiler_1, boolean boolean_1) {
        super(levelProperties_1, dimensionType_1, biFunction_1, profiler_1, boolean_1);
    }

    @Inject(
            at = @At("RETURN"),
            method = "<init>(Lnet/minecraft/server/MinecraftServer;" +
                            "Ljava/util/concurrent/Executor;" +
                            "Lnet/minecraft/world/WorldSaveHandler;" +
                            "Lnet/minecraft/world/level/LevelProperties;" +
                            "Lnet/minecraft/world/dimension/DimensionType;" +
                            "Lnet/minecraft/util/profiler/Profiler;" +
                            "Lnet/minecraft/server/WorldGenerationProgressListener;)V"
    )
    private void onServerWorld(MinecraftServer minecraftServer,
                               Executor executor,
                               WorldSaveHandler worldSaveHandler,
                               LevelProperties levelProperties,
                               DimensionType dimensionType,
                               Profiler profiler,
                               WorldGenerationProgressListener worldGenerationProgressListener,
                               CallbackInfo info) {

        DataManager.iniDimension( this);
    }

    @Inject(
            at = @At(
                    value = "INVOKE_STRING",
                    target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
                    shift = At.Shift.AFTER,
                    args = {"ldc=raid"}
            ),
            method = "tick(Ljava/util/function/BooleanSupplier;)V",
            expect = 0
    )
    private void onServerWorldTick(BooleanSupplier booleanSupplier, CallbackInfo info){
        DataManager.tick(this);
    }

    @Shadow
    public abstract PersistentStateManager getPersistentStateManager();

}
