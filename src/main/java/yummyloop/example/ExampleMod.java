package yummyloop.example;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import yummyloop.example.block.Block;
import yummyloop.example.item.Item;
import yummyloop.example.item.ItemGroup;

public class ExampleMod implements ModInitializer {

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        
        ItemGroup a = new ItemGroup("tutorial", "hello");
        Item i = new Item("tutorial", "fabric_item", a);
        i.addTooltip("item.tutorial.fabric_item.tooltip");

        Item b = new Item(a);
        b.register("tutorial", "fabric_item1");

        //Block EXAMPLE_BLOCK = new Block(Block.Settings.of(Material.METAL).strength(1.5F, 6.0F));
        //EXAMPLE_BLOCK.register("tutorial", "example_block");
        Block EXAMPLE_BLOCK = new Block("tutorial", "example_block", Material.METAL);
        Registry.register(Registry.ITEM, new Identifier("tutorial", "example_block"), new BlockItem(EXAMPLE_BLOCK, new Item.Settings().group(ItemGroup.MISC)));

        System.out.println("Hello Fabric world!");
    }
}
