package yummyloop.example.item

import net.minecraft.block.Material
import net.minecraft.item.ItemConvertible
import yummyloop.example.block.Block
import yummyloop.example.block.entity.TemplateBlockWithEntity
import yummyloop.example.item.backpack.Backpack
import net.minecraft.item.Items as Vanilla
import net.minecraft.item.Item as VanillaItem

object Items {
    @JvmField val itemList = mutableMapOf<String, ItemConvertible>()
    @JvmField val groupList = mutableMapOf<String, ItemGroup>()

   @JvmField val blockA = TemplateBlockWithEntity("tutorial", "example_blockrender", Block.of(Material.METAL).lightLevel(10))

    init {
        groupList["groupA"] = ItemGroup("tutorial", "hello1", Vanilla.ANVIL)

        itemList["itemX"] = Item("tutorial", "fabric_item", groupList["groupA"]!!)
        itemList["backpack"] = Backpack("example", "ring", 6, groupList["groupA"]!!)
        itemList["backpack2"] = Backpack("example", "ring2", 5, groupList["groupA"]!!)
        itemList["blockItemA"] = BlockItem("tutorial", "example_blockrender", blockA)

        (itemList["itemX"] as Item).addTooltip("item.tutorial.fabric_item.tooltip")
    }

}