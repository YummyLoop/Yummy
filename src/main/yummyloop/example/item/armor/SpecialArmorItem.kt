package yummyloop.example.item.armor

import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.ArmorMaterials
import net.minecraft.item.ItemGroup
import yummyloop.example.item.ArmorItem

abstract class SpecialArmorItem : ArmorItem, SpecialArmor {

    constructor(itemName: String, armorMaterial : ArmorMaterial, equipmentSlot : EquipmentSlot, settings : Settings)
            : super(itemName, armorMaterial, equipmentSlot, settings)
    constructor(itemName: String, armorMaterial : ArmorMaterial, equipmentSlot : EquipmentSlot)
            : this(itemName, armorMaterial, equipmentSlot, Settings().group(ItemGroup.COMBAT))
    constructor(itemName : String)
            : this(itemName, ArmorMaterials.IRON, EquipmentSlot.HEAD)

    override fun addTooltip(tooltip : String) : SpecialArmorItem {
        super.addTooltip(tooltip)
        return this
    }
}