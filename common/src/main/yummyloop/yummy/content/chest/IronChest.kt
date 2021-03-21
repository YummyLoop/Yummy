package yummyloop.yummy.content.chest

import net.minecraft.block.entity.BlockEntity
import net.minecraft.world.BlockView

open class IronChest(settings: Settings) : ChestBlock(settings) {
    override val columns = 9
    override val rows = 6

    override fun createBlockEntity(world: BlockView?): BlockEntity = IronChestEntity(columns, rows)
}