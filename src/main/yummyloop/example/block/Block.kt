package yummyloop.example.block

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.Material
import net.minecraft.block.MaterialColor
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.block.Block as VanillaBlock

// ModId, ItemName, FabricBlockSettings
class Block constructor(modId: String, itemName: String, settings: FabricBlockSettings) :
        VanillaBlock(settings.build()) {

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

}

/*Settings list:
  of ... Material material &/| MaterialColor color | DyeColor color
   breakByHand(boolean breakByHand)
   breakByTool(Tag<Item> tag, int miningLevel) / miningLevels
   breakByTool(Tag<Item> tag)
   materialColor(MaterialColor color)
   materialColor(DyeColor color)
   collidable(boolean collidable)
   noCollision()
   sounds(BlockSoundGroup group)
   ticksRandomly()
   lightLevel(int lightLevel)
   hardness(float hardness)
   resistance(float resistance)
   strength(float hardness, float resistance)
   breakInstantly()
   dropsNothing()
   dropsLike(Block block)
   drops(Identifier dropTableId)
   friction(float friction)
   slipperiness(float value)
   dynamicBounds()
 */