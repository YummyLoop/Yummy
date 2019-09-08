package yummyloop.example.mixin.render

import net.minecraft.client.color.item.ItemColors
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.BakedQuad
import net.minecraft.client.render.item.ItemRenderer as VanillaItemRenderer
import net.minecraft.item.ItemStack
import net.minecraft.resource.SynchronousResourceReloadListener
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import yummyloop.example.item.models.BuiltInItemModel

@Mixin(VanillaItemRenderer::class)
abstract class ItemRenderer : SynchronousResourceReloadListener {
    @Shadow
    private var colorMap: ItemColors? = null

    @Shadow
    abstract fun renderQuad(bufferBuilder_1: BufferBuilder, bakedQuad_1: BakedQuad, int_1: Int)

    @Shadow
    abstract fun renderModel(bakedModel_1: BakedModel, int_1: Int, itemStack_1: ItemStack)

    @Inject(at = [At("HEAD")], method = ["renderItemAndGlow"], cancellable = true)
    private fun onRender(stack: ItemStack, bakedModel: BakedModel, info: CallbackInfo) {
        if (stack.item is BuiltInItemModel){
            (stack.item as BuiltInItemModel).render(stack,bakedModel)
            info.cancel()
        }
    }

    /* //Redundant implementation minecraft already does it with tintindex
    @Inject(at = [At("HEAD")], method = ["renderItemModel"], cancellable = true)
    private fun onRenderItemModel(bakedModel : BakedModel, stack : ItemStack, info: CallbackInfo) {
        val color: Int

        if(stack.item is DyeableItem) {
            color = (stack.item as DyeableItem).getColor(stack)
        } else {
            return
        }

        this.renderModel(bakedModel, color, stack)

        info.cancel()
    }*/
}
