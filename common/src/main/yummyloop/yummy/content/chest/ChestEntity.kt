package yummyloop.yummy.content.chest

import me.shedaniel.architectury.registry.RegistrySupplier
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import yummyloop.common.block.entity.AnimatableChestContainerBlockEntity
import yummyloop.common.network.packets.PacketBuffer

open class ChestEntity(type: BlockEntityType<*>, size: Int) : AnimatableChestContainerBlockEntity(type, size) {
    constructor(size: Int) : this(rType!!.get(), size)
    constructor() : this(9)

    companion object {
        var rType: RegistrySupplier<BlockEntityType<ChestEntity>>? = null
    }

    init {
        //LOG.info("Calling from TestBlockEntity")
    }

    /** Screen provider, create menu */
    override fun createScreenHandler(syncId: Int, playerInventory: PlayerInventory): ScreenHandler =
        ChestScreenHandler(syncId, playerInventory, PacketBuffer(), this)

    /** Screen provider, packet extra data */
    override fun saveExtraData(buf: PacketByteBuf?) {}

    override fun getContainerName(): Text = TranslatableText("a_chest")
}