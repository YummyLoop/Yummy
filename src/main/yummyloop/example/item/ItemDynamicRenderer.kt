package yummyloop.example.item

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.AbstractSkullBlock
import net.minecraft.block.Blocks
import net.minecraft.block.ChestBlock
import net.minecraft.block.ShulkerBoxBlock
import net.minecraft.block.entity.EnderChestBlockEntity
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.item.BannerItem
import net.minecraft.item.BedItem
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import net.minecraft.item.Items as VanillaItems

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
        BlockEntityRenderDispatcher.INSTANCE.renderEntity(EnderChestBlockEntity())
        info.cancel()

    }
}