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
        blocks["blockA"]=TemplateBlockWithEntity("example_blockrender", Block.of(Material.METAL).lightLevel(10))

        groups["groupA"] = ItemGroup(ExampleMod.id, "hello1", Vanilla.ANVIL)

        items["itemX"] = SpecialArmorItem("fabric_item", ArmorMaterials.LEATHER, EquipmentSlot.FEET, ItemSettings())
        items["hat"] = SpecialArmorItem( "hat", ArmorMaterials.LEATHER, EquipmentSlot.HEAD, ItemSettings())
        items["chest"] = SpecialArmorItem( "chest", ArmorMaterials.LEATHER, EquipmentSlot.CHEST, ItemSettings())
        items["arm"] = SpecialArmorItem( "arm", ArmorMaterials.LEATHER, EquipmentSlot.CHEST, ItemSettings())


        items["backpack"] = Backpack("ring", 6, groups["groupA"]!!)
        items["backpack2"] = Backpack("ring2", 5, groups["groupA"]!!)
        items["blockItemA"] = BlockItem("example_blockrender", ItemSettings().group(null), blocks["blockA"]!!)

        (items["itemX"] as SpecialArmorItem).addTooltip("item.example.fabric_item.tooltip")

        containers["backpack"] = BContainer
        blockEntities["backpack"] = TemplateBlockEntity
    }
}