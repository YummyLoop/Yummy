package yummyloop.example.mixin.client.network

import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.client.network.packet.EntitySpawnS2CPacket
import net.minecraft.client.sound.RidingMinecartSoundInstance
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.entity.vehicle.AbstractMinecartEntity
import net.minecraft.network.NetworkThreadUtils
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.util.ThreadExecutor
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import yummyloop.example.ExampleMod
import yummyloop.example.item.Spear

@Mixin(ClientPlayNetworkHandler::class)
abstract class OnEntitySpawn : ClientPlayPacketListener {

    @Shadow private var world: ClientWorld? = null
    @Shadow private var client: MinecraftClient? = null

    @Inject(
            at = [At("HEAD")],
            method = ["Lnet/minecraft/client/network/ClientPlayNetworkHandler;onEntitySpawn(Lnet/minecraft/client/network/packet/EntitySpawnS2CPacket;)V"],
            expect = 0,
            cancellable = true
    )
    fun onOnEntitySpawn(packet : EntitySpawnS2CPacket, info: CallbackInfo) {
        if(client == null || world == null) return

        NetworkThreadUtils.forceMainThread<ClientPlayPacketListener>(packet, this, this.client as ThreadExecutor<*>)
        val x = packet.x
        val y = packet.y
        val z = packet.z
        val entityType = packet.entityTypeId
        val entity: Any?
        val player: Entity?

        when (entityType){
            ExampleMod.spearType -> {
                entity = Spear.SpearEntity(ExampleMod.spearType, world!!, x, y, z)
                player = world!!.getEntityById(packet.entityData)
                if (player != null) {
                    (entity as ProjectileEntity).owner = player
                }
            }
            else -> return
        }

        val packetId = packet.id
        (entity as Entity).updateTrackedPosition(x, y, z)
        (entity as Entity).pitch = (packet.pitch * 360).toFloat() / 256.0f
        (entity as Entity).yaw = (packet.yaw * 360).toFloat() / 256.0f
        (entity as Entity).entityId = packetId
        (entity as Entity).uuid = packet.uuid
        world!!.addEntity(packetId, entity as Entity)
        if (entity is AbstractMinecartEntity) {
            client!!.soundManager.play(RidingMinecartSoundInstance(entity as AbstractMinecartEntity))
        }
        info.cancel()
    }
}
