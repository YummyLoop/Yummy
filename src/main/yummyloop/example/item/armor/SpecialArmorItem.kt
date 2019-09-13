package yummyloop.example.item.armor

import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.ArmorMaterials
import net.minecraft.item.ItemGroup
import yummyloop.example.item.ArmorItem

abstract class SpecialArmorItem(
        itemName: String,
        armorMaterial : ArmorMaterial,
        equipmentSlot : EquipmentSlot,
        settings : Settings
) :
        ArmorItem(itemName, armorMaterial, equipmentSlot, settings), SpecialArmor {
    constructor(itemName : String) :
            this(itemName, ArmorMaterials.IRON, EquipmentSlot.HEAD)
    constructor(itemName: String, armorMaterial : ArmorMaterial, equipmentSlot : EquipmentSlot) :
            this(itemName, armorMaterial, equipmentSlot, Settings().group(ItemGroup.COMBAT))

    override fun addTooltip(tooltip : String) : SpecialArmorItem {
        super.addTooltip(tooltip)
        return this
    }
}