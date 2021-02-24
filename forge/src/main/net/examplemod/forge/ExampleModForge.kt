package net.examplemod.forge

import me.shedaniel.architectury.platform.forge.EventBuses
import net.examplemod.ExampleMod
import net.examplemod.ExampleMod.init
import net.examplemod.integration.geckolib.forge.GeckoUtilsImpl
import net.examplemod.registry.forge.UtilImpl
import net.minecraft.entity.attribute.DefaultAttributeRegistry
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import thedarkcolour.kotlinforforge.KotlinModLoadingContext.Companion.get as KotlinModLoadingContext

@Mod(ExampleMod.MOD_ID)
object ExampleModForge {
    init {
        // Submit our event bus to let architectury register our content on the right time
        //EventBuses.registerModEventBus(ExampleMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        EventBuses.registerModEventBus(ExampleMod.MOD_ID, KotlinModLoadingContext().getKEventBus())
        init()
    }

    @Mod.EventBusSubscriber(modid = ExampleMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    object EventStuff {
        @Suppress("UNUSED_PARAMETER")
        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        fun registerRenderers(event: FMLClientSetupEvent) {
            GeckoUtilsImpl.registerAll()
        }

        @SubscribeEvent
        fun registerEntityAttributes(event: FMLCommonSetupEvent) {
            event.enqueueWork {
                for (i in UtilImpl.attributeList) {
                    DefaultAttributeRegistry.put(i.entityType.get(), i.builder.get().build())
                }
            }
        }
    }
}