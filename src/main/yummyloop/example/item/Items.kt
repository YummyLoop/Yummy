package yummyloop.example.item

import net.minecraft.block.Material
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorMaterials
import net.minecraft.item.ItemConvertible
import yummyloop.example.ExampleMod
import yummyloop.example.block.Block
import yummyloop.example.block.entity.TemplateBlockEntity
import yummyloop.example.block.entity.TemplateBlockWithEntity
import yummyloop.example.item.backpack.BContainer
import yummyloop.example.item.backpack.Backpack
import net.minecraft.block.Block as VanillaBlock
import net.minecraft.item.Items as Vanilla
import net.minecraft.item.Item.Settings as ItemSettings

object Items {
    @JvmField val items = mutableMapOf<String, ItemConvertible>()
    @JvmField val groups = mutableMapOf<String, ItemGroup>()
    @JvmField val containers = mutableMapOf<String, Any>()
    @JvmField val blockEntities = mutableMapOf<String, Any>()
    @JvmField val blocks = mutableMapOf<String, VanillaBlock>()

    init {
        blocks["blockA"]=TemplateBlockWithEntity(ExampleMod.id, "example_blockrender", Block.of(Material.METAL).lightLevel(10))

        groups["groupA"] = ItemGroup(ExampleMod.id, "hello1", Vanilla.ANVIL)

        items["itemX"] = SpecialArmorItem(ExampleMod.id, "fabric_item", ArmorMaterials.LEATHER, EquipmentSlot.FEET, ItemSettings())
        items["backpack"] = Backpack(ExampleMod.id, "ring", 6, groups["groupA"]!!)
        items["backpack2"] = Backpack(ExampleMod.id, "ring2", 5, groups["groupA"]!!)
        items["blockItemA"] = BlockItem(ExampleMod.id, "example_blockrender", blocks["blockA"]!!)

        (items["itemX"] as SpecialArmorItem).addTooltip("item.example.fabric_item.tooltip")

        containers["backpack"] = BContainer
        blockEntities["backpack"] = TemplateBlockEntity
    }
}