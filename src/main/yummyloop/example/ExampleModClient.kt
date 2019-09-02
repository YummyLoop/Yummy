package yummyloop.example

import net.fabricmc.api.ClientModInitializer
import yummyloop.example.block.entity.TemplateBlockEntity
import yummyloop.example.block.entity.TemplateBlockWithEntity
import yummyloop.example.item.Items
import yummyloop.example.item.backpack.Backpack

class ExampleModClient : ClientModInitializer {
    override fun onInitializeClient() {
        // Init Backpack client
        Backpack.client()
        TemplateBlockWithEntity.client()
    }
}