package yummyloop.example.block.entity

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.DyeColor
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.world.BlockView
import net.minecraft.world.World

class BlockE constructor(modId: String, itemName: String, settings: FabricBlockSettings) :
        BlockWithEntity(settings.build()) {

    companion object{
        fun of(material: Material): FabricBlockSettings {
            return FabricBlockSettings.of(material, material.color)
        }

        fun of(material: Material, color: MaterialColor): FabricBlockSettings {
            return FabricBlockSettings.of(material, color)
        }

        fun of(material: Material, color: DyeColor): FabricBlockSettings {
            return FabricBlockSettings.of(material, color.materialColor)
        }
    }

    init {
        register(modId, itemName)
    }

    // ModId, ItemName
    constructor(modId: String, itemName: String) :
            this(modId, itemName, of(Material.AIR))

    // End of constructors
    private fun register(modId: String, itemName: String) {
        Registry.register(Registry.BLOCK, Identifier(modId, itemName), this)
    }

    //-------------------------------------------------
    //Block entity stuff

    override fun createBlockEntity(blockView: BlockView): BlockEntity? {
        println("Created entity")
        return TestEntity()
    }

    override fun activate(blockState: BlockState, world: World, blockPos: BlockPos, player: PlayerEntity, hand: Hand, blockHitResult: BlockHitResult): Boolean {
        val blockEntity = world.getBlockEntity(blockPos)
        if (blockEntity is TestEntity) {
            val currentBlockEntity = blockEntity as TestEntity?
            currentBlockEntity!!.number = 11
            println("HAS entity")
        }

        return true
    }

    override fun getRenderType(blockState_1: BlockState?): BlockRenderType {
        return BlockRenderType.MODEL
    }
}