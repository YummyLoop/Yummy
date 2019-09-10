package yummyloop.example.item

import net.minecraft.block.Material
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorMaterials
import yummyloop.example.block.Block
import yummyloop.example.block.entity.TemplateBlockEntity
import yummyloop.example.block.entity.TemplateBlockWithEntity
import yummyloop.example.item.backpack.BContainer
import yummyloop.example.item.backpack.Backpack
import net.minecraft.block.Block as VanillaBlock
import net.minecraft.item.Item as VanillaItem
import net.minecraft.item.Item.Settings as ItemSettings
import net.minecraft.item.Items as Vanilla

object Items : HashMap<String, VanillaItem>() {
    @JvmField val groups = mutableMapOf<String, ItemGroup>()
    @JvmField val containers = mutableMapOf<String, Any>()
    @JvmField val blockEntities = mutableMapOf<String, Any>()
    @JvmField val blocks = mutableMapOf<String, VanillaBlock>()

    init {
        blocks["blockA"]=TemplateBlockWithEntity("example_blockrender", Block.of(Material.METAL).lightLevel(10))

        groups["groupA"] = ItemGroup("hello1", Vanilla.ANVIL)

        SpecialArmorItem("fabric_item", ArmorMaterials.LEATHER, EquipmentSlot.FEET, ItemSettings())
                .addTooltip("item.example.fabric_item.tooltip")
        SpecialArmorItem( "hat", ArmorMaterials.LEATHER, EquipmentSlot.HEAD, ItemSettings())
        SpecialArmorItem( "chest", ArmorMaterials.LEATHER, EquipmentSlot.CHEST, ItemSettings())
        SpecialArmorItem( "arm", ArmorMaterials.LEATHER, EquipmentSlot.CHEST, ItemSettings())


        Backpack("ring", 6, groups["groupA"])
        Backpack("ring2", 5, groups["groupA"])
        BlockItem("example_blockrender", ItemSettings().group(null), blocks["blockA"]!!)

        containers["backpack"] = BContainer
        blockEntities["backpack"] = TemplateBlockEntity
    }
}