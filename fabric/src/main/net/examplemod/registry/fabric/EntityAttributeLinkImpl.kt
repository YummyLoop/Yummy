package net.examplemod.registry.fabric

import net.examplemod.registry.EntityAttributeLink
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry

object EntityAttributeLinkImpl {

    @Suppress("UNUSED_PARAMETER")
    @JvmStatic
    fun linkEntityAttributes(
        attributeList: MutableList<EntityAttributeLink.AttributeListItem>,
    ): Unit {
        for (i in attributeList) {
            FabricDefaultAttributeRegistry.register(i.entityType.get(), i.builder.get())
        }
    }
}