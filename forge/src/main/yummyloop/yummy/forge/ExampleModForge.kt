package yummyloop.yummy.forge

import me.shedaniel.architectury.platform.forge.EventBuses
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import yummyloop.yummy.ExampleMod
import yummyloop.yummy.ExampleMod.onInitialize
import yummyloop.yummy.integration.geckolib.forge.GeckoUtilsImpl
import thedarkcolour.kotlinforforge.KotlinModLoadingContext.Companion.get as KotlinModLoadingContext

@Mod(ExampleMod.MOD_ID)
object ExampleModForge {
    init {
        // Submit our event bus to let architectury register our content on the right time
        //EventBuses.registerModEventBus(ExampleMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        EventBuses.registerModEventBus(ExampleMod.MOD_ID, KotlinModLoadingContext().getKEventBus())
        onInitialize()
    }

    @Mod.EventBusSubscriber(modid = ExampleMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    object EventStuff {
        @Suppress("UNUSED_PARAMETER")
        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        fun onInitializeClient(event: FMLClientSetupEvent) {
            GeckoUtilsImpl.registerAll()
            ExampleMod.onInitializeClient()
        }
    }
}