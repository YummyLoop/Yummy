package yummyloop.example.item

import net.minecraft.item.ArmorMaterials
import net.minecraft.item.ItemStack
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
    @JvmField val containers = mutableMapOf<String, Any>()

    fun ini() {
        ItemGroup["hello1"]= Vanilla.ANVIL
        //ItemGroup["hello3", Vanilla.ANVIL] = listOf(ItemStack(Vanilla.APPLE), ItemStack(Vanilla.APPLE))

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

        Backpack("ring", 6, ItemGroup["hello1"])
        //.addTooltip("item.example.fabric_item.tooltip")
        Backpack("ring2", 5, ItemGroup["hello1"])
        BlockItem("example_blockrender", ItemSettings().group(null), Blocks["template_be"])
        BlockItem("test_barrel", ItemSettings().group(ItemGroup["hello2"]), Blocks["test_barrel"])

        containers["backpack"] = BContainer
    }
}

/* Reference
    // New Custom ItemStack
    ItemStack b = new ItemStack(a);
    b.getOrCreateTag().putInt("CustomModelData", 1);
    b.setCustomName(new LiteralText("Hello"));
*/