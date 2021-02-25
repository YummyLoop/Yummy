package net.examplemod.registry.forge

import net.examplemod.ExampleMod
import net.examplemod.registry.EntityAttributeLink
import net.minecraftforge.event.entity.EntityAttributeCreationEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

object EntityAttributeLinkImpl {

    /** Private list of EntityAttributes links */
    private var iAttributeList: MutableList<EntityAttributeLink.AttributeListItem> = mutableListOf()

    /**
     * Platform specific implementation of the link registry
     *
     * @param attributeList list of the links to register
     */
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
        fun registerEntityAttributes(event: EntityAttributeCreationEvent) {
            for (i in iAttributeList) {
                event.put(i.entityType.get(), i.builder.get().build())
            }
        }
    }
}
