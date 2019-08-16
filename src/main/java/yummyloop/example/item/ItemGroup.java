package yummyloop.example.item;

import java.util.List;
import java.util.function.Consumer;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;

/**
 * ItemGroup defaults to MISC <p>
 * <b>Note:</b> ItemGroup can be set as null for no ItemGroup
 */
public class ItemGroup {
    private static final Item defaultItemToIcon = Items.APPLE;
    private net.minecraft.item.ItemGroup mcItemGroup = null;
    private FabricItemGroupBuilder fabricItemGroup = null;
    // Minecraft ItemGroups
    public static final net.minecraft.item.ItemGroup BUILDING_BLOCKS = net.minecraft.item.ItemGroup.BUILDING_BLOCKS;
    public static final net.minecraft.item.ItemGroup DECORATIONS = net.minecraft.item.ItemGroup.DECORATIONS;
    public static final net.minecraft.item.ItemGroup REDSTONE = net.minecraft.item.ItemGroup.REDSTONE;
    public static final net.minecraft.item.ItemGroup TRANSPORTATION = net.minecraft.item.ItemGroup.TRANSPORTATION;
    public static final net.minecraft.item.ItemGroup MISC = net.minecraft.item.ItemGroup.MISC;
    public static final net.minecraft.item.ItemGroup FOOD = net.minecraft.item.ItemGroup.FOOD;
    public static final net.minecraft.item.ItemGroup TOOLS = net.minecraft.item.ItemGroup.TOOLS;
    public static final net.minecraft.item.ItemGroup COMBAT = net.minecraft.item.ItemGroup.COMBAT;
    public static final net.minecraft.item.ItemGroup BREWING = net.minecraft.item.ItemGroup.BREWING;

    public ItemGroup(String modid, String name){
        this.fabricItemGroup = FabricItemGroupBuilder.create(new Identifier(modid, name)).icon(() -> new ItemStack(defaultItemToIcon));
    }

    public ItemGroup(String modid, String name, ItemConvertible itemToIcon){
        this.fabricItemGroup = FabricItemGroupBuilder.create(new Identifier(modid, name)).icon(() -> new ItemStack(itemToIcon));
    }
   
    public ItemGroup(String modid, String name, ItemConvertible itemToIcon, Consumer<List<ItemStack>> itemStacks) {
        this(modid, name, itemToIcon);
        this.fabricItemGroup.appendItems(itemStacks);
    }

    public net.minecraft.item.ItemGroup getGroup() {
        if (this.mcItemGroup == null) {
            this.mcItemGroup = this.fabricItemGroup.build();
        }
        return this.mcItemGroup;
    }
}

/* Example
    // New Fabric ItemGroup
    FabricItemGroupBuilder ola = ItemGroup.init("tutorial", "other");
    // New Item
    net.minecraft.item.Item a = new Item("tutorial", "fabric_item", ItemGroup.MISC);
    // New Custom ItemStack
    ItemStack b = new ItemStack(a);
    b.getOrCreateTag().putInt("CustomModelData", 1);
    b.setCustomName(new LiteralText("Hello"));
    
    // Append Ttems(Stacks) to the ItemGroup 
    ola.appendItems(stacks ->
    {
        stacks.add(new ItemStack(Blocks.BONE_BLOCK));
        stacks.add(new ItemStack(Items.APPLE));
        stacks.add(ItemStack.EMPTY);
        stacks.add(new ItemStack(Items.IRON_SHOVEL));
        stacks.add(b);
    });
    // Build the ItemGroup
    ola.build();
*/