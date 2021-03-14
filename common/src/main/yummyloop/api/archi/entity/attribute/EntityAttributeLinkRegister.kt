@file:JvmMultifileClass

package yummyloop.api.archi.entity.attribute

import me.shedaniel.architectury.annotations.ExpectPlatform
import me.shedaniel.architectury.registry.RegistrySupplier
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.DefaultAttributeContainer
import java.util.function.Supplier

class EntityAttributeLinkRegister private constructor(val modId: String) {

    companion object {
        fun create(modId: String): EntityAttributeLinkRegister = EntityAttributeLinkRegister(modId)
    }

    /** Private list of EntityAttributes links */
    private var attributeLinkList: MutableList<Pair<
            RegistrySupplier<out EntityType<out LivingEntity>>,
            DefaultAttributeContainer.Builder
            >> = mutableListOf()

    fun getLinkList() = attributeLinkList

    /**
     * Registers all the links currently received in the Expected Platform (fabric/forge) to be registered
     */
    fun register() {
        EntityAttributeLinkPlatform.register(this)
    }

    /**
     * Register a EntityAttributes link
     *
     * @param entityType EntityType to link
     * @param entityAttributesBuilder EntityAttributes Builder to link
     */
    fun register(
        entityType: RegistrySupplier<out EntityType<out LivingEntity>>,
        entityAttributesBuilder: DefaultAttributeContainer.Builder,
    ) {
        attributeLinkList.add(Pair(entityType, entityAttributesBuilder))
    }
}

object EntityAttributeLinkPlatform {
    /**
     * Registers a mod EntityAttributes links,
     * with platform specific implementation
     *
     * @param register A mod Entity Attribute Link register
     */
    @Suppress("UNUSED_PARAMETER")
    @ExpectPlatform
    @JvmStatic
    fun register(register: EntityAttributeLinkRegister): Unit = throw AssertionError()
}