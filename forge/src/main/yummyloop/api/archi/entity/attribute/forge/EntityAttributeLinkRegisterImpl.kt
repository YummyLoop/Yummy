@file:JvmMultifileClass

package yummyloop.api.archi.entity.attribute.forge

import net.minecraftforge.event.entity.EntityAttributeCreationEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import yummyloop.api.archi.entity.attribute.EntityAttributeLinkRegister
import yummyloop.yummy.ExampleMod

private class EntityAttributeLinkRegisterImpl {

    companion object {
        /** Private list of EntityAttributes links */
        var iAttributeList: MutableList<EntityAttributeLinkRegister.AttributeListItem> = mutableListOf()
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

object EntityAttributeLinkPlatformImpl {
    /**
     * Platform specific implementation of the link registry
     *
     * @param attributeList list of the links to register
     */
    @Suppress("UNUSED_PARAMETER")
    @JvmStatic
    fun linkEntityAttributes(
        attributeList: MutableList<EntityAttributeLinkRegister.AttributeListItem>,
    ): Unit {
        EntityAttributeLinkRegisterImpl.iAttributeList = attributeList
    }
}