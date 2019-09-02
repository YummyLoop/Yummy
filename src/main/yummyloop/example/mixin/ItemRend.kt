package yummyloop.example.mixin

import net.minecraft.block.entity.ChestBlockEntity
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.client.render.item.ItemDynamicRenderer
import net.minecraft.item.ItemStack
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(ItemDynamicRenderer::class)
class ItemRend {
    @Inject(at = [At("HEAD")], method = ["render"], cancellable = true)
    private fun onRender(stack: ItemStack, info: CallbackInfo) {

        BlockEntityRenderDispatcher.INSTANCE.renderEntity(ChestBlockEntity())
        info.cancel()
    }
}
