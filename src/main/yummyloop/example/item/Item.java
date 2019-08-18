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
import yummyloop.example.item.ItemGroup;
//import net.minecraft.item.Item;

public class Item extends net.minecraft.item.Item {
    private List<Text> tooltip = new ArrayList<Text>();

    public static class Settings extends net.minecraft.item.Item.Settings{}
    
    public Item(net.minecraft.item.Item.Settings itemSettings){
        super(itemSettings);
    }

    public Item(Item.Settings itemSettings){
        super(itemSettings);
    }

    public Item() {
        super(new Settings().group(ItemGroup.MISC));
    }

    public Item(String modid, String itemName){
        this();
        register(modid, itemName);
    }

    public Item(ItemGroup group) {
        super(new Settings().group(group.getGroup()));
    }

    public Item(String modid, String itemName, ItemGroup group) {
        this(group);
        register(modid, itemName);
    }
    
    public Item(String modid, String itemName, ItemGroup group, int maxCount) {
        super(new Settings().group(group.getGroup()).maxCount(maxCount));
        register(modid, itemName);
    }

    public Item(int maxDamage, int maxCount, net.minecraft.item.Item recipeRemainder, ItemGroup group, net.minecraft.util.Rarity rarity, net.minecraft.item.FoodComponent foodComponent) {
        super(new Settings().maxDamage(maxDamage).maxCount(maxCount).recipeRemainder(recipeRemainder).group(group.getGroup()).rarity(rarity).food(foodComponent));
    }

    // End of constructors 
    public boolean register (String modid, String itemName) {
        Registry.register(Registry.ITEM, new Identifier(modid, itemName), this);
        return true;
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