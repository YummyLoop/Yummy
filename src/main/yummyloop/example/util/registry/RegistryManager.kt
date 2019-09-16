package yummyloop.example.util.registry

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.container.Container
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Identifier
import net.minecraft.util.PacketByteBuf
import net.minecraft.util.registry.Registry
import yummyloop.example.ExampleMod
import yummyloop.example.block.Blocks
import yummyloop.example.item.ItemGroup
import yummyloop.example.item.Items
import net.minecraft.block.Block as VanillaBlock
import net.minecraft.item.Item as VanillaItem

typealias ContainerFactory = (Int, Identifier, PlayerEntity, PacketByteBuf) -> Container

object RegistryManager {
    private var modId : String = ExampleMod.id
    private val blockList = Blocks
    private val itemList = Items
    private val itemGroupList = Items.groups

    // Item
    fun <T : VanillaItem> register(item : T, modId : String, itemName : String) {
        itemList.putIfAbsent(itemName, item)
        Registry.register(Registry.ITEM, Identifier(modId, itemName), item)
    }
    fun <T : VanillaItem> register(item : T, itemName : String) {
        register(item, this.modId, itemName)
    }
    // Block
    fun <T : VanillaBlock> register(block : T, modId : String, blockName : String) {
        blockList.putIfAbsent(blockName, block)
        Registry.register(Registry.BLOCK, Identifier(modId, blockName), block)
    }
    fun <T : VanillaBlock> register(block : T, blockName : String) {
        register(block, this.modId, blockName)
    }
    // ItemGroup
    fun <T : ItemGroup> register(itemGroup : T, modId : String, itemGroupName : String) : FabricItemGroupBuilder{
        itemGroupList.putIfAbsent(itemGroupName, itemGroup)
        return FabricItemGroupBuilder.create(Identifier(modId, itemGroupName))
    }
    fun <T : ItemGroup> register(itemGroup : T, itemGroupName : String) : FabricItemGroupBuilder{
        return register(itemGroup, this.modId, itemGroupName)
    }
    // Containers
    fun registerContainer(modId : String, containerName : String, containerFactory: ContainerFactory){
        ContainerProviderRegistry.INSTANCE.registerFactory(Identifier(modId, containerName), containerFactory)
    }
    fun registerContainer(containerName : String, containerFactory: ContainerFactory) {
        registerContainer(this.modId, containerName, containerFactory)
    }
}