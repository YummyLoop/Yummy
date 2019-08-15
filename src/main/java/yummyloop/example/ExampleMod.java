package yummyloop.example;

import net.fabricmc.api.ModInitializer;
import yummyloop.example.item.Item;
import yummyloop.example.item.ItemGroup;

public class ExampleMod implements ModInitializer {

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		
		ItemGroup a = new ItemGroup("tutorial", "hello");
		new Item("tutorial", "fabric_item", a);
		
		System.out.println("Hello Fabric world!");
	}
}
