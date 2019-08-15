package yummyloop.example.item;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.item.ItemGroup;
//import net.minecraft.item.Item;

public class Item extends net.minecraft.item.Item {
           
    public Item(net.minecraft.item.Item.Settings itemSettings) {
        super(itemSettings);
    }

    public Item() {
        super(new net.minecraft.item.Item.Settings().group(ItemGroup.MISC));
    }

    public Item(int maxDamage, int maxCount, net.minecraft.item.Item recipeRemainder, ItemGroup group, net.minecraft.util.Rarity rarity, net.minecraft.item.FoodComponent foodComponent) {
        super(new net.minecraft.item.Item.Settings().maxDamage(maxDamage).maxCount(maxCount).recipeRemainder(recipeRemainder).group(group).rarity(rarity).food(foodComponent));
    }
 
    public Item(int maxDamage, int maxCount, ItemGroup group) {
        super(new net.minecraft.item.Item.Settings().maxDamage(maxDamage).maxCount(maxCount).group(group));
    }

    public Item(ItemGroup group) {
        super(new net.minecraft.item.Item.Settings().group(group));
    }

    public Item(String modid, String itemName){
        super(new net.minecraft.item.Item.Settings().group(ItemGroup.MISC));
        register(modid, itemName);
    }

    public Item(String modid, String itemName, ItemGroup group) {
        super(new net.minecraft.item.Item.Settings().group(group));
        register(modid, itemName);
    }

    public boolean register (String modid, String itemName) {
        Registry.register(Registry.ITEM, new Identifier(modid, itemName), this);
        return true;
    }
    

}