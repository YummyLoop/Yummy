package yummyloop.example.item

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.*
import net.minecraft.block.entity.ChestBlockEntity
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.item.*
import net.minecraft.item.BlockItem
import net.minecraft.item.Items as VanillaItems
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Environment(EnvType.CLIENT)
object ItemDynamicRenderer {
    val defaultRenders = listOf(BannerItem::class)
    fun render(stack: ItemStack, info: CallbackInfo){

        // Don't render vanilla stuff
        when (stack.item) {
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
        }

        // Todo: Add custom item renders here
        BlockEntityRenderDispatcher.INSTANCE.renderEntity(ChestBlockEntity())
        info.cancel()

    }
}