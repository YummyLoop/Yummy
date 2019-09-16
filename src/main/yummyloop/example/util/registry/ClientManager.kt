package yummyloop.example.util.registry

import net.fabricmc.fabric.api.client.render.ColorProviderRegistry
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry
import net.minecraft.client.color.item.ItemColorProvider
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.DyeableItem
import net.minecraft.item.ItemConvertible
import net.minecraft.util.Identifier
import net.minecraft.util.PacketByteBuf
import yummyloop.example.ExampleMod
import yummyloop.example.item.Items

typealias Screen = (Int, Identifier, PlayerEntity, PacketByteBuf) -> AbstractContainerScreen<*>

object ClientManager {
    private var modId : String = ExampleMod.id
    private val itemList = Items
    private val screens = HashMap< Identifier, Screen>()

    fun registerScreen(id : String, screen : Screen){
        if (screens.putIfAbsent(Identifier(modId, id), screen) != null){
            ExampleMod.logger.error("Screen $id already exists!")
        }
    }

    private fun registerScreenFactory(id : Identifier, screen : Screen){
        ScreenProviderRegistry.INSTANCE.registerFactory(id) {
            syncId, identifier, player, buf ->
            screen(syncId, identifier ,player, buf)
        }
    }

    private fun registerDyeableItem (item : ItemConvertible){
        ColorProviderRegistry.ITEM.register(// json model requires a "tintindex" while 2d uses the texture layer
            ItemColorProvider { itemStack, layer ->
                if(layer != 0){
                    -1
                }else{
                    val color = (itemStack.item as DyeableItem).getColor(itemStack)
                    if (color == 10511680) { // Default color when color was not found
                        -1
                    }else{
                        color
                    }
                }
            },
            item
        )
    }

    fun ini(){
        // Init items
        for (i in itemList) {
            if (i.value is DyeableItem) {
                registerDyeableItem(i.value)
            }
        }
        for (i in screens){
            registerScreenFactory(i.key,i.value)
        }
    }
}