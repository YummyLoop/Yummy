@file:JvmMultifileClass

package yummyloop.api.archi.entity.attribute

import me.shedaniel.architectury.annotations.ExpectPlatform
import me.shedaniel.architectury.registry.RegistrySupplier
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.DefaultAttributeContainer
import java.util.function.Supplier

class EntityAttributeLinkRegister private constructor() {

    companion object {
        /** Private list of EntityAttributes links */
        private var attributeList: MutableList<AttributeListItem> = mutableListOf()

        fun create(): EntityAttributeLinkRegister = EntityAttributeLinkRegister()
    }

    data class AttributeListItem(
        var entityType: RegistrySupplier<out EntityType<out LivingEntity>>,
        var builder: Supplier<DefaultAttributeContainer.Builder>,
    )

    /**
     * Send all the links currently received to the Expected Platform (fabric/forge) to be registered
     */
    fun register() {
        EntityAttributeLinkPlatform.linkEntityAttributes(attributeList)
    }

    /**
     * Register a EntityAttributes link
     *
     * @param entityType EntityType to link
     * @param entityAttributesBuilder EntityAttributes Builder to link
     */
    fun register(
        entityType: RegistrySupplier<out EntityType<out LivingEntity>>,
        entityAttributesBuilder: Supplier<DefaultAttributeContainer.Builder>,
    ) {
        attributeList.add(AttributeListItem(entityType, entityAttributesBuilder))
    }
}

object EntityAttributeLinkPlatform {
    /**
     * Platform specific implementation of the link registry
     *
     * @param attributeList list of the links to register
     */
    @Suppress("UNUSED_PARAMETER")
    @ExpectPlatform
    @JvmStatic
    fun linkEntityAttributes(
        attributeList: MutableList<EntityAttributeLinkRegister.AttributeListItem>,
    ): Unit = throw AssertionError()
}