package net.examplemod.integration.geckolib

import me.shedaniel.architectury.annotations.ExpectPlatform
import me.shedaniel.architectury.registry.RegistrySupplier
import net.examplemod.ExampleMod
import net.examplemod.ExampleMod.ITEMS
import net.minecraft.item.Item
import software.bernie.geckolib3.core.IAnimatable
import java.util.function.Supplier
import kotlin.reflect.KFunction1

object GeckoLib {
    @JvmStatic
    @ExpectPlatform
    fun initialize(): Unit = throw AssertionError()

    object Items {
        var itemList: MutableList<Array<Any>> = mutableListOf()

        fun <I> register(
            itemID: String,
            itemFunc: KFunction1<Item.Settings, I>,
            itemSettings: Item.Settings,
            modelLocation: String = "geo/$itemID.geo.json",
            textureLocation: String = "textures/item/$itemID.png",
            animationFileLocation: String = "animations/$itemID.animation.json",
            modID: String = ExampleMod.MOD_ID
        ): RegistrySupplier<I> where I : Item, I : IAnimatable {
            val itemSupplier =
                geckoSupplier(itemFunc, itemSettings, modID, modelLocation, textureLocation, animationFileLocation)
            val myItem = ITEMS.register(itemID, itemSupplier)
            itemList.add(arrayOf(myItem, modID, modelLocation, textureLocation, animationFileLocation))
            return myItem
        }
    }

    @JvmStatic
    @ExpectPlatform
    fun <I> geckoSupplier(
        itemFunc: KFunction1<Item.Settings, I>,
        itemSettings: Item.Settings,
        modID: String,
        modelLocation: String,
        textureLocation: String,
        animationFileLocation: String
    ): Supplier<out I> where I : Item, I : IAnimatable = throw AssertionError()
}