package yummyloop.example.item

import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.ArmorMaterials
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.world.World
import yummyloop.example.util.registry.RegistryManager
import net.minecraft.item.ArmorItem as VanillaArmorItem
import net.minecraft.item.ItemGroup as VanillaItemGroup

open class ArmorItem(itemName: String, armorMaterial : ArmorMaterial, equipmentSlot : EquipmentSlot, settings : Settings) :
        VanillaArmorItem(armorMaterial, equipmentSlot, settings) {
    private val tooltip = ArrayList<Text>()

    init {
        register(itemName)
    }
    constructor(itemName : String) :
            this(itemName, ArmorMaterials.IRON, EquipmentSlot.HEAD, Settings().group(VanillaItemGroup.MISC))

    private fun register (itemName : String) {
        RegistryManager.register(this, itemName)
    }

    override fun appendTooltip(itemStack: ItemStack?, world: World?, tooltip: MutableList<Text>?, tooltipContext: TooltipContext?) {
        tooltip?.addAll(this.tooltip)
    }

    /**
     * Appends a tooltip to the item
     * @param tooltip example: item.tutorial.fabric_item.tooltip
     */
    open fun addTooltip(tooltip : String) : ArmorItem{
        this.tooltip.add(TranslatableText(tooltip))
        return this
    }
}