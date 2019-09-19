package yummyloop.example.util.registry

import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry
import net.fabricmc.fabric.api.client.model.ModelProviderContext
import net.fabricmc.fabric.api.client.model.ModelResourceProvider
import net.fabricmc.fabric.api.client.model.ModelVariantProvider
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry
import net.fabricmc.fabric.api.client.render.EntityRendererRegistry
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.color.item.ItemColorProvider
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.DyeableItem
import net.minecraft.item.ItemConvertible
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import net.minecraft.util.PacketByteBuf
import yummyloop.example.ExampleMod
import yummyloop.example.item.Items
import net.minecraft.client.render.model.UnbakedModel as VanillaUnbakedModel
import yummyloop.example.item.Spear
import yummyloop.example.item.models.SimpleBakedItemModel
import yummyloop.example.render.models.UnbakedModel
import java.util.function.Consumer
import net.minecraft.block.entity.BlockEntity as VanillaBLockEntity
import net.minecraft.item.Items as VanillaItems

typealias Screen = (Int, Identifier, PlayerEntity, PacketByteBuf) -> AbstractContainerScreen<*>

object ClientManager {
    private var modId : String = ExampleMod.id
    private val isClient : Boolean = FabricLoader.getInstance().environmentType == EnvType.CLIENT
    private val itemList = Items
    private val screens = HashMap<Identifier, Screen>()
    private val blockEntityRenderers = HashMap<Class<out VanillaBLockEntity>, () -> BlockEntityRenderer<out VanillaBLockEntity>>()

    // Screens
    fun registerScreen(id : String, screen : Screen){
        if (isClient) {
            if (screens.putIfAbsent(Identifier(modId, id), screen) != null) {
                ExampleMod.logger.error("Screen $id already exists!")
            }
        }
    }
    private fun registerScreenFactory(id : Identifier, screen : Screen){
        ScreenProviderRegistry.INSTANCE.registerFactory(id) {
            syncId, identifier, player, buf ->
            screen(syncId, identifier ,player, buf)
        }
    }

    // BlockEntityRenders
    fun registerBlockEntityRenderer(blockEntityClass: Class<out VanillaBLockEntity>, blockEntityRenderer: () -> BlockEntityRenderer<out VanillaBLockEntity>){
        if (isClient) {
            if (blockEntityRenderers.putIfAbsent(blockEntityClass, blockEntityRenderer) != null) {
                ExampleMod.logger.error("Block entity renderer for $blockEntityClass already exists!")
            }
        }
    }
    private fun bindBlockEntityRenderer(blockEntityClass: Class<out VanillaBLockEntity>, blockEntityRenderer: () -> BlockEntityRenderer<out VanillaBLockEntity>){
        BlockEntityRendererRegistry.INSTANCE.register(blockEntityClass, blockEntityRenderer())
    }

    // Dyeable items
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

    // Models
    private val modelList = HashMap<Identifier, String>()
    // ----------------------------------------------------------------------------------------------------------------
    // Register a model appender, which can request loading additional models.
    private fun appendRequestedModels(){
        if (modelList.size > 0) {
            ModelLoadingRegistry.INSTANCE.registerAppender { manager: ResourceManager, out: Consumer<ModelIdentifier> ->
                for (i in modelList) {
                    out.accept(ModelIdentifier(i.key, i.value))
                }
            }
        }
    }
    // request model -> requires a blockState
    fun requestModel(name : String, variant : String) : ModelIdentifier? {
        return if (isClient) {
            if (modelList.putIfAbsent(Identifier(this.modId, name), variant) != null) {
                ExampleMod.logger.error("Model $name # $variant already exists!")
            }
            ModelIdentifier(Identifier(this.modId, name), variant)
        }else{
            null
        }
    }
    // request item model
    fun requestModel(name : String) : ModelIdentifier? {
        return requestModel(name, "inventory")
    }
    /**
     * Model variant providers hooks the resolution of ModelIdentifiers.
    *  In vanilla, this is the part where a "minecraft:stone#normal" identifier triggers the loading of a
     * "minecraft:models/stone" model ( ModelResourceProvider handles the later step).
     * @see ModelResourceProvider
     */
    private val modelVariantList =  HashSet<(ModelIdentifier, ModelProviderContext) -> VanillaUnbakedModel?>()
    private fun appendModelVariantProviders() {
        ModelLoadingRegistry.INSTANCE.registerVariantProvider { manager: ResourceManager? ->
            ModelVariantProvider { modelId: ModelIdentifier, context: ModelProviderContext ->
                if (modelId.namespace == this.modId) {
                    var ret : VanillaUnbakedModel?= null
                    for (i in modelVariantList){
                        ret = i(modelId,context)
                        if (ret!=null) break
                    }
                    ret
                } else {
                    null
                }
            }
        }
    }
    fun registerModelVariant(v : (ModelIdentifier, ModelProviderContext) -> VanillaUnbakedModel?){
        if (isClient){
            modelVariantList.add(v)
        }
    }
    private fun <T : VanillaUnbakedModel?> modelResourceProvider(modelResource : (id: Identifier, context: ModelProviderContext) -> T){
        ModelLoadingRegistry.INSTANCE.registerResourceProvider { manager: ResourceManager? ->
            ModelResourceProvider(modelResource)
        }
    }

    // ----------------------------------------------------------------------------------------------------------------
    fun ini(){
        // Init items
        registerDyeableItem(VanillaItems.ELYTRA)

        for (i in itemList) {
            if (i.value is DyeableItem) {
                registerDyeableItem(i.value)
            }
        }
        for (i in screens){
            registerScreenFactory(i.key,i.value)
        }
        for (i in blockEntityRenderers){
            bindBlockEntityRenderer(i.key,i.value)
        }

        //---------Code below is temporary, and/or needs to be improved

        EntityRendererRegistry.INSTANCE.register(Spear.SpearEntity::class.java) { entityRenderDispatcher, context -> Spear.SpearEntityRenderer(entityRenderDispatcher) }


        requestModel("model", "custom")
        appendRequestedModels()

        registerModelVariant { modelId: ModelIdentifier, context: ModelProviderContext ->
            when {
                modelId.variant == "custom" -> {
                    //modelId.path =  model name
                    println("--- ModelVariantProvider called! ---")
                    context.loadModel(Identifier(this.modId,"custom"))
                }
                else -> null
            }
        }

        appendModelVariantProviders()


        modelResourceProvider{ id: Identifier, context: ModelProviderContext ->
            when {
                id.toString() == "example:custom" -> context.loadModel(Identifier(this.modId, "custom2"))
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