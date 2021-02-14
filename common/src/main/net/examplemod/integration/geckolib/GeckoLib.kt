package net.examplemod.integration.geckolib

import me.shedaniel.architectury.annotations.ExpectPlatform
import me.shedaniel.architectury.registry.RegistrySupplier
import net.examplemod.ExampleMod
import net.examplemod.ExampleMod.ITEMS
import net.minecraft.item.Item
import java.util.function.Supplier

object GeckoLib {
    @JvmStatic @ExpectPlatform
    fun initialize(): Unit = throw AssertionError()

    object Items {
        var itemList : MutableList<Array<Any>> = mutableListOf()
        fun <I> register(
            itemID : String,
            modelLocation: String = "geo/$itemID.geo.json",
            textureLocation: String = "textures/item/$itemID.png",
            animationFileLocation: String = "animations/$itemID.animation.json",
            modID: String = ExampleMod.MOD_ID,
            supplier: Supplier<out I>
        ) : RegistrySupplier<I> where I : Item {
            val myItem = ITEMS.register(itemID, supplier)
            itemList.add(arrayOf(myItem, modID, modelLocation, textureLocation, animationFileLocation))
            //registerGeckoItem(myItem.get(), modID, modelLocation, textureLocation, animationFileLocation)
            return myItem
        }
    }
}