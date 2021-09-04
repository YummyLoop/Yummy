package yummyloop.yummy.content.chest.iron

import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.math.BlockPos
import yummyloop.yummy.content.chest.singleChest.SingleChestEntity

class IronChestEntity(size: Int, blockPos: BlockPos?, blockState: BlockState?) : SingleChestEntity(rType!!.get(), size, blockPos, blockState) {
    constructor(blockPos: BlockPos?, blockState: BlockState?) : this(54, blockPos, blockState)

    companion object {
        var rType: RegistrySupplier<BlockEntityType<IronChestEntity>>? = null
    }

    override fun getContainerName(): Text = TranslatableText(rType!!.id.toString())
}