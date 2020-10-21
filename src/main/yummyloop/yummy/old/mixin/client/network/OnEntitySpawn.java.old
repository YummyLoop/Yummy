package yummyloop.yummy.old.mixin.client.network;

import kotlin.Pair;
import kotlin.jvm.functions.Function4;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.packet.EntitySpawnS2CPacket;
import net.minecraft.client.sound.RidingMinecartSoundInstance;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yummyloop.yummy.old.util.registry.RegistryManager;

import java.util.Map;

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
    private void onOnEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo info) {
        if (this.client != null && this.world != null) {
            NetworkThreadUtils.forceMainThread(packet, this, this.client);
            EntityType packetEntityType = packet.getEntityTypeId();
            Entity player = world.getEntityById(packet.getEntityData());
            Entity entity = null;
            double x = packet.getX();
            double y = packet.getY();
            double z = packet.getZ();

            /*
            EntityType entityType = RegistryManager.INSTANCE.getMiscEntityType().get("spear_entity").getFirst();
            if (packetEntityType == entityType) {
                //entity = new SpearEntity(world, x, y, z);
                entity = RegistryManager.INSTANCE.getMiscEntityType().get("spear_entity").getSecond().invoke(world, x, y, z);
            }*/

            for (Map.Entry<String, Pair<EntityType<? extends Entity>, Function4<World, Double, Double, Double, Entity>>> entry : RegistryManager.INSTANCE.getMiscEntityType().entrySet()) {
                if (entry.getValue().getFirst() == packetEntityType) {
                    entity = entry.getValue().getSecond().invoke(world, x, y, z);
                    break;
                }
            }

            if (entity != null) {
                if (player != null && entity instanceof ProjectileEntity) {
                    ((ProjectileEntity) entity).setOwner(player);
                }
                int packetId = packet.getId();
                entity.updateTrackedPosition(x, y, z);
                entity.pitch = (float)(packet.getPitch() * 360) / 256.0F;
                entity.yaw = (float)(packet.getYaw() * 360) / 256.0F;
                entity.setEntityId(packetId);
                entity.setUuid(packet.getUuid());

                world.addEntity(packetId, entity);

                if (entity instanceof AbstractMinecartEntity) {
                    client.getSoundManager().play(new RidingMinecartSoundInstance((AbstractMinecartEntity)entity));
                }
                info.cancel();
            }
        }
    }
}
