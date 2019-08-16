package yummyloop.example;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Material;
import yummyloop.example.block.Block;
import yummyloop.example.item.BlockItem;
import yummyloop.example.item.Item;
import yummyloop.example.item.ItemGroup;

public class ExampleMod implements ModInitializer {

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        
        ItemGroup group_a = new ItemGroup("tutorial", "hello");
        Item i = new Item("tutorial", "fabric_item", group_a);
        i.addTooltip("item.tutorial.fabric_item.tooltip");

        new Item("tutorial", "fabric_item1", group_a);
         
        Block a = new Block("tutorial", "example_block", Block.Settings.of(Material.METAL).lightLevel(10));
        new BlockItem("tutorial", "example_block", a, group_a);

        System.out.println("Hello Fabric world!");
    }
}
