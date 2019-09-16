package yummyloop.example

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry
import net.fabricmc.fabric.api.client.model.ModelResourceProvider
import net.fabricmc.fabric.api.client.model.ModelVariantProvider
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry
import net.minecraft.client.color.item.ItemColorProvider
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.item.DyeableItem
import net.minecraft.item.ItemConvertible
import net.minecraft.util.Identifier
import yummyloop.example.block.Blocks
import yummyloop.example.item.Items
import yummyloop.example.item.models.SimpleBakedItemModel
import yummyloop.example.render.models.UnbakedModel
import yummyloop.example.render.HasClient
import net.fabricmc.fabric.api.client.render.EntityRendererRegistry
import yummyloop.example.item.Spear
import yummyloop.example.util.registry.ClientManager

object ExampleModClient : ClientModInitializer {
    override fun onInitializeClient() {

        ClientManager.ini()

        for (i in Blocks.entities) {
            if (i.value is HasClient) {
                (i.value as HasClient).client()
            }
        }

        EntityRendererRegistry.INSTANCE.register(Spear.SpearEntity::class.java) { entityRenderDispatcher, context -> Spear.SpearEntityRenderer(entityRenderDispatcher) }


        ModelLoadingRegistry.INSTANCE.registerAppender { manager, out->
            println("--- ModelAppender called! ---")
            out.accept(ModelIdentifier(ExampleMod.id,"model#custom")) }

        ModelLoadingRegistry.INSTANCE.registerVariantProvider { manager->
            ModelVariantProvider{
                modelId, context->
                if (modelId.variant == "custom" && modelId.namespace == ExampleMod.id) {
                    println("--- ModelVariantProvider called! ---")
                    context.loadModel(Identifier(ExampleMod.id, "custom"))
                } else {
                    null
                }
            }
        }

        ModelLoadingRegistry.INSTANCE.registerResourceProvider { manager->
            ModelResourceProvider{
                id, context->
                when {
                    id.toString() == "example:custom" -> context.loadModel(Identifier(ExampleMod.id, "custom2"))
                    id.toString() == "example:custom2" -> {
                        println("--- ModelResourceProvider called! ---")
                        UnbakedModel(
                                SimpleBakedItemModel(
                                        ModelIdentifier("example", "template_be2"),
                                        Identifier("example", "template_be2")
                                )
                        )
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
}