package yummyloop.yummy

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import yummyloop.yummy.integration.geckolib.fabric.GeckoUtilsImpl

object ExampleModFabric : ModInitializer, ClientModInitializer {
    override fun onInitialize() = ExampleMod.onInitialize()

    override fun onInitializeClient() {
        GeckoUtilsImpl.registerAll()
        ExampleMod.onInitializeClient()
    }
}