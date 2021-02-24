package net.examplemod.registry

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

    private var attributeList: MutableList<AttributeListItem> = mutableListOf()

    fun register() {
        linkEntityAttributes(attributeList)
    }

    fun <T> register(
        entity: RegistrySupplier<EntityType<T>>,
        entityAttributes: Supplier<DefaultAttributeContainer.Builder>,
    ) where T : LivingEntity {
        attributeList.add(AttributeListItem(entity, entityAttributes))
    }

    @Suppress("UNUSED_PARAMETER")
    @JvmStatic
    @ExpectPlatform
    fun linkEntityAttributes(
        attributeList: MutableList<AttributeListItem>,
    ): Unit = throw AssertionError()
}