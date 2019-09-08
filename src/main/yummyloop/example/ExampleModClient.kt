package yummyloop.example

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry
import net.minecraft.client.color.item.ItemColorProvider
import net.minecraft.item.DyeableItem
import net.minecraft.item.ItemConvertible
import yummyloop.example.item.Items
import yummyloop.example.render.HasClient

class ExampleModClient : ClientModInitializer {
    override fun onInitializeClient() {
        // Init items
        for (i in Items.items) {
            if (i.value is HasClient) {
                (i.value as HasClient).client()
            }
            if (i.value is DyeableItem) {
                registerDyeableItem(i.value)
            }
        }

        for (i in Items.containers) {
            if (i.value is HasClient) {
                (i.value as HasClient).client()
            }
        }

        for (i in Items.blockEntities) {
            if (i.value is HasClient) {
                (i.value as HasClient).client()
            }
        }
    }

    private fun registerDyeableItem (item : ItemConvertible){
        ColorProviderRegistry.ITEM.register(// json model requires a "tintindex" while 2d uses the texture layer
                ItemColorProvider { itemStack, layer ->
                    if(layer != 0){
                        -1
                    }else{
                        (itemStack.item as DyeableItem).getColor(itemStack)
                    }
                },
                item
        )
    }
}