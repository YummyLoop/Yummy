package net.examplemod.registry.fabric

import me.shedaniel.architectury.registry.RegistrySupplier
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.DefaultAttributeContainer
import java.util.function.Supplier

object UtilImpl {

    @Suppress("UNUSED_PARAMETER")
    @JvmStatic
    fun <T>linkEntityAttributes(
        entity: RegistrySupplier<out EntityType<out LivingEntity>>,
        entityAttributes: Supplier<DefaultAttributeContainer.Builder>,
    ): Unit where T : LivingEntity {
        FabricDefaultAttributeRegistry.register(entity.get(), entityAttributes.get())
    }
}