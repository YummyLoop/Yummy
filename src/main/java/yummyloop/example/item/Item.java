package yummyloop.example.item;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import yummyloop.example.item.ItemGroup;
//import net.minecraft.item.Item;

public class Item extends net.minecraft.item.Item {
           
    public Item(net.minecraft.item.Item.Settings itemSettings) {
        super(itemSettings);
    }

    public Item() {
        super(new net.minecraft.item.Item.Settings().group(ItemGroup.MISC));
    }

    public Item(String modid, String itemName){
        this();
        register(modid, itemName);
    }

    public Item(ItemGroup group) {
        super(new net.minecraft.item.Item.Settings().group(group.getGroup()));
    }

    public Item(String modid, String itemName, ItemGroup group) {
        this(group);
        register(modid, itemName);
    }
    
    public Item(String modid, String itemName, ItemGroup group, int maxCount) {
        super(new net.minecraft.item.Item.Settings().group(group.getGroup()).maxCount(maxCount));
        register(modid, itemName);
    }

    public Item(int maxDamage, int maxCount, net.minecraft.item.Item recipeRemainder, ItemGroup group, net.minecraft.util.Rarity rarity, net.minecraft.item.FoodComponent foodComponent) {
        super(new net.minecraft.item.Item.Settings().maxDamage(maxDamage).maxCount(maxCount).recipeRemainder(recipeRemainder).group(group.getGroup()).rarity(rarity).food(foodComponent));
    }

    // End of constructors 
    public boolean register (String modid, String itemName) {
        Registry.register(Registry.ITEM, new Identifier(modid, itemName), this);
        return true;
    }
}