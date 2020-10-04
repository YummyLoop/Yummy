package yummyloop.example.item;

import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.item.Items as Vanilla
import yummyloop.example.util.registry.RegistryManager
import java.util.function.Consumer
import net.minecraft.item.ItemGroup as VanillaItemGroup

object ItemGroup{
    private val map = HashMap<String, VanillaItemGroup>()
    private val defaultItemToIcon : Item = Vanilla.APPLE

    operator fun set(name: String, itemToIcon: ItemConvertible) {
        map[name] = RegistryManager.registerItemGroup(name).icon{ItemStack(itemToIcon)}.build()
    }

    operator fun set(name: String, itemToIcon: ItemConvertible, itemStacks: List<ItemStack>) {
        val fabricConsumer : Consumer<MutableList<ItemStack>> = Consumer { stacks -> stacks.addAll(itemStacks) }
        map[name] = RegistryManager.registerItemGroup(name).icon{ItemStack(itemToIcon)}.appendItems(fabricConsumer).build()
    }

    operator fun get(name: String) : VanillaItemGroup?{
        val value = map[name]
        return if (value == null){
            val ret = RegistryManager.registerItemGroup(name).icon{ItemStack(defaultItemToIcon)}.build()
            map[name] = ret
            ret
        }else{
            value
        }
    }
}