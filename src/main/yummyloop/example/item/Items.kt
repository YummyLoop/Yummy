package yummyloop.example.item

import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorMaterials
import yummyloop.example.block.Blocks
import yummyloop.example.item.backpack.BContainer
import yummyloop.example.item.backpack.Backpack
import net.minecraft.item.Item as VanillaItem
import net.minecraft.item.Item.Settings as ItemSettings
import net.minecraft.item.Items as Vanilla

object Items : HashMap<String, VanillaItem>() {
    @JvmField val groups = mutableMapOf<String, ItemGroup>()
    @JvmField val containers = mutableMapOf<String, Any>()

    fun ini() {
        ItemGroup("hello1", Vanilla.ANVIL)

        SpecialArmorItem("fabric_item", ArmorMaterials.LEATHER, EquipmentSlot.FEET, ItemSettings())
                .addTooltip("item.example.fabric_item.tooltip")
        SpecialArmorItem( "hat", ArmorMaterials.LEATHER, EquipmentSlot.HEAD, ItemSettings())
        SpecialArmorItem( "chest", ArmorMaterials.LEATHER, EquipmentSlot.CHEST, ItemSettings())
        SpecialArmorItem( "arm", ArmorMaterials.LEATHER, EquipmentSlot.CHEST, ItemSettings())


        Backpack("ring", 6, groups["hello1"])
        Backpack("ring2", 5, groups["hello1"])
        BlockItem("example_blockrender", ItemSettings().group(null), Blocks["template_be"])

        containers["backpack"] = BContainer
    }
}