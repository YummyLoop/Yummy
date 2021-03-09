package yummyloop.yummy.fabric

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import yummyloop.yummy.ExampleMod.init
import yummyloop.yummy.integration.geckolib.fabric.GeckoUtilsImpl
import yummyloop.yummy.registry.Register

object ExampleModFabric : ModInitializer, ClientModInitializer {
    override fun onInitialize() = init()

    override fun onInitializeClient() {
        GeckoUtilsImpl.registerAll()
        Register.Client.register()
    }
}