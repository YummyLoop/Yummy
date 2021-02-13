package net.examplemod

import me.shedaniel.architectury.registry.CreativeTabs
import me.shedaniel.architectury.registry.DeferredRegister
import me.shedaniel.architectury.registry.Registries
import me.shedaniel.architectury.registry.RegistrySupplier
import net.examplemod.config.clothconfig.AutoConfig
import net.minecraft.block.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object ExampleMod {
    const val MOD_ID = "yummy" // when changing this forge.toml needs to be changed too
    val log: Logger = LogManager.getLogger("Yummy")
    val modConfig = AutoConfig()

    // We can use this if we don't want to use DeferredRegister
    val REGISTRIES by lazyOf(Registries.get(MOD_ID))

    // Registering a new creative tab
    var EXAMPLE_TAB: ItemGroup =
        CreativeTabs.create(Identifier(MOD_ID, "example_tab")) { ItemStack(Blocks.COBBLESTONE) }

    // Registering a new item
    var ITEMS = DeferredRegister.create(MOD_ID, Registry.ITEM_KEY)
    var EXAMPLE_ITEM: RegistrySupplier<Item> =
        ITEMS.register("example_item") { Item(Item.Settings().group(EXAMPLE_TAB)) }

    fun init() {
        log.info("**************************")
        log.info("     YummY says hello!    ")
        log.info("**************************")
        log.error("logger error")
        log.warn("logger warn")
        log.fatal("logger fatal")

        ITEMS.register()
        println(ExampleExpectPlatform.getConfigDirectory().absolutePath)
    }
}
