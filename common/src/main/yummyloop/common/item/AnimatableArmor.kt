package yummyloop.common.item

import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorMaterial
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.item.GeoArmorItem

abstract class AnimatableArmor(materialIn: ArmorMaterial, slot: EquipmentSlot, builder: Settings) :
    GeoArmorItem(materialIn, slot, builder), IAnimatable