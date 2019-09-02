package yummyloop.example.item

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.Material
import java.util.ArrayList

import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import yummyloop.example.block.Block
import net.minecraft.block.Block as VanillaBlock
import net.minecraft.item.BlockItem as VanillaBlockItem
import net.minecraft.item.ItemGroup as VanillaItemGroup

class BlockItem : VanillaBlockItem {
    private val tooltip = ArrayList<Text>()

    // ModId, Name, ItemSettings, BlockSettings
    constructor(modId: String, itemName: String, itemSettings: Settings, blockSettings: FabricBlockSettings) :
            super(Block(modId, itemName, blockSettings), itemSettings) {
        register(modId, itemName)
    }

    // ModId, Name
    constructor(modId: String, itemName: String) :
            this(modId, itemName, Settings().group(VanillaItemGroup.MISC), Block.of(Material.AIR))

    // ModId, Name, ItemSettings, Block
    constructor(modId: String, itemName: String, itemSettings: Settings, block: VanillaBlock) :
            super(block, itemSettings) {
        register(modId, itemName)
    }

    // ModId, Name, Block
    constructor(modId: String, itemName: String, block: VanillaBlock) :
            this(modId, itemName, Settings().group(net.minecraft.item.ItemGroup.MISC), block)

    // Register name
    private fun register(modId: String, itemName: String) {
        Registry.register(Registry.ITEM, Identifier(modId, itemName), this)
    }

    override fun appendTooltip(itemStack: ItemStack?, world: World?, tooltip: MutableList<Text>, tooltipContext: TooltipContext?) {
        tooltip.addAll(this.tooltip)
    }

    /**
     * Appends a tooltip to the item
     * @param tooltip example: item.tutorial.fabric_item.tooltip
     */
    fun addTooltip(tooltip: String) {
        this.tooltip.add(TranslatableText(tooltip))
    }
}