package yummyloop.example.item

import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.world.World
import yummyloop.example.registry.RegistryManager
import net.minecraft.item.Item as VanillaItem
import net.minecraft.item.ItemGroup as VanillaItemGroup
g
open class Item(itemName: String, settings : Settings) : VanillaItem(settings) {
    private val tooltip = ArrayList<Text>()

    init {
        register(itemName)
    }
    constructor(itemName : String) :
            this(itemName, Settings().group(VanillaItemGroup.MISC))
    constructor(itemName : String, group : VanillaItemGroup?) :
            this(itemName, Settings().group(group))
    constructor(itemName : String, group : VanillaItemGroup?, maxCount : Int) :
            this(itemName, Settings().group(group).maxCount(maxCount))

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
    fun addTooltip(tooltip : String) : Item{
        this.tooltip.add(TranslatableText(tooltip))
        return this
    }
}