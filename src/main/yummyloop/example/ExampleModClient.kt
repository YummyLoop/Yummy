package yummyloop.example

import net.fabricmc.api.ClientModInitializer
import yummyloop.example.item.Items
import yummyloop.example.item.backpack.HasClient

class ExampleModClient : ClientModInitializer {
    override fun onInitializeClient() {
        // Init items
        for (i in Items.itemList) {
            if (i.value is HasClient) {
                (i.value as HasClient).client()
            }
        }
        Items.blockA.client()
    }
}