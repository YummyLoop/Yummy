package net.examplemod.fabric

import net.examplemod.ExampleMod.init
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer

object ExampleModFabric : ModInitializer, ClientModInitializer {
    override fun onInitialize() = init()

    override fun onInitializeClient() {}
}