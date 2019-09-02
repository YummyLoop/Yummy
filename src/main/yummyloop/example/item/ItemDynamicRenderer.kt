package yummyloop.example.item

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.entity.model.ShieldEntityModel
import net.minecraft.item.ItemStack
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import yummyloop.example.item.backpack.Backpack
import net.minecraft.item.Items as VanillaItems

@Environment(EnvType.CLIENT)
object ItemDynamicRenderer {
    //val list =

    fun render(stack: ItemStack, info: CallbackInfo){
        // Todo: Add custom item renders here
        when (stack.item){
            is Backpack ->  ShieldEntityModel().renderItem()
            else -> {
                return
            }
        }
        //BlockEntityRenderDispatcher.INSTANCE.renderEntity(EnderChestBlockEntity())
        info.cancel()
    }
}

// Vanilla Check
/*when (stack.item) {
    is BannerItem -> return
    is BedItem -> return
    VanillaItems.SHIELD -> return
    VanillaItems.TRIDENT -> return
    Blocks.ENDER_CHEST.asItem() -> return
    Blocks.TRAPPED_CHEST.asItem() -> return

    is BlockItem -> when((stack.item as BlockItem).block){
        is AbstractSkullBlock -> return
        is ShulkerBoxBlock -> return
        Blocks.CONDUIT -> return

        // Vanilla default when not one of the above
        is ChestBlock -> return
    }
}*/