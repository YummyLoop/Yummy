package yummyloop.example.block;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Block extends net.minecraft.block.Block {

    public static class Settings{
        public static FabricBlockSettings of(Material material) {
            return FabricBlockSettings.of(material, material.getColor());
        }
    
        public static FabricBlockSettings of(Material material, MaterialColor color) {
            return FabricBlockSettings.of(material, color);
        }

        public static FabricBlockSettings of(Material material, DyeColor color) {
            return FabricBlockSettings.of(material, color.getMaterialColor());
        }
    }

    public Block(FabricBlockSettings settings){
        super(settings.build());
    }

    public Block() {
        this(Settings.of(Material.AIR));
    }

    public Block(String modid, String itemName){
        this();
        register(modid, itemName);
    }

    public Block(String modid, String itemName, Material material){
        this(Settings.of(material));
        register(modid, itemName);
    }

    // End of constructors 
    public boolean register (String modid, String itemName) {
        Registry.register(Registry.BLOCK, new Identifier(modid, itemName), this);
        return true;
    }
}

/**Settings list:
 * of ... Material material &/| MaterialColor color | DyeColor color
 *  breakByHand(boolean breakByHand)
 *  breakByTool(Tag<Item> tag, int miningLevel)
 *  breakByTool(Tag<Item> tag)
 *  materialColor(MaterialColor color)
 *  materialColor(DyeColor color)
 *  collidable(boolean collidable)
 *  noCollision()
 *  sounds(BlockSoundGroup group)
 *  ticksRandomly()
 *  lightLevel(int lightLevel)
 *  hardness(float hardness)
 *  resistance(float resistance)
 *  strength(float hardness, float resistance)
 *  breakInstantly()
 *  dropsNothing()
 *  dropsLike(Block block)
 *  drops(Identifier dropTableId)
 *  friction(float friction)
 *  slipperiness(float value)
 *  dynamicBounds()
 */