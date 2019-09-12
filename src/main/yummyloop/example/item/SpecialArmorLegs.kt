package yummyloop.example.item

import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.ArmorMaterials
import net.minecraft.item.ItemGroup
import yummyloop.example.item.armor.*

class SpecialArmorLegs(
        itemName: String,
        armorMaterial : ArmorMaterial,
        settings : Settings)
    : SpecialArmorItem(itemName, armorMaterial, EquipmentSlot.LEGS, settings) , ArmorWithBody, ArmorWithRightLeg, ArmorWithLeftLeg{

    constructor(itemName : String) :
            this(itemName, ArmorMaterials.IRON)
    constructor(itemName: String, armorMaterial : ArmorMaterial) :
            this(itemName, armorMaterial, Settings().group(ItemGroup.COMBAT))

    override val bodyItem: Item = Item("$itemName.body", Settings().group(null).maxCount(1))
    override val rightLegItem: Item = Item("$itemName.right.leg", Settings().group(null).maxCount(1))
    override val mirrorRightLeg: Boolean = false
    override val leftLegItem: Item = rightLegItem //Item("$itemName.left.leg", Settings().group(null).maxCount(1))
    override val mirrorLeftLeg: Boolean = true
}