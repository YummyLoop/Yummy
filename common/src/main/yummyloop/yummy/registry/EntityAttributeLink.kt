package yummyloop.yummy.registry

import me.shedaniel.architectury.annotations.ExpectPlatform
import me.shedaniel.architectury.registry.RegistrySupplier
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.DefaultAttributeContainer
import java.util.function.Supplier

object EntityAttributeLink {

    data class AttributeListItem(
        var entityType: RegistrySupplier<out EntityType<out LivingEntity>>,
        var builder: Supplier<DefaultAttributeContainer.Builder>,
    )

    /** Private list of EntityAttributes links */
    private var attributeList: MutableList<AttributeListItem> = mutableListOf()

    /**
     * Send all the links currently received to the Expected Platform (fabric/forge) to be registered
     */
    internal fun register() {
        linkEntityAttributes(attributeList)
    }

    /**
     * Register a EntityAttributes link
     *
     * @param entityType EntityType to link
     * @param entityAttributesBuilder EntityAttributes Builder to link
     */
    internal fun register(
        entityType: RegistrySupplier<out EntityType<out LivingEntity>>,
        entityAttributesBuilder: Supplier<DefaultAttributeContainer.Builder>,
    ) {
        attributeList.add(AttributeListItem(entityType, entityAttributesBuilder))
    }

    /**
     * Platform specific implementation of the link registry
     *
     * @param attributeList list of the links to register
     */
    @Suppress("UNUSED_PARAMETER")
    @JvmStatic
    @ExpectPlatform
    fun linkEntityAttributes(
        attributeList: MutableList<AttributeListItem>,
    ): Unit = throw AssertionError()
}