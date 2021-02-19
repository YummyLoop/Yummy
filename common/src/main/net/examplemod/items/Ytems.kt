package net.examplemod.items

import net.examplemod.integration.geckolib.GeckoUtils
import net.examplemod.integration.geckolib.JackInTheBoxItem2
import net.examplemod.integration.geckolib.PotatoArmor2
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorMaterials
import net.minecraft.item.Item
import java.util.function.Supplier

object Ytems {
    init {
        Ytem.register()
    }

    var EXAMPLE_ITEM = Ytem.register("example_item") { Ytem(Ytem.Settings().group(YtemGroup.EXAMPLE_TAB)) }
    var test_ITEM = Ytem.register("test_item")

    // Gecko
    var JACK_IN_THE_BOX2 =
        GeckoUtils.Items.register("jack", ::JackInTheBoxItem2, Item.Settings().group(YtemGroup.EXAMPLE_TAB))

    //var JACK_IN_THE_BOX2  = ITEMS.register("jack") { JackInTheBoxItem2(Item.Settings().group(EXAMPLE_TAB)) }
    var armor = GeckoUtils.Items.registerArmor(
        "potato_armor",
        "_head" to Supplier { PotatoArmor2(ArmorMaterials.DIAMOND, EquipmentSlot.HEAD, Ytem.Settings()) },
        "_chestplate" to Supplier { PotatoArmor2(ArmorMaterials.DIAMOND, EquipmentSlot.CHEST, Ytem.Settings()) },
        "_leggings" to Supplier { PotatoArmor2(ArmorMaterials.DIAMOND, EquipmentSlot.LEGS, Ytem.Settings()) },
        "_boots" to Supplier { PotatoArmor2(ArmorMaterials.DIAMOND, EquipmentSlot.FEET, Ytem.Settings()) },
    )
}