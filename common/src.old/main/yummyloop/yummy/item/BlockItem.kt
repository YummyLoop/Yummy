package yummyloop.yummy.item

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.Material
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.world.World
import yummyloop.yummy.block.Block
import yummyloop.yummy.registry.RegistryManager
import java.util.*
import net.minecraft.block.Block as VanillaBlock
import net.minecraft.item.BlockItem as VanillaBlockItem
import net.minecraft.item.ItemGroup as VanillaItemGroup

class BlockItem : VanillaBlockItem {
    private val tooltip = ArrayList<Text>()

    // Name, ItemSettings, BlockSettings
    constructor(itemName: String, itemSettings: Settings, blockSettings: FabricBlockSettings) :
            super(Block(itemName, blockSettings), itemSettings) {
        register(itemName)
    }

    // Name
    constructor(itemName: String) :
            this(itemName, Settings().group(VanillaItemGroup.MISC), Block.of(Material.AIR))

    // Name, ItemSettings, Block
    constructor(itemName: String, itemSettings: Settings, block: VanillaBlock?) :
            super(block ?: Block(itemName), itemSettings) {
        register(itemName)
    }

    // Name, Block
    constructor(itemName: String, block: VanillaBlock) :
            this(itemName, Settings().group(net.minecraft.item.ItemGroup.MISC), block)

    // Register name
    private fun register(itemName: String) = RegistryManager.register(this, itemName)

    override fun appendTooltip(itemStack: ItemStack?, world: World?, tooltip: MutableList<Text>, tooltipContext: TooltipContext?) {
        tooltip.addAll(this.tooltip)
    }

    /**
     * Appends a tooltip to the item
     * @param tooltip example: item.tutorial.fabric_item.tooltip
     */
    fun addTooltip(tooltip: String) : BlockItem{
        this.tooltip.add(TranslatableText(tooltip))
        return this
    }
}