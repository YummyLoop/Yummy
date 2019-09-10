package yummyloop.example.util.registry

import net.minecraft.item.Item as VanillaItem
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import yummyloop.example.ExampleMod

object RegistryManager {
    var modId : String = ExampleMod.id


    fun <T : VanillaItem> register(item : T, modId : String, itemName : String) {
        Registry.register(Registry.ITEM, Identifier(modId, itemName), item)
    }
    fun <T : VanillaItem> register(item : T, itemName : String) {
        register(item, this.modId, itemName)
    }
}