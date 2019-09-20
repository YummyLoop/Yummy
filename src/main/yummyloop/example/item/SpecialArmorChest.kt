package yummyloop.example.item

import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.ArmorMaterials
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import yummyloop.example.ExampleMod
import yummyloop.example.item.armor.ArmorWithBody
import yummyloop.example.item.armor.ArmorWithLeftArm
import yummyloop.example.item.armor.ArmorWithRightArm
import yummyloop.example.item.armor.SpecialArmorItem
import yummyloop.example.item.armor.render.BakedSpecialArmor
import yummyloop.example.render.models.UnbakedModel
import yummyloop.example.util.registry.ClientManager

class SpecialArmorChest(
        itemName: String,
        armorMaterial : ArmorMaterial,
        settings : Settings)
    : SpecialArmorItem(itemName, armorMaterial, EquipmentSlot.CHEST, settings) , ArmorWithBody, ArmorWithRightArm, ArmorWithLeftArm{

    constructor(itemName : String) :
            this(itemName, ArmorMaterials.IRON)
    constructor(itemName: String, armorMaterial : ArmorMaterial) :
            this(itemName, armorMaterial, Settings().group(ItemGroup.COMBAT))

    override val body = ClientManager.requestModel("$itemName.body")
    //override val bodyTransform = BakedSpecialArmor.fetchTransform(Identifier(ExampleMod.id, "models/item/$name.body.json"))
    override val rightArm = ClientManager.requestModel("$itemName.right.arm")
    override val mirrorRightArm: Boolean = false
    override val leftArm = rightArm
    override val mirrorLeftArm: Boolean = true

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