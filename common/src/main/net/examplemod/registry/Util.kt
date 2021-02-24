package net.examplemod.registry

import me.shedaniel.architectury.annotations.ExpectPlatform
import me.shedaniel.architectury.registry.RegistrySupplier
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.DefaultAttributeContainer
import java.util.function.Supplier

object Util {

    @Suppress("UNUSED_PARAMETER")
    @JvmStatic
    @ExpectPlatform
    fun linkEntityAttributes(
        entity: RegistrySupplier<out EntityType<out LivingEntity>>,
        entityAttributes: Supplier<DefaultAttributeContainer.Builder>,
    ): Unit = throw AssertionError()
}