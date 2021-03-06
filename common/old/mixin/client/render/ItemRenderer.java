package yummyloop.yummy.old.mixin.client.render;

import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.SynchronousResourceReloadListener;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yummyloop.yummy.old.item.models.builtIn.BuiltInItemModel;

@Mixin({net.minecraft.client.render.item.ItemRenderer.class})
public abstract class ItemRenderer implements SynchronousResourceReloadListener {
    @Final
    @Shadow
    private ItemColors colorMap;

    @Shadow
    protected abstract void renderQuad(@NotNull BufferBuilder var1, @NotNull BakedQuad var2, int var3);

    @Shadow
    protected abstract void renderModel(@NotNull BakedModel var1, int var2, @NotNull ItemStack var3);

    @Inject(
            at = {@At("HEAD")},
            method = "renderItemAndGlow",
            cancellable = true
    )
    private void onRender(ItemStack stack, BakedModel bakedModel, CallbackInfo info) {
        if (stack.getItem() instanceof BuiltInItemModel) {
            Item var10000 = stack.getItem();

            ((BuiltInItemModel)var10000).render(stack, bakedModel);
            info.cancel();
        }
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
