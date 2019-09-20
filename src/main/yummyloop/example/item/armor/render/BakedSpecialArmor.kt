package yummyloop.example.item.armor.render

import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.BakedModelManager
import net.minecraft.client.render.model.BakedQuad
import net.minecraft.client.render.model.json.JsonUnbakedModel
import net.minecraft.client.render.model.json.ModelItemPropertyOverrideList
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.ExtendedBlockView
import yummyloop.example.ExampleMod
import yummyloop.example.item.armor.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import java.util.function.Supplier


class BakedSpecialArmor(private val name: String) : BakedModel, FabricBakedModel {
    companion object{
        private val bakedModelManager: BakedModelManager = MinecraftClient.getInstance().bakedModelManager

        fun fetchTransform(id : Identifier) : ModelTransformation? {
            return try {
                JsonUnbakedModel.deserialize(BufferedReader(InputStreamReader(MinecraftClient.getInstance().resourceManager.getResource(id).inputStream, Charsets.UTF_8))).transformations
            }catch (i : IOException){
                //println("Could not get file" + id.namespace + ":" + id.path + ".json" )
                ModelTransformation.NONE
            }
        }
    }
    private val bodyT = fetchTransform(Identifier(ExampleMod.id,"models/item/$name.body.json"))
    private val rArmT = fetchTransform(Identifier(ExampleMod.id,"models/item/$name.right.arm.json"))
    private val lArmT = fetchTransform(Identifier(ExampleMod.id,"models/item/$name.left.arm.json"))
    private val disp = fetchTransform(Identifier(ExampleMod.id,"models/item/$name.display.json"))
    private var trans = disp

    // Render as item
    override fun emitItemQuads(stack: ItemStack, randomSupplier: Supplier<Random>?, context: RenderContext) {
        trans = disp
        var ret = bakedModelManager.getModel(ModelIdentifier(Identifier(ExampleMod.id, "$name.display"), "inventory"))
        if (stack.orCreateTag.containsKey("model")) {
            when (stack.orCreateTag.getString("model")) {
                "body" -> {
                    trans = bodyT
                    ret = bakedModelManager.getModel((stack.item as ArmorWithBody).body)
                }
                "rightLeg" -> {
                    ret = bakedModelManager.getModel((stack.item as ArmorWithRightLeg).rightLeg)
                }
                "leftLeg" -> {
                    ret = bakedModelManager.getModel((stack.item as ArmorWithLeftLeg).leftLeg)
                }
                "rightArm" -> {
                    trans = rArmT
                    ret = bakedModelManager.getModel((stack.item as ArmorWithRightArm).rightArm)
                }
                "leftArm" -> {
                    trans = lArmT
                    ret = bakedModelManager.getModel((stack.item as ArmorWithLeftArm).leftArm)
                }
                else -> {
                    println("This should not happen at BakedSpecialArmor")
                }
            }
            context.fallbackConsumer().accept(ret)
        }else{
            trans = disp
            context.fallbackConsumer().accept(ret)
        }
    }

    // Render as block
    override fun emitBlockQuads(blockView: ExtendedBlockView?, state: BlockState?, pos: BlockPos?, randomSupplier: Supplier<Random>?, context: RenderContext?) {
        // Not a block
    }

    // Model texture
    override fun getSprite(): Sprite? {
        return null//MinecraftClient.getInstance().spriteAtlas.getSprite("missingno")
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

    override fun getTransformation(): ModelTransformation? {
        return this.trans
    }

    // isBuiltin = "Does it not load from json?"
    override fun isBuiltin(): Boolean {
        return false
    }

    override fun isVanillaAdapter(): Boolean {
        return false
    }
}