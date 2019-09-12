@file:JvmName("EntityAttributes")
@file:Mixin(EntityAttributes::class)

package yummyloop.example.mixin.entity.attribute

import net.minecraft.entity.attribute.ClampedEntityAttribute
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributes
import org.spongepowered.asm.mixin.Final
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Mutable
import org.spongepowered.asm.mixin.Shadow

private val maxValue = Double.MAX_VALUE

@Shadow @Final @Mutable private val MAX_HEALTH : EntityAttribute =
        (ClampedEntityAttribute(null, "generic.maxHealth", 20.0, 0.0, maxValue)).setName("Max Health").setTracked(true)

@Shadow @Final @Mutable private val ARMOR : EntityAttribute =
        ClampedEntityAttribute(null, "generic.armor", 0.0, 0.0, maxValue).setTracked(true)

@Shadow @Final @Mutable private val ARMOR_TOUGHNESS : EntityAttribute =
        (ClampedEntityAttribute(null, "generic.armorToughness", 0.0, 0.0, maxValue)).setTracked(true);
