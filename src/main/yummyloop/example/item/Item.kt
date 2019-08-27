package yummyloop.example.item

import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import net.minecraft.item.Item as VanillaItem
import net.minecraft.item.ItemGroup as VanillaItemGroup

open class Item(modId : String, itemName: String, settings : Settings) : VanillaItem(settings) {
    private val tooltip = ArrayList<Text>()

    init {
        register(modId, itemName)
    }
    constructor(modId : String, itemName : String) :
            this(modId, itemName, Settings().group(VanillaItemGroup.MISC))
    constructor(modId : String, itemName : String, group : ItemGroup) :
            this(modId, itemName, Settings().group(group.getGroup()))
    constructor(modId : String, itemName : String, group : ItemGroup, maxCount : Int) :
            this(modId, itemName, Settings().group(group.getGroup()).maxCount(maxCount))

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