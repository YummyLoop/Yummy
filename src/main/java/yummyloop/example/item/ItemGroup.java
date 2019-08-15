package yummyloop.example.item;

import java.util.List;
import java.util.function.Consumer;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.item.Item;

/**
 * ItemGroup defaults to MISC <p>
 * <b>Note:</b> ItemGroup can be set as null for no ItemGroup
 */
public abstract class ItemGroup {
    private static final Item defaultItem = Items.APPLE;
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

    /**
    * Create&Build a new ItemGroup
    */
    public static net.minecraft.item.ItemGroup create(String modid, String name) {
        return FabricItemGroupBuilder.create(new Identifier(modid, name)).icon(() -> new ItemStack(defaultItem)).build();
    }

    /**
    * Create&Build a new ItemGroup
    */
    public static net.minecraft.item.ItemGroup create(String modid, String name, Item item) {
        return FabricItemGroupBuilder.create(new Identifier(modid, name)).icon(() -> new ItemStack(item)).build();
    }

    /**
    * Create&Build a new ItemGroup
    */    
    public static net.minecraft.item.ItemGroup create(String modid, String name, Item item, Consumer<List<ItemStack>> itemStacks) {
        return FabricItemGroupBuilder.create(new Identifier(modid, name)).icon(() -> new ItemStack(item)).appendItems(itemStacks).build();
    }

    /**
    * Create a new FabricGroup <p>
    * <b>Note:</b> .buid() is required to build the ItemGroup
    */    
    public static FabricItemGroupBuilder init(String modid, String name) {
        return FabricItemGroupBuilder.create(new Identifier(modid, name)).icon(() -> new ItemStack(defaultItem));
    }

    /**
    * Create a new FabricGroup <p>
    * <b>Note:</b> .buid() is required to build the ItemGroup
    */        
    public static FabricItemGroupBuilder init(String modid, String name, Consumer<List<ItemStack>> itemStacks) {
        final Item defaultItem = Items.APPLE;
        return FabricItemGroupBuilder.create(new Identifier(modid, name)).icon(() -> new ItemStack(defaultItem)).appendItems(itemStacks);
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