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

    fun add(vararg args: Any): PacketBuffer {
        for (i in args) {
            when (i) {
                is BlockHitResult -> this.writeBlockHitResult(i)
                is BlockPos -> this.writeBlockPos(i)
                is Boolean -> this.writeBoolean(i)
                is ByteArray -> this.writeByteArray(i)
                is ByteBuf -> this.writeBytes(i)
                is ByteBuffer -> this.writeBytes(i)
                is Char -> this.writeChar(i.toInt())
                is CompoundTag -> this.writeCompoundTag(i)
                is Date -> this.writeDate(i)
                is Double -> this.writeDouble(i)
                is Enum<*> -> this.writeEnumConstant(i)
                is Float -> this.writeFloat(i)
                is Identifier -> this.writeIdentifier(i)
                is Int -> this.writeInt(i)
                is IntArray -> this.writeIntArray(i)
                is ItemStack -> this.writeItemStack(i)
                is Long -> this.writeLong(i)
                is LongArray -> this.writeLongArray(i)
                is String -> this.writeString(i)
                is Text -> this.writeText(i)
                is UUID -> this.writeUuid(i)
                else -> {
                    LOG.error("Incorrect usage of PacketBuffer!")
                    LOG.error("Tried to add:" + i.javaClass.typeName)
                    throw Exception(i.toString())
                }
            }
        }
        return this
    }
}