package yummyloop.example.block;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Block extends net.minecraft.block.Block {

    public static class Settings extends FabricBlockSettings{
        protected Settings(Block base) {
            super(base);
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

    public Block(String modid, String itemName, FabricBlockSettings settings){
        this(settings);
        register(modid, itemName);
    }

    // End of constructors 
    public void register (String modid, String itemName) {
        Registry.register(Registry.BLOCK, new Identifier(modid, itemName), this);
    }
}

/*Settings list:
  of ... Material material &/| MaterialColor color | DyeColor color
   breakByHand(boolean breakByHand)
   breakByTool(Tag<Item> tag, int miningLevel)
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