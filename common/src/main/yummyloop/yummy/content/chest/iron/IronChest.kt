package yummyloop.yummy.content.chest.iron

import net.minecraft.block.entity.BlockEntity
import net.minecraft.world.BlockView
import yummyloop.yummy.content.chest.doubleChest.DoubleChestBlock

open class IronChest(settings: Settings) : DoubleChestBlock(settings) {
    override val columns = 9
    override val rows = 6

    override fun createBlockEntity(world: BlockView?): BlockEntity = IronChestEntity(columns, rows)
}