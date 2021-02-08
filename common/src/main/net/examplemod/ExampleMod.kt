package net.examplemod;

import me.shedaniel.architectury.registry.*
import net.minecraft.block.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object ExampleMod {
    const val MOD_ID = "yummy" // when changing this forge.toml needs to be changed too

    // We can use this if we don't want to use DeferredRegister
    val REGISTRIES by lazyOf( Registries.get(MOD_ID))

    // Registering a new creative tab
    var EXAMPLE_TAB = CreativeTabs.create(Identifier(MOD_ID, "example_tab")) { -> ItemStack(Blocks.COBBLESTONE) }

    // Registering a new item
    var ITEMS = DeferredRegister.create(MOD_ID, Registry.ITEM_KEY)
    var EXAMPLE_ITEM = ITEMS.register("example_item") { -> Item(Item.Settings().group(EXAMPLE_TAB))}
    
    fun init() {
        ITEMS.register()
        println(ExampleExpectPlatform.getConfigDirectory().absolutePath)
        println("Kotlin says hello !!!")
    }
}
