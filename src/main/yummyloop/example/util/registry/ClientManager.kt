package yummyloop.example.util.registry

import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry
import net.minecraft.client.color.item.ItemColorProvider
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.DyeableItem
import net.minecraft.item.ItemConvertible
import net.minecraft.util.Identifier
import net.minecraft.util.PacketByteBuf
import yummyloop.example.ExampleMod
import yummyloop.example.item.Items
import net.minecraft.block.entity.BlockEntity as VanillaBLockEntity
import net.minecraft.item.Items as VanillaItems

typealias Screen = (Int, Identifier, PlayerEntity, PacketByteBuf) -> AbstractContainerScreen<*>

object ClientManager {
    private var modId : String = ExampleMod.id
    private val itemList = Items
    private val screens = HashMap<Identifier, Screen>()
    private val blockEntityRenderers = HashMap<Class<out VanillaBLockEntity>, () -> BlockEntityRenderer<out VanillaBLockEntity>>()

    // Screens
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

    // BlockEntityRenders
    fun registerBlockEntityRenderer(blockEntityClass: Class<out VanillaBLockEntity>, blockEntityRenderer: () -> BlockEntityRenderer<out VanillaBLockEntity>){
        if (blockEntityRenderers.putIfAbsent(blockEntityClass, blockEntityRenderer) != null){
            ExampleMod.logger.error("Block entity renderer for $blockEntityClass already exists!")
        }
    }
    private fun bindBlockEntityRenderer(blockEntityClass: Class<out VanillaBLockEntity>, blockEntityRenderer: () -> BlockEntityRenderer<out VanillaBLockEntity>){
        BlockEntityRendererRegistry.INSTANCE.register(blockEntityClass, blockEntityRenderer())
    }

    // Dyeable items
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
        registerDyeableItem(VanillaItems.ELYTRA)

        for (i in itemList) {
            if (i.value is DyeableItem) {
                registerDyeableItem(i.value)
            }
        }
        for (i in screens){
            registerScreenFactory(i.key,i.value)
        }
        for (i in blockEntityRenderers){
            bindBlockEntityRenderer(i.key,i.value)
        }
    }
}