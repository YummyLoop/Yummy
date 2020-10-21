package yummyloop.example.item;

import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.item.Items as Vanilla
import yummyloop.example.registry.RegistryManager
import java.util.function.Consumer
import net.minecraft.item.ItemGroup as VanillaItemGroup

object ItemGroup{
    private val map = HashMap<String, VanillaItemGroup>()
    private val defaultItemToIcon : Item = Vanilla.APPLE

    /**
     * Creates a ItemGroup
     *
     * @param name ItemGroup name to create
     * @param itemToIcon Item to use as ItemGroup image
     */
    operator fun set(name: String, itemToIcon: ItemConvertible) {
        map[name] = RegistryManager.registerItemGroup(name).icon{ItemStack(itemToIcon)}.build()
    }

    /**
     * Creates a ItemGroup
     *
     * @param name ItemGroup name to create
     * @param itemToIcon Item to use as ItemGroup image
     * @param itemStacks list of items to include in the ItemGroup
     */
    operator fun set(name: String, itemToIcon: ItemConvertible, itemStacks: List<ItemStack>) {
        val fabricConsumer : Consumer<MutableList<ItemStack>> = Consumer { stacks -> stacks.addAll(itemStacks) }
        map[name] = RegistryManager.registerItemGroup(name).icon{ItemStack(itemToIcon)}.appendItems(fabricConsumer).build()
    }

    /**
     * Gets an existing ItemGroup or creates one if it does not exist
     *
     * @param name ItemGroup name
     */
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