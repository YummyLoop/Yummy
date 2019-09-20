package yummyloop.example.item

import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.ArmorMaterials
import net.minecraft.item.ItemGroup
import yummyloop.example.item.armor.ArmorWithLeftLeg
import yummyloop.example.item.armor.ArmorWithRightLeg
import yummyloop.example.item.armor.SpecialArmorItem
import yummyloop.example.item.armor.render.BakedSpecialArmor
import yummyloop.example.render.models.UnbakedModel
import yummyloop.example.util.registry.ClientManager

class SpecialArmorBoots(
        itemName: String,
        armorMaterial : ArmorMaterial,
        settings : Settings)
    : SpecialArmorItem(itemName, armorMaterial, EquipmentSlot.FEET, settings), ArmorWithRightLeg, ArmorWithLeftLeg {

    constructor(itemName : String) :
            this(itemName, ArmorMaterials.IRON)
    constructor(itemName: String, armorMaterial : ArmorMaterial) :
            this(itemName, armorMaterial, Settings().group(ItemGroup.COMBAT))

    override val rightLeg = ClientManager.requestModel("$itemName.right.leg")
    override val mirrorRightLeg: Boolean = false
    override val leftLeg = rightLeg
    override val mirrorLeftLeg: Boolean = true

    init {
        ClientManager.requestModel("$itemName.display")
        ClientManager.registerModelVariant { modelIdentifier, modelProviderContext ->
            if (modelIdentifier.path == itemName) {
                UnbakedModel(BakedSpecialArmor(itemName))
            } else {
                null
            }
        }
    }
}