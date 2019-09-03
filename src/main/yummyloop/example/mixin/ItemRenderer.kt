package yummyloop.example.mixin

import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.item.ItemRenderer as VanillaItemRenderer
import net.minecraft.item.ItemStack
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import yummyloop.example.item.ItemRenderer as CustomItemRenderer

@Mixin(VanillaItemRenderer::class)
class ItemRenderer {
    @Inject(at = [At("HEAD")], method = ["renderItemAndGlow"], cancellable = true)
    private fun onRender(stack: ItemStack, bakedModel: BakedModel, info: CallbackInfo) {
        CustomItemRenderer.render(stack, bakedModel, info)
    }
}
