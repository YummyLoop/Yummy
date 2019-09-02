package yummyloop.example.block.entity

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry
import net.minecraft.block.BlockRenderType
import net.minecraft.block.Material
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World
import yummyloop.example.block.BlockWithEntity
import net.minecraft.block.BlockState
import net.minecraft.util.shape.VoxelShape
import net.minecraft.block.Block as VanillaBlock
import net.minecraft.entity.EntityContext

class TemplateBlockWithEntity constructor(modId: String, itemName: String, settings: FabricBlockSettings) :
        BlockWithEntity(modId, itemName, settings) {

    // ModId, ItemName
    constructor(modId: String, itemName: String) :
            this(modId, itemName, of(Material.AIR))

    //-------------------------------------------------
    //Block entity stuff

    companion object {
        @Environment(EnvType.CLIENT)
        fun client() {
            BlockEntityRendererRegistry.INSTANCE.register(TemplateBlockEntity::class.java, TemplateBlockEntityRenderer())
        }
    }

    override fun createBlockEntity(blockView: BlockView): BlockEntity? {
        println("Created entity")
        return TemplateBlockEntity()
    }

    override fun activate(blockState: BlockState, world: World, blockPos: BlockPos, player: PlayerEntity, hand: Hand, blockHitResult: BlockHitResult): Boolean {
        val blockEntity = world.getBlockEntity(blockPos)
        if (blockEntity is TemplateBlockEntity) {
            val currentBlockEntity = blockEntity as TemplateBlockEntity?
            currentBlockEntity!!.number = 11
            println("HAS entity")
        }

        return true
    }

    override fun getRenderType(blockState_1: BlockState?): BlockRenderType {
       return BlockRenderType.MODEL
    }

    override fun getOutlineShape(blockState_1: BlockState?, blockView_1: BlockView?, blockPos_1: BlockPos?, entityContext_1: EntityContext?): VoxelShape {
        return VanillaBlock.createCuboidShape(1.0, 0.0, 1.0, 15.0, 20.0, 15.0)
    }
}