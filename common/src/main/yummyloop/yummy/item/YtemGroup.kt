package yummyloop.yummy.item

import dev.architectury.registry.CreativeTabRegistry
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import yummyloop.yummy.ExampleMod
import yummyloop.yummy.ModContent

object YtemGroup {
    // Registering a new creative tab
    var EXAMPLE_TAB: ItemGroup =
        CreativeTabRegistry.create(
            Identifier(ExampleMod.MOD_ID, "example_tab")) { ItemStack(ModContent.EXAMPLE_ITEM.get()) }

    internal object Dev {
        var devGroup: ItemGroup =
            CreativeTabRegistry.create(
                Identifier(ExampleMod.MOD_ID, "dev_tab")) { ItemStack(ModContent.EXAMPLE_ITEM.get()) }
    }
}