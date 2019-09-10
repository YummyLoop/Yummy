package yummyloop.example

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry
import net.fabricmc.fabric.api.client.model.ModelResourceProvider
import net.fabricmc.fabric.api.client.model.ModelVariantProvider
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry
import net.fabricmc.fabric.api.event.client.ClientTickCallback
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.color.item.ItemColorProvider
import net.minecraft.client.render.model.*
import net.minecraft.client.render.model.json.ModelItemPropertyOverrideList
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.item.DyeableItem
import net.minecraft.item.ItemConvertible
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import yummyloop.example.block.Blocks
import yummyloop.example.item.Items
import yummyloop.example.render.HasClient
import java.util.*
import java.util.function.Function as JavaFunction


class ExampleModClient : ClientModInitializer {
    companion object {
        var bakedModel: BakedModel? = null
    }

    override fun onInitializeClient() {
        // Init items
        for (i in Items) {
            if (i.value is HasClient) {
                (i.value as HasClient).client()
            }
            if (i.value is DyeableItem) {
                registerDyeableItem(i.value)
            }
        }

        for (i in Items.containers) {
            if (i.value is HasClient) {
                (i.value as HasClient).client()
            }
        }

        for (i in Blocks.entities) {
            if (i.value is HasClient) {
                (i.value as HasClient).client()
            }
        }


        ModelLoadingRegistry.INSTANCE.registerAppender { manager, out->
            println("--- ModelAppender called! ---")
            out.accept(ModelIdentifier("fabric:model#custom")) }

        ModelLoadingRegistry.INSTANCE.registerVariantProvider { manager->
            ModelVariantProvider{
                modelId, context->
                if (modelId.variant == "custom" && modelId.namespace == "fabric") {
                    println("--- ModelVariantProvider called! ---")
                    //context.loadModel(Identifier("fabric:custom"))
                    context.loadModel(Identifier("example:item/hat2"))
                } else {
                    null
                }
            }
        }

        ModelLoadingRegistry.INSTANCE.registerResourceProvider { manager->
            ModelResourceProvider{
                id, context->
                when {
                    id.toString() == "fabric:custom" -> context.loadModel(Identifier("fabric:custom2"))
                    //id.toString() == "fabric:custom2" -> {
                    id.toString() == "example:item/hat2" -> {
                        println("--- ModelResourceProvider called! ---")
                        object : UnbakedModel {

                            override fun getModelDependencies():Collection<Identifier> {

                                return Collections.emptyList()
                            }

                            override fun getTextureDependencies(var1:JavaFunction<Identifier, UnbakedModel>, var2:Set<String>):Collection<Identifier> {
                                return Collections.emptyList()
                            }

                            override fun bake(var1:ModelLoader, var2:JavaFunction<Identifier, Sprite>, var3:ModelBakeSettings): BakedModel? {
                                println("--- Model baked! ---")

                                bakedModel = object:BakedModel {
                                    override fun getQuads(var1: BlockState?, var2: Direction?, var3: Random?): MutableList<BakedQuad>? {
                                        return Collections.emptyList()
                                    }

                                    override fun useAmbientOcclusion():Boolean {
                                        return false
                                    }

                                    override fun hasDepthInGui():Boolean {
                                        return false
                                    }

                                    override fun isBuiltin():Boolean {
                                        return false
                                    }

                                    override fun getSprite():Sprite {
                                        return MinecraftClient.getInstance().spriteAtlas.getSprite("example:item/hat")
                                    }

                                    override fun getTransformation():ModelTransformation {
                                        return ModelTransformation.NONE
                                    }

                                    override fun getItemPropertyOverrides():ModelItemPropertyOverrideList {
                                        return ModelItemPropertyOverrideList.EMPTY
                                    }
                                }
                                return bakedModel
                            }
                        }
                    }
                    else -> null
                }
            }
        }

        /* // Check when model loads
        ClientTickCallback.EVENT.register(
                ClientTickCallback{
                    client->
                    try {
                        if (client.bakedModelManager.getModel(ModelIdentifier("fabric:model#custom")) === bakedModel && bakedModel != null) {
                            println("--- MODEL LOADED! ---")
                        } else {
                            println("--- MODEL NOT LOADED! ---")
                        }
                    } catch (e : NullPointerException){
                        println("--- MODEL NOT LOADED! + null ---")
                    }
                }
        )*/



    }

    private fun registerDyeableItem (item : ItemConvertible){
        ColorProviderRegistry.ITEM.register(// json model requires a "tintindex" while 2d uses the texture layer
                ItemColorProvider { itemStack, layer ->
                    if(layer != 0){
                        -1
                    }else{
                        val color = (itemStack.item as DyeableItem).getColor(itemStack)
                        if (color == 10511680) { // Default color when color was not found
                            -1
                        }else{
                            color
                        }
                    }
                },
                item
        )
    }
}