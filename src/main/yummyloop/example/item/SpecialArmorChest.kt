package yummyloop.example.item

import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.ArmorMaterials
import net.minecraft.item.ItemGroup
import yummyloop.example.item.armor.ArmorWithBody
import yummyloop.example.item.armor.ArmorWithLeftArm
import yummyloop.example.item.armor.ArmorWithRightArm
import yummyloop.example.item.armor.SpecialArmorItem

class SpecialArmorChest(
        itemName: String,
        armorMaterial : ArmorMaterial,
        settings : Settings)
    : SpecialArmorItem(itemName, armorMaterial, EquipmentSlot.CHEST, settings) , ArmorWithBody, ArmorWithRightArm, ArmorWithLeftArm{

    constructor(itemName : String) :
            this(itemName, ArmorMaterials.IRON)
    constructor(itemName: String, armorMaterial : ArmorMaterial) :
            this(itemName, armorMaterial, Settings().group(ItemGroup.COMBAT))

    override val bodyItem: Item = Item("$itemName.body", Settings().group(null).maxCount(1))
    override val rightArmItem: Item = Item("$itemName.right.arm", Settings().group(null).maxCount(1))
    override val mirrorRightArm: Boolean = false
    override val leftArmItem: Item = rightArmItem //Item(itemName+"_leftArm", Settings().group(null).maxCount(1))
    override val mirrorLeftArm: Boolean = true
}