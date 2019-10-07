package yummyloop.example.item

import net.minecraft.item.ArmorMaterials
import yummyloop.example.block.Blocks
import yummyloop.example.item.backpack.BContainer
import yummyloop.example.item.backpack.Backpack
import yummyloop.example.item.thrown.spear.*
import yummyloop.example.item.thrown.Cobweb
import net.minecraft.item.Item as VanillaItem
import net.minecraft.item.Item.Settings as ItemSettings
import net.minecraft.item.Items as Vanilla
import net.minecraft.item.ItemGroup as VanillaItemGroup

object Items : HashMap<String, VanillaItem>() {
    @JvmField val groups = mutableMapOf<String, VanillaItemGroup?>()
    @JvmField val containers = mutableMapOf<String, Any>()

    fun ini() {
        ItemGroup("hello1", Vanilla.ANVIL)

        SpecialArmorBoots("boots", ArmorMaterials.LEATHER)
        SpecialArmorHelmet("hat", ArmorMaterials.LEATHER)
        SpecialArmorChest( "chest", ArmorMaterials.LEATHER)

        SpearAcacia
        SpearBirch
        SpearDarkOak
        SpearJungle
        SpearOak
        SpearSpruce
        Cobweb

        Backpack("ring", 6, groups["hello1"])
        //.addTooltip("item.example.fabric_item.tooltip")
        Backpack("ring2", 5, groups["hello1"])
        BlockItem("example_blockrender", ItemSettings().group(null), Blocks["template_be"])
        BlockItem("test_barrel", ItemSettings().group(groups["hello1"]), Blocks["test_barrel"])

        containers["backpack"] = BContainer
    }
}