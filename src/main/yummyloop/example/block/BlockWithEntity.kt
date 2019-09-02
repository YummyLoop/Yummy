package yummyloop.example.block

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.Material
import net.minecraft.block.MaterialColor
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.block.BlockWithEntity as VanillaBlockWithEntity

abstract class BlockWithEntity constructor(modId: String, itemName: String, settings: FabricBlockSettings) :
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

    override fun getRenderType(blockState_1: BlockState?): BlockRenderType {
        return BlockRenderType.MODEL
    }
}