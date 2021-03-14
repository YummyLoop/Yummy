package yummyloop.api.archi.entity.attribute.forge

import net.minecraftforge.event.entity.EntityAttributeCreationEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import yummyloop.api.archi.entity.attribute.EntityAttributeLinkRegister
import yummyloop.yummy.ExampleMod

object EntityAttributeLinkPlatformImpl {
    /** Private list of EntityAttributes links */
    private var attributeLinkList: MutableList<EntityAttributeLinkRegister> = mutableListOf()

    /**
     * Registers a mod EntityAttributes links,
     * with platform specific implementation
     *
     * @param register A mod Entity Attribute Link register
     */
    @Suppress("UNUSED_PARAMETER")
    @JvmStatic
    fun register(register: EntityAttributeLinkRegister): Unit {
        attributeLinkList.add(register)
    }

    @Mod.EventBusSubscriber(modid = ExampleMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    private object EntityAttributes {
        @SubscribeEvent
        fun registerEntityAttributes(event: EntityAttributeCreationEvent) {
            for (i in attributeLinkList) for (l in i.getLinkList()) event.put(l.first.get(), l.second.build())
        }
    }
}