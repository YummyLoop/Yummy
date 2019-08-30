package yummyloop.example

import net.fabricmc.api.ClientModInitializer
import yummyloop.example.item.backpack.Backpack

class ExampleModClient : ClientModInitializer {
    override fun onInitializeClient() {
        // Init Backpack client
        Backpack.Client
    }
}