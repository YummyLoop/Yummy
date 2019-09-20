package yummyloop.example.mixin.client.network;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.packet.EntitySpawnS2CPacket;
import net.minecraft.client.sound.RidingMinecartSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.util.ThreadExecutor;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yummyloop.example.ExampleMod;
import yummyloop.example.item.spear.Spear.SpearEntity;

@Mixin({ClientPlayNetworkHandler.class})
public abstract class OnEntitySpawn implements ClientPlayPacketListener {
    @Shadow
    private ClientWorld world;
    @Shadow
    private MinecraftClient client;

    @Inject(
            at = @At("HEAD"),
            method = "onEntitySpawn(Lnet/minecraft/client/network/packet/EntitySpawnS2CPacket;)V",
            expect = 0,
            cancellable = true
    )
    private void onOnEntitySpawn(@NotNull EntitySpawnS2CPacket packet, @NotNull CallbackInfo info) {
        Intrinsics.checkParameterIsNotNull(packet, "packet");
        Intrinsics.checkParameterIsNotNull(info, "info");
        if (this.client != null && this.world != null) {
            Packet var10000 = (Packet)packet;
            PacketListener var10001 = (PacketListener)this;
            MinecraftClient var10002 = this.client;
            if (var10002 == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.minecraft.util.ThreadExecutor<*>");
            } else {
                NetworkThreadUtils.forceMainThread(var10000, var10001, (ThreadExecutor)var10002);
                double x = packet.getX();
                double y = packet.getY();
                double z = packet.getZ();
                EntityType entityType = packet.getEntityTypeId();
                Object entity = null;
                Entity player = null;
                if (Intrinsics.areEqual(entityType, ExampleMod.INSTANCE.getSpearType())) {
                    EntityType var16 = ExampleMod.INSTANCE.getSpearType();
                    ClientWorld var10003 = this.world;
                    if (var10003 == null) {
                        Intrinsics.throwNpe();
                    }

                    SpearEntity var13 = new SpearEntity(var16, (World)var10003, x, y, z);
                    entity = var13;
                    ClientWorld var14 = this.world;
                    if (var14 == null) {
                        Intrinsics.throwNpe();
                    }

                    player = var14.getEntityById(packet.getEntityData());
                    if (player != null) {
                        ((ProjectileEntity)entity).setOwner(player);
                    }

                    int packetId = packet.getId();
                    ((Entity)entity).updateTrackedPosition(x, y, z);
                    ((Entity)entity).pitch = (float)(packet.getPitch() * 360) / 256.0F;
                    ((Entity)entity).yaw = (float)(packet.getYaw() * 360) / 256.0F;
                    ((Entity)entity).setEntityId(packetId);
                    ((Entity)entity).setUuid(packet.getUuid());
                    var14 = this.world;
                    if (var14 == null) {
                        Intrinsics.throwNpe();
                    }

                    var14.addEntity(packetId, (Entity)entity);
                    if (entity instanceof AbstractMinecartEntity) {
                        MinecraftClient var15 = this.client;
                        if (var15 == null) {
                            Intrinsics.throwNpe();
                        }
                        var15.getSoundManager().play((SoundInstance)(new RidingMinecartSoundInstance((AbstractMinecartEntity)entity)));
                    }

                    info.cancel();
                }
            }
        }
    }
}
