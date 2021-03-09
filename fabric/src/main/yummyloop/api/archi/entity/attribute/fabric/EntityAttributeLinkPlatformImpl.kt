package yummyloop.api.archi.entity.attribute.fabric

import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import yummyloop.api.archi.entity.attribute.EntityAttributeLinkRegister

object EntityAttributeLinkPlatformImpl {
    /**
     * Registers a mod EntityAttributes links,
     * with platform specific implementation
     *
     * @param register A mod Entity Attribute Link register
     */
    @Suppress("UNUSED_PARAMETER")
    @JvmStatic
    fun register(register: EntityAttributeLinkRegister): Unit {
        for (l in register.getLinkList()) FabricDefaultAttributeRegistry.register(l.first.get(), l.second.get())
    }
}