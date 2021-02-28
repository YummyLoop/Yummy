package net.examplemod.fabric

import net.examplemod.ExampleMod.init
import net.examplemod.integration.geckolib.fabric.GeckoUtilsImpl
import net.examplemod.registry.Register
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer

object ExampleModFabric : ModInitializer, ClientModInitializer {
    override fun onInitialize() = init()

    override fun onInitializeClient() {
        GeckoUtilsImpl.registerAll()
        Register.Client.register()
    }
}