package yummyloop.yummy.content.chest.iron

import net.minecraft.block.entity.BlockEntity
import net.minecraft.world.BlockView
import yummyloop.yummy.content.chest.singleChest.SingleChestBlock

open class IronChest(settings: Settings) : SingleChestBlock(settings) {
    override val size: Int = 54

    override fun createBlockEntity(world: BlockView?): BlockEntity = IronChestEntity(size)
}