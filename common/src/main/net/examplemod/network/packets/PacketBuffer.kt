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
                    is CompoundTag -> obj.writeCompoundTag(i)
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

fun PacketByteBuf.add(vararg args: Any): PacketByteBuf = PacketBuffer.add(this, *args)
fun PacketByteBuf.write(vararg args: Any): PacketByteBuf = PacketBuffer.add(this, *args)