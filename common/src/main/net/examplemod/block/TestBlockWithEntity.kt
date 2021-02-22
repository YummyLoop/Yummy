package net.examplemod.block

import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.world.BlockView

class TestBlockWithEntity(settings: Settings) : Block(settings), BlockEntityProvider {
    override fun createBlockEntity(world: BlockView?): BlockEntity {
       return TestBlockEntity()
    }

    override fun getRenderType(state: BlockState?): BlockRenderType {
        return BlockRenderType.ENTITYBLOCK_ANIMATED
    }

}