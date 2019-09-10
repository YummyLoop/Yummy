package yummyloop.example.block

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.Material
import net.minecraft.block.MaterialColor
import net.minecraft.util.DyeColor
import yummyloop.example.util.registry.RegistryManager
import net.minecraft.block.BlockWithEntity as VanillaBlockWithEntity

abstract class BlockWithEntity constructor(blockName: String, settings: FabricBlockSettings) :
        VanillaBlockWithEntity(settings.build()) {

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
        register(blockName)
    }

    // BlockName
    constructor(blockName: String) :
            this(blockName, of(Material.AIR))

    // End of constructors
    private fun register(blockName: String) {
        RegistryManager.register(this, blockName)
    }

    //-------------------------------------------------
    //Block entity stuff

    override fun getRenderType(blockState_1: BlockState?): BlockRenderType {
        return BlockRenderType.MODEL
    }
}