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
import net.minecraft.block.BlockRenderLayer

class TemplateBlockWithEntity constructor(modId: String, itemName: String, settings: FabricBlockSettings) :
        BlockWithEntity(modId, itemName, settings) {

    // ModId, ItemName
    constructor(modId: String, itemName: String) :
            this(modId, itemName, of(Material.AIR))

    //-------------------------------------------------
    //Block entity stuff
    @Environment(EnvType.CLIENT)
    object Client{
        private val rederer = BlockEntityRendererRegistry.INSTANCE.register(TemplateBlockEntity::class.java, TemplateBlockEntityRenderer())
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

    override fun getRenderLayer(): BlockRenderLayer {
        return BlockRenderLayer.TRANSLUCENT
    }

}