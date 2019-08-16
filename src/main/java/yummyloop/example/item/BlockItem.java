package yummyloop.example.item;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import yummyloop.example.block.Block;

public class BlockItem extends net.minecraft.item.BlockItem {
    
    public BlockItem(Block block){
        super(block, new Item.Settings().group(ItemGroup.MISC));
    }

    public BlockItem(Block block, Item.Settings itemSettings){
        super(block, itemSettings);
    }

    public BlockItem(Block block, net.minecraft.item.Item.Settings itemSettings) {
        super(block, itemSettings);
    }

    public BlockItem(String modid, String itemName){
        this(new Block(modid, itemName));
        register(modid, itemName);
    }

    public BlockItem(String modid, String itemName, Block block){
        this(block);
        register(modid, itemName);
    }

    public BlockItem(String modid, String itemName, Block block, Item.Settings itemSettings){
        this(block, itemSettings);
        register(modid, itemName);
    }

    public BlockItem(String modid, String itemName, Block.Settings blockSettings, Item.Settings itemSettings){
        this(new Block(modid, itemName, blockSettings), itemSettings);
        register(modid, itemName);
    }

    public BlockItem(String modid, String itemName, Block block, ItemGroup group){
        this(block, new Item.Settings().group(group.getGroup()));
        register(modid, itemName);
    }

    
    public BlockItem(String modid, String itemName, ItemGroup group){
        this(new Block(modid, itemName), new Item.Settings().group(group.getGroup()));
        register(modid, itemName);
    }

    // End of constructors
    public boolean register (String modid, String itemName) {
        Registry.register(Registry.ITEM, new Identifier(modid, itemName), this);
        return true;
    }
}