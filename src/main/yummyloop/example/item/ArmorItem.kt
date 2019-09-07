package yummyloop.example.item

import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.ArmorMaterials
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import net.minecraft.item.ArmorItem as VanillaArmorItem
import net.minecraft.item.ItemGroup as VanillaItemGroup

class ArmorItem(modId : String, itemName: String, armorMaterial : ArmorMaterial, equipmentSlot : EquipmentSlot,  settings : Settings) :
        VanillaArmorItem(armorMaterial, equipmentSlot, settings) {
    private val tooltip = ArrayList<Text>()

    init {
        register(modId, itemName)
    }
    constructor(modId : String, itemName : String) :
            this(modId, itemName, ArmorMaterials.IRON, EquipmentSlot.HEAD, Settings().group(VanillaItemGroup.MISC))

    private fun register (modId : String, itemName : String) {
        Registry.register(Registry.ITEM, Identifier(modId, itemName), this)
    }

    override fun appendTooltip(itemStack: ItemStack?, world: World?, tooltip: MutableList<Text>?, tooltipContext: TooltipContext?) {
        tooltip?.addAll(this.tooltip)
    }

    /**
     * Appends a tooltip to the item
     * @param tooltip example: item.tutorial.fabric_item.tooltip
     */
    fun addTooltip(tooltip : String){
        this.tooltip.add(TranslatableText(tooltip))
    }
}