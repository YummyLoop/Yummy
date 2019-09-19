package yummyloop.example

import net.fabricmc.api.ClientModInitializer
import yummyloop.example.util.registry.ClientManager

object ExampleModClient : ClientModInitializer {
    override fun onInitializeClient() {
        ClientManager.ini()
    }
}