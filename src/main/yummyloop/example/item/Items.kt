package yummyloop.example.item

import net.minecraft.block.Material
import yummyloop.example.block.Block
import yummyloop.example.item.backpack.Backpack
import net.minecraft.item.Items as Vanilla

object Items {
   @JvmField val groupA = ItemGroup("tutorial", "hello1", Vanilla.ANVIL)
   //@JvmField val F = Item("tutorial", "fabric_item1", groupA)

   @JvmField val itemX = Item("tutorial", "fabric_item", groupA)

   @JvmField val blockA = Block("tutorial", "example_block", Block.Settings.of(Material.METAL).lightLevel(10))
   @JvmField val blockItemA = BlockItem("tutorial", "example_block", blockA, groupA)

    val backpack = Backpack("example", "ring", 6, groupA)
    val itemP = Item("example", "portal", groupA)

    init {
        this.itemX.addTooltip("item.tutorial.fabric_item.tooltip")
    }

}