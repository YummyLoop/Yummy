package yummyloop.example.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import yummyloop.example.block.Block;

public class BlockItem extends net.minecraft.item.BlockItem {
    private final List<Text> tooltip = new ArrayList<>();
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
    public void register (String modid, String itemName) {
        Registry.register(Registry.ITEM, new Identifier(modid, itemName), this);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.addAll(this.tooltip);
    }

    /**
     * Appends a tooltip to the item
     * @param tooltip example: item.tutorial.fabric_item.tooltip
     */
    public void addTooltip(String tooltip){
        this.tooltip.add(new TranslatableText(tooltip));
    }
}