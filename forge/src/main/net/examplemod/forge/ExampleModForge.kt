package net.examplemod.forge

import me.shedaniel.architectury.platform.forge.EventBuses
import net.examplemod.ExampleMod
import net.examplemod.ExampleMod.init
import net.examplemod.integration.geckolib.forge.GeckoUtils
import net.minecraftforge.fml.common.Mod
import thedarkcolour.kotlinforforge.KotlinModLoadingContext.Companion.get as KotlinModLoadingContext

@Mod(ExampleMod.MOD_ID)
object ExampleModForge {
    init {
        // Submit our event bus to let architectury register our content on the right time
        //EventBuses.registerModEventBus(ExampleMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        EventBuses.registerModEventBus(ExampleMod.MOD_ID, KotlinModLoadingContext().getKEventBus())
        init()
        GeckoUtils.Items.registerAll()
    }
}