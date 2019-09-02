package yummyloop.example.item

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.item.ItemStack
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

object ItemDynamicRenderer {
    val list = mutableListOf<Any>()

    @Environment(EnvType.CLIENT)
    fun render(stack: ItemStack, info: CallbackInfo){
        val i : Int = list.indexOf(stack.item)
        if(i > -1) { // -1 if not found
            (list[i] as Rend).render(stack)
        } else {
            return
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