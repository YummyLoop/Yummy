package net.examplemod.items

import me.shedaniel.architectury.registry.CreativeTabs
import net.examplemod.ExampleMod
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

object YtemGroup {
    // Registering a new creative tab
    var EXAMPLE_TAB: ItemGroup =
        CreativeTabs.create(Identifier(ExampleMod.MOD_ID, "example_tab")) { ItemStack(Ytems.EXAMPLE_ITEM.get()) }
}