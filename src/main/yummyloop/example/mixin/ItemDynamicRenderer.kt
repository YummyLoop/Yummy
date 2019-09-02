package yummyloop.example.mixin

import net.minecraft.item.ItemStack
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import net.minecraft.client.render.item.ItemDynamicRenderer as VanillaItemDynamicRenderer
import yummyloop.example.item.ItemDynamicRenderer as CustomItemDynamicRenderer

@Mixin(VanillaItemDynamicRenderer::class)
class ItemDynamicRenderer {
    @Inject(at = [At("HEAD")], method = ["render"], cancellable = true)
    private fun onRender(stack: ItemStack, info: CallbackInfo) {
        CustomItemDynamicRenderer.render(stack, info)
    }
}
