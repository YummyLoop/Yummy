package net.examplemod.registry.forge

import me.shedaniel.architectury.registry.RegistrySupplier
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.DefaultAttributeContainer
import java.util.function.Supplier

object UtilImpl {

    internal data class AttributeListItem(
        var entityType: RegistrySupplier<out EntityType<out LivingEntity>>,
        var builder: Supplier<DefaultAttributeContainer.Builder>,
    )

    internal var attributeList: MutableList<AttributeListItem> = mutableListOf()


    @Suppress("UNUSED_PARAMETER")
    @JvmStatic
    fun <T> linkEntityAttributes(
        entity: RegistrySupplier<out EntityType<out LivingEntity>>,
        entityAttributes: Supplier<DefaultAttributeContainer.Builder>,
    ): Unit where T : LivingEntity {
        attributeList.add(AttributeListItem(entity, entityAttributes))
    }
}
