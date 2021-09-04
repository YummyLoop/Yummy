package yummyloop.yummy.content.chest.singleChest

import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.math.BlockPos
import yummyloop.common.block.entity.AnimatableChestContainerBlockEntity
import yummyloop.common.network.packets.PacketBuffer
import yummyloop.yummy.content.chest.ChestScreenHandler

open class SingleChestEntity(type: BlockEntityType<*>, val size : Int, blockPos: BlockPos?, blockState: BlockState?) :
    AnimatableChestContainerBlockEntity(type, size, blockPos, blockState) {
    constructor(size: Int, blockPos: BlockPos?, blockState: BlockState?) : this(rType!!.get(), size, blockPos, blockState)
    constructor(blockPos: BlockPos?, blockState: BlockState?) : this(27, blockPos, blockState)

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