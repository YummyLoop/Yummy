package yummyloop.example.block

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.Material
import yummyloop.example.util.registry.RegistryManager
import net.minecraft.block.BlockWithEntity as VanillaBlockWithEntity

abstract class BlockWithEntity : VanillaBlockWithEntity {

    constructor(blockName: String, settings: FabricBlockSettings) : super(settings.build()) {
        register(blockName)
    }

    // BlockName
    constructor(blockName: String) : this(blockName, Block.of(Material.AIR))

    private fun register(blockName: String) = RegistryManager.register(this, blockName)

    override fun getRenderType(blockState: BlockState): BlockRenderType = BlockRenderType.MODEL
}