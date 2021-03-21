package yummyloop.yummy.content.chest.singleChest

import me.shedaniel.architectury.registry.RegistrySupplier
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import yummyloop.common.block.entity.AnimatableChestContainerBlockEntity
import yummyloop.common.network.packets.PacketBuffer
import yummyloop.common.network.packets.add
import yummyloop.yummy.content.chest.ChestScreenHandler

open class SingleChestEntity(type: BlockEntityType<*>, var columns: Int, var rows: Int) :
    AnimatableChestContainerBlockEntity(type, columns * rows) {
    constructor(columns: Int, rows: Int) : this(rType!!.get(), columns, rows)
    constructor() : this(9, 3)

    companion object {
        var rType: RegistrySupplier<BlockEntityType<SingleChestEntity>>? = null
    }

    init {
        //
    }

    /** Screen provider, create menu */
    override fun createScreenHandler(syncId: Int, playerInventory: PlayerInventory): ScreenHandler {
        return ChestScreenHandler(syncId, playerInventory, PacketBuffer(columns, rows), columns, rows, this)
    }


    /** Screen provider, packet extra data */
    override fun saveExtraData(buf: PacketByteBuf) {
        buf.add(columns, rows)
    }

    override fun getContainerName(): Text = TranslatableText(rType!!.id.toString())
}