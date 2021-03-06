package net.examplemod.network.packets

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import net.examplemod.LOG
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.PacketByteBuf
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import java.nio.ByteBuffer
import java.util.*


fun packetBuffer(): PacketByteBuf = PacketByteBuf(Unpooled.buffer())

fun packetBuffer(vararg args: Any): PacketByteBuf {
    val buff = packetBuffer()
    for (i in args) {
        when (i) {
            is BlockHitResult -> buff.writeBlockHitResult(i)
            is BlockPos -> buff.writeBlockPos(i)
            is Boolean -> buff.writeBoolean(i)
            is ByteArray -> buff.writeByteArray(i)
            is ByteBuf -> buff.writeBytes(i)
            is ByteBuffer -> buff.writeBytes(i)
            is Char -> buff.writeChar(i.toInt())
            is CompoundTag -> buff.writeCompoundTag(i)
            is Date -> buff.writeDate(i)
            is Double -> buff.writeDouble(i)
            is Enum<*> -> buff.writeEnumConstant(i)
            is Float -> buff.writeFloat(i)
            is Identifier -> buff.writeIdentifier(i)
            is Int -> buff.writeInt(i)
            is IntArray -> buff.writeIntArray(i)
            is ItemStack -> buff.writeItemStack(i)
            is Long -> buff.writeLong(i)
            is LongArray -> buff.writeLongArray(i)
            is String -> buff.writeString(i)
            is Text -> buff.writeText(i)
            is UUID -> buff.writeUuid(i)
            else -> {
                LOG.error("Incorrect usage of packetBuffer!")
                throw Exception()
            }
        }
    }
    return buff
}