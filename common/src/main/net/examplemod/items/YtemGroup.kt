package net.examplemod.items

import me.shedaniel.architectury.registry.CreativeTabs
import net.examplemod.ExampleMod
import net.examplemod.ModContent
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

object YtemGroup {
    // Registering a new creative tab
    var EXAMPLE_TAB: ItemGroup =
        CreativeTabs.create(
            Identifier(ExampleMod.MOD_ID, "example_tab")) { ItemStack(ModContent.EXAMPLE_ITEM.get()) }

    internal object Dev {
        var devGroup: ItemGroup =
            CreativeTabs.create(
                Identifier(ExampleMod.MOD_ID, "dev_tab")) { ItemStack(ModContent.EXAMPLE_ITEM.get()) }
    }
}