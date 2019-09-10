package yummyloop.example.util.registry

import net.minecraft.block.Block as VanillaBlock
import net.minecraft.item.Item as VanillaItem
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import yummyloop.example.ExampleMod
import yummyloop.example.item.Items

object RegistryManager {
    private var modId : String = ExampleMod.id
    private val itemList = Items

    // Item
    fun <T : VanillaItem> register(item : T, modId : String, itemName : String) {
        Registry.register(Registry.ITEM, Identifier(modId, itemName), item)
        itemList.putIfAbsent(itemName, item)
    }
    fun <T : VanillaItem> register(item : T, itemName : String) {
        register(item, this.modId, itemName)
    }
    // Block
    fun <T : VanillaBlock> register(block : T, modId : String, blockName : String) {
        Registry.register(Registry.BLOCK, Identifier(modId, blockName), block)
    }
    fun <T : VanillaBlock> register(block : T, blockName : String) {
        register(block, this.modId, blockName)
    }
}