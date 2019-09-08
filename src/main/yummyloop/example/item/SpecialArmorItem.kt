package yummyloop.example.item

import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorMaterial
import yummyloop.example.item.armor.SpecialArmor

class SpecialArmorItem(
        modId : String,
        itemName: String,
        armorMaterial : ArmorMaterial,
        equipmentSlot : EquipmentSlot,
        settings : Settings
) :
        ArmorItem(modId, itemName, armorMaterial, equipmentSlot, settings), SpecialArmor {
}