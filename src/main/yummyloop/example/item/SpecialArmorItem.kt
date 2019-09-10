package yummyloop.example.item

import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorMaterial
import net.minecraft.text.TranslatableText
import yummyloop.example.item.armor.SpecialArmor

class SpecialArmorItem(
        itemName: String,
        armorMaterial : ArmorMaterial,
        equipmentSlot : EquipmentSlot,
        settings : Settings
) :
        ArmorItem(itemName, armorMaterial, equipmentSlot, settings), SpecialArmor {

    override fun addTooltip(tooltip : String) : SpecialArmorItem{
        super.addTooltip(tooltip)
        return this
    }
}