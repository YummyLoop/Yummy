package yummyloop.example.mixin

import net.minecraft.client.color.item.ItemColors
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.BakedQuad
import net.minecraft.item.DyeableItem
import net.minecraft.client.render.item.ItemRenderer as VanillaItemRenderer
import net.minecraft.item.ItemStack
import net.minecraft.resource.SynchronousResourceReloadListener
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import yummyloop.example.item.BuiltInItemModel

@Mixin(VanillaItemRenderer::class)
abstract class ItemRenderer : SynchronousResourceReloadListener {
    @Shadow
    private var colorMap: ItemColors? = null

    @Shadow
    abstract fun renderQuad(bufferBuilder_1: BufferBuilder, bakedQuad_1: BakedQuad, int_1: Int)

    @Inject(at = [At("HEAD")], method = ["renderItemAndGlow"], cancellable = true)
    private fun onRender(stack: ItemStack, bakedModel: BakedModel, info: CallbackInfo) {
        if (stack.item is BuiltInItemModel){
            (stack.item as BuiltInItemModel).render(stack,bakedModel)
            info.cancel()
        }
    }

    @Inject(at = [At("HEAD")], method = ["renderQuads"], cancellable = true)
    private fun onRenderQuads(bufferBuilder: BufferBuilder, bakedQuads: List<BakedQuad>, color0: Int, stack: ItemStack, info: CallbackInfo) {
        val initialColor: Int

        if(stack.item is DyeableItem) {
            initialColor = (stack.item as DyeableItem).getColor(stack)
        } else {
            return
        }

        val notEmpty = initialColor == -1 && !stack.isEmpty

        for (bakedQuad in bakedQuads) {
            var color = initialColor
            if (notEmpty && bakedQuad.hasColor()) {
                color = this.colorMap!!.getColorMultiplier(stack, bakedQuad.colorIndex)
                color = color or -16777216
            }
            this.renderQuad(bufferBuilder, bakedQuad, color)
        }

        info.cancel()
    }
}
