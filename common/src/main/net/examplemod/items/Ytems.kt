package net.examplemod.items

import net.examplemod.integration.geckolib.GeckoUtils
import net.examplemod.integration.geckolib.JackInTheBoxItem2
import net.minecraft.item.Item

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
}