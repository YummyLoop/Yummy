package net.examplemod

import me.shedaniel.architectury.platform.Platform
import me.shedaniel.architectury.registry.CreativeTabs
import me.shedaniel.architectury.registry.Registries
import net.examplemod.config.clothconfig.AutoConfig
import net.examplemod.integration.geckolib.GeckoUtils
import net.examplemod.integration.geckolib.JackInTheBoxItem2
import net.examplemod.items.Ytem
import net.fabricmc.api.EnvType
import net.minecraft.block.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import software.bernie.geckolib3.GeckoLib

object ExampleMod {
    const val MOD_ID = "yummy" // when changing this forge.toml needs to be changed too
    val log: Logger = LogManager.getLogger("Yummy")
    val modConfig = AutoConfig()

    // Registering a new creative tab
    var EXAMPLE_TAB: ItemGroup =
        CreativeTabs.create(Identifier(MOD_ID, "example_tab")) { ItemStack(Blocks.COBBLESTONE) }

    // We can use this if we don't want to use DeferredRegister
    val REGISTRIES by lazyOf(Registries.get(MOD_ID))
    var lazyItems = REGISTRIES.get(Registry.ITEM_KEY)
    var lazyItem =
        lazyItems.registerSupplied(Identifier(MOD_ID, "example_lazy_item"), ::Ytem)

    // Registering a new item
    //var ITEMS = DeferredRegister.create(MOD_ID, Registry.ITEM_KEY)
    var EXAMPLE_ITEM = Ytem.register("example_item") { Ytem(Ytem.Settings().group(EXAMPLE_TAB)) }

    // Gecko
    var JACK_IN_THE_BOX2 = GeckoUtils.Items.register("jack", ::JackInTheBoxItem2, Item.Settings().group(EXAMPLE_TAB))
    //var JACK_IN_THE_BOX2  = ITEMS.register("jack") { JackInTheBoxItem2(Item.Settings().group(EXAMPLE_TAB)) }

    fun init() {
        log.info("**************************")
        log.info("     YummY says hello!    ")
        log.info("**************************")
        //log.error("logger error")
        //log.warn("logger warn")
        //log.fatal("logger fatal")

        GeckoLib.initialize()
        Ytem.register()
        println(ExampleExpectPlatform.getConfigDirectory().absolutePath)

        if (Platform.getEnv() == EnvType.CLIENT) {
            println("Its client")
        }
    }
}
