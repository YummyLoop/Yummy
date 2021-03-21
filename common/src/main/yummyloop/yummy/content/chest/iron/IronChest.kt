package yummyloop.yummy.content.chest.iron

import net.minecraft.block.entity.BlockEntity
import net.minecraft.world.BlockView
import yummyloop.yummy.content.chest.singleChest.SingleChestBlock

open class IronChest(settings: Settings) : SingleChestBlock(settings) {
    override val columns = 9
    override val rows = 6

    override fun createBlockEntity(world: BlockView?): BlockEntity = IronChestEntity(columns, rows)
}