package yummyloop.example.render.models

import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.BakedModelManager
import net.minecraft.client.render.model.BakedQuad
import net.minecraft.client.render.model.json.ModelItemPropertyOverrideList
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.ExtendedBlockView
import java.util.*
import java.util.function.Supplier


class SimpleBakedItemModel(private val modelIdentifier : ModelIdentifier, private val sprite : Identifier) : BakedModel, FabricBakedModel {
    companion object{
        private val bakedModelManager: BakedModelManager = MinecraftClient.getInstance().bakedModelManager
    }

    // Render as item
    override fun emitItemQuads(stack: ItemStack, randomSupplier: Supplier<Random>?, context: RenderContext) {
        context.fallbackConsumer().accept(bakedModelManager.getModel(modelIdentifier))
    }

    // Render as block
    override fun emitBlockQuads(blockView: ExtendedBlockView?, state: BlockState?, pos: BlockPos?, randomSupplier: Supplier<Random>?, context: RenderContext?) {
        // Not a block
    }

    // Model texture
    override fun getSprite(): Sprite {
        return MinecraftClient.getInstance().spriteAtlas.getSprite(sprite)
    }

    // BlockStates / item overrides
    override fun getItemPropertyOverrides(): ModelItemPropertyOverrideList {
        return ModelItemPropertyOverrideList.EMPTY
    }

    override fun getQuads(var1: BlockState?, var2: Direction?, var3: Random?): MutableList<BakedQuad> {
        return Collections.emptyList()
    }

    override fun useAmbientOcclusion(): Boolean {
        return false
    }

    override fun hasDepthInGui(): Boolean {
        return false
    }

    override fun getTransformation(): ModelTransformation {
        return ModelTransformation.NONE
    }

    // isBuiltin = "Does it not load from json?"
    override fun isBuiltin(): Boolean {
        return false
    }

    override fun isVanillaAdapter(): Boolean {
        return false
    }
}