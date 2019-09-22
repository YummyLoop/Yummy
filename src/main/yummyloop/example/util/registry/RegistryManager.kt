package yummyloop.example.util.registry

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.container.Container
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityCategory
import net.minecraft.entity.EntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Identifier
import net.minecraft.util.PacketByteBuf
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import yummyloop.example.block.Blocks
import yummyloop.example.config.Config
import yummyloop.example.item.ItemGroup
import yummyloop.example.item.Items
import java.util.function.Supplier
import net.minecraft.block.Block as VanillaBlock
import net.minecraft.item.Item as VanillaItem

typealias ContainerFactory = (Int, Identifier, PlayerEntity, PacketByteBuf) -> Container

object RegistryManager {
    private var modId : String = Config.modId
    private val blockList = Blocks
    private val itemList = Items
    private val itemGroupList = Items.groups
    val miscEntityType = HashMap<String, Pair<EntityType<out Entity>, (World, Double, Double, Double) -> Entity >>()

    // Item
    //-----------------------------------------------------------------------------------------------------------------
    fun <T : VanillaItem> register(item : T, modId : String, itemName : String) {
        itemList.putIfAbsent(itemName, item)
        Registry.register(Registry.ITEM, Identifier(modId, itemName), item)
    }
    fun <T : VanillaItem> register(item : T, itemName : String) {
        register(item, this.modId, itemName)
    }
    // Block
    //-----------------------------------------------------------------------------------------------------------------
    fun <T : VanillaBlock> register(block : T, modId : String, blockName : String) {
        blockList.putIfAbsent(blockName, block)
        Registry.register(Registry.BLOCK, Identifier(modId, blockName), block)
    }
    fun <T : VanillaBlock> register(block : T, blockName : String) {
        register(block, this.modId, blockName)
    }
    // ItemGroup
    //-----------------------------------------------------------------------------------------------------------------
    fun <T : ItemGroup> register(itemGroup : T, modId : String, itemGroupName : String) : FabricItemGroupBuilder{
        itemGroupList.putIfAbsent(itemGroupName, itemGroup)
        return FabricItemGroupBuilder.create(Identifier(modId, itemGroupName))
    }
    fun <T : ItemGroup> register(itemGroup : T, itemGroupName : String) : FabricItemGroupBuilder{
        return register(itemGroup, this.modId, itemGroupName)
    }
    // Containers
    //-----------------------------------------------------------------------------------------------------------------
    fun registerContainer(modId : String, containerName : String, containerFactory: ContainerFactory){
        ContainerProviderRegistry.INSTANCE.registerFactory(Identifier(modId, containerName), containerFactory)
    }
    fun registerContainer(containerName : String, containerFactory: ContainerFactory) {
        registerContainer(this.modId, containerName, containerFactory)
    }
    // Block entity
    //-----------------------------------------------------------------------------------------------------------------
    fun <M: BlockEntity, T : BlockEntityType<M>> register(blockEntityType : T, modId : String, blockName : String) {
        Registry.register(Registry.BLOCK_ENTITY, Identifier(modId, blockName), blockEntityType)
    }
    fun <M: BlockEntity, T : BlockEntityType<M>> register(blockEntityType : T, blockName : String) {
        register(blockEntityType, this.modId, blockName)
    }
    fun <T : BlockEntity> register(sup : Supplier<T>, blocks: List<VanillaBlock?>, blockName : String) {
        register(BlockEntityType.Builder.create(sup, *blocks.toTypedArray()).build(null)!!, this.modId, blockName)
    }
    // Entity type
    //-----------------------------------------------------------------------------------------------------------------
    fun <M : Entity> registerEntityType(modId: String, name: String, category: EntityCategory, function: (EntityType<M>, World) -> M ): EntityType<M> {
        return Registry.register(
                Registry.ENTITY_TYPE,
                Identifier (modId, name),
                FabricEntityTypeBuilder.create(category){ entity: EntityType<M>, world : World -> function(entity, world)}.build())
    }
    fun <M : Entity> registerEntityType(name: String, category: EntityCategory, function: (EntityType<M>, World) -> M ): EntityType<M> {
        return registerEntityType(this.modId, name, category, function)
    }
    fun <M : Entity> registerMiscEntityType(modId: String, name: String, function: (EntityType<M>, World) -> M, worldFunction : (World, Double, Double, Double) -> Entity ): EntityType<M> {
        val ret = registerEntityType(this.modId, name, EntityCategory.MISC, function)
        miscEntityType[name] = Pair(ret, worldFunction)
        return ret
    }
    fun <M : Entity> registerMiscEntityType(name: String, function: (EntityType<M>, World) -> M, worldFunction : (World, Double, Double, Double) -> Entity ): EntityType<M> {
        return registerMiscEntityType(this.modId, name, function, worldFunction)
    }
}