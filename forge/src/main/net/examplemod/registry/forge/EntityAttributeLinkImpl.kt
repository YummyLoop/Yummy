package net.examplemod.registry.forge

import net.examplemod.ExampleMod
import net.examplemod.registry.EntityAttributeLink
import net.minecraft.entity.attribute.DefaultAttributeRegistry
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent

object EntityAttributeLinkImpl {

    internal var iAttributeList: MutableList<EntityAttributeLink.AttributeListItem> = mutableListOf()

    @Suppress("UNUSED_PARAMETER")
    @JvmStatic
    fun linkEntityAttributes(
        attributeList: MutableList<EntityAttributeLink.AttributeListItem>,
    ): Unit {
        this.iAttributeList = attributeList
    }

    @Mod.EventBusSubscriber(modid = ExampleMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    object EntityAttributes {
        @SubscribeEvent
        fun registerEntityAttributes(event: FMLCommonSetupEvent) {
            event.enqueueWork {
                for (i in iAttributeList) {
                    DefaultAttributeRegistry.put(i.entityType.get(), i.builder.get().build())
                }
            }
        }
    }
}
