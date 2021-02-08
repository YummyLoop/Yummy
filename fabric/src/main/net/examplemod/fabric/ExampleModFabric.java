package net.examplemod.fabric;

import net.examplemod.ExampleMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class ExampleModFabric implements ModInitializer, ClientModInitializer {
    @Override
    public void onInitialize() {
        ExampleMod.INSTANCE.init();
    }

    @Override
    public void onInitializeClient() {

    }
}
