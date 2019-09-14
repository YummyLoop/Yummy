package yummyloop.example.item

import net.minecraft.item.ArmorMaterials
import yummyloop.example.block.Blocks
import yummyloop.example.item.backpack.BContainer
import yummyloop.example.item.backpack.Backpack
import net.minecraft.item.Item as VanillaItem
import net.minecraft.item.Item.Settings as ItemSettings
import net.minecraft.item.Items as Vanilla
import net.minecraft.item.ItemGroup as VanillaItemGroup

object Items : HashMap<String, VanillaItem>() {
    @JvmField val groups = mutableMapOf<String, ItemGroup>()
    @JvmField val containers = mutableMapOf<String, Any>()

    fun ini() {
        ItemGroup("hello1", Vanilla.ANVIL)

        SpecialArmorBoots("boots", ArmorMaterials.LEATHER)
        SpecialArmorHelmet("hat", ArmorMaterials.LEATHER)
        SpecialArmorChest( "chest", ArmorMaterials.LEATHER)

        Spear("spear", ItemSettings().maxDamage(60).group(VanillaItemGroup.COMBAT))

        Backpack("ring", 6, groups["hello1"])
        //.addTooltip("item.example.fabric_item.tooltip")
        Backpack("ring2", 5, groups["hello1"])
        BlockItem("example_blockrender", ItemSettings().group(null), Blocks["template_be"])

        containers["backpack"] = BContainer
    }
}