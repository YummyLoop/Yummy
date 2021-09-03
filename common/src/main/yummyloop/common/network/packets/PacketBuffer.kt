package yummyloop.common.network.packets

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import me.shedaniel.architectury.networking.NetworkManager
import me.shedaniel.architectury.platform.Platform
import net.fabricmc.api.EnvType
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import yummyloop.common.Common.LOG
import java.nio.ByteBuffer
import java.util.*

class PacketBuffer() : PacketByteBuf(Unpooled.buffer()) {
    constructor(vararg args: Any) : this() {
        add(*args)
    }

    companion object {
        fun <T> add(obj: T, vararg args: Any): T where T : PacketByteBuf {
            for (i in args) {
                when (i) {
                    is BlockHitResult -> obj.writeBlockHitResult(i)
                    is BlockPos -> obj.writeBlockPos(i)
                    is Boolean -> obj.writeBoolean(i)
                    is ByteArray -> obj.writeByteArray(i)
                    is ByteBuf -> obj.writeBytes(i)
                    is ByteBuffer -> obj.writeBytes(i)
                    is Char -> obj.writeChar(i.toInt())
                    is NbtCompound -> obj.writeNbt(i)
                    is Date -> obj.writeDate(i)
                    is Double -> obj.writeDouble(i)
                    is Enum<*> -> obj.writeEnumConstant(i)
                    is Float -> obj.writeFloat(i)
                    is Identifier -> obj.writeIdentifier(i)
                    is Int -> obj.writeInt(i)
                    is IntArray -> obj.writeIntArray(i)
                    is ItemStack -> obj.writeItemStack(i)
                    is Long -> obj.writeLong(i)
                    is LongArray -> obj.writeLongArray(i)
                    is String -> obj.writeString(i)
                    is Text -> obj.writeText(i)
                    is UUID -> obj.writeUuid(i)
                    else -> {
                        LOG.error("Incorrect usage of PacketBuffer!")
                        LOG.error("Tried to add:" + i.javaClass.typeName)
                        throw Exception(i.toString())
                    }
                }
            }
            return obj
        }
    }
}

/** 
 * Inserts the specified vararg elements at the tail of this packet 
 * 
 * @param args the elements to insert
 * @return self
 */
fun PacketByteBuf.add(vararg args: Any): PacketByteBuf = PacketBuffer.add(this, *args)

/**
 * Inserts the specified vararg elements at the tail of this packet
 *
 * @param args the elements to insert
 * @return self
 */
fun PacketByteBuf.write(vararg args: Any): PacketByteBuf = PacketBuffer.add(this, *args)

/**
 * Sends the Packet to a player
 *
 * @param player the player to send the packet to
 * @param id The identifier(modName, PacketName)
 */
fun PacketByteBuf.sendToPlayer(player: ServerPlayerEntity, id: Identifier) {
    NetworkManager.sendToPlayer(player, id, this)
}

/**
 * Sends the Packet to a collection of players
 *
 * @param players the collection of players to send the packet to
 * @param id The identifier(modName, PacketName)
 */
fun PacketByteBuf.sendToPlayers(players: Iterable<ServerPlayerEntity>, id: Identifier) {
    NetworkManager.sendToPlayers(players, id, this)
}

/**
 * Sends the Packet to the server
 *
 * @param id The identifier(modName, PacketName)
 */
fun PacketByteBuf.sendToServer(id: Identifier) {
    if (Platform.getEnv() == EnvType.CLIENT) NetworkManager.sendToServer(id, this)
    else LOG.warn("The packet $id can not be sent to the server when already on the server!")
}