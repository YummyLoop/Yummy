package yummyloop.example.util.extensions

import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import net.minecraft.util.PacketByteBuf

/**
 * Open a modded container.
 *
 * @param identifier the identifier that was used when registering the container
 * @param writer     a PacketByteBuf where data can be written to, this data is then accessible by the container factory when creating the container or the gui
 */
fun PlayerEntity.openContainer(identifier: Identifier, writer: (PacketByteBuf) -> Unit){
    ContainerProviderRegistry.INSTANCE.openContainer(identifier, this, writer)
}
fun PlayerEntity.openContainer(identifier: Identifier){
    ContainerProviderRegistry.INSTANCE.openContainer(identifier, this) {Unit}
}

/**
 * Open a modded container.
 *
 * @param identifier the identifier that was used when registering the container
 * @param writer     a PacketByteBuf where data can be written to, this data is then accessible by the container factory when creating the container or the gui
 */
fun ServerPlayerEntity.openContainer(identifier : Identifier, writer : (PacketByteBuf) -> Unit){
    ContainerProviderRegistry.INSTANCE.openContainer(identifier, this, writer)
}
fun ServerPlayerEntity.openContainer(identifier : Identifier){
    ContainerProviderRegistry.INSTANCE.openContainer(identifier, this) {Unit}
}