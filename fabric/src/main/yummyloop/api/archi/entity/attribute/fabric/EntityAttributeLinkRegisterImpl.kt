@file:JvmMultifileClass

package yummyloop.api.archi.entity.attribute.fabric

import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import yummyloop.api.archi.entity.attribute.EntityAttributeLinkRegister

private class EntityAttributeLinkRegisterImpl {}

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
        for (i in attributeList) {
            FabricDefaultAttributeRegistry.register(i.entityType.get(), i.builder.get())
        }
    }
}