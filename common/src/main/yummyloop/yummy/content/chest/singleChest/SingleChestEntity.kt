package yummyloop.yummy.content.chest.singleChest

import me.shedaniel.architectury.registry.RegistrySupplier
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import yummyloop.common.block.entity.AnimatableChestContainerBlockEntity
import yummyloop.common.network.packets.PacketBuffer
import yummyloop.yummy.content.chest.ChestScreenHandler

open class SingleChestEntity(type: BlockEntityType<*>, val size : Int) :
    AnimatableChestContainerBlockEntity(type, size) {
    constructor(size: Int) : this(rType!!.get(), size)
    constructor() : this(27)

    companion object {
        var rType: RegistrySupplier<BlockEntityType<SingleChestEntity>>? = null
    }

    init {
        //
    }

    /** Screen provider, create menu */
    override fun createScreenHandler(syncId: Int, playerInventory: PlayerInventory): ScreenHandler {
        return ChestScreenHandler(syncId, playerInventory, PacketBuffer(), size, this)
    }

    override fun getContainerName(): Text = TranslatableText(rType!!.id.toString())
}