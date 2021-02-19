package net.examplemod.integration.geckolib

import me.shedaniel.architectury.annotations.ExpectPlatform
import me.shedaniel.architectury.registry.RegistrySupplier
import net.examplemod.ExampleMod
import net.examplemod.items.Ytem
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.item.GeoArmorItem
import software.bernie.geckolib3.model.AnimatedGeoModel
import java.util.function.Supplier
import kotlin.reflect.KFunction1

object GeckoUtils {
    open class GenericModel<T>(
        private val modID: String,
        private val modelLocation: String,
        private val textureLocation: String,
        private val animationFileLocation: String,
    ) : AnimatedGeoModel<T>() where T : IAnimatable? { // Crashes if it has a null check
        override fun getModelLocation(obj: T) = Identifier(modID, modelLocation)
        override fun getTextureLocation(obj: T) = Identifier(modID, textureLocation)
        override fun getAnimationFileLocation(obj: T) = Identifier(modID, animationFileLocation)
    }

    var geckoList: MutableList<Pair<GeckoType, Array<Any>>> = mutableListOf()

    object Items {
        fun <I> register(
            itemID: String,
            itemFunc: KFunction1<Item.Settings, I>,
            itemSettings: Item.Settings,
            modelLocation: String = "geo/$itemID.geo.json",
            textureLocation: String = "textures/item/$itemID.png",
            animationFileLocation: String = "animations/$itemID.animation.json",
            modID: String = ExampleMod.MOD_ID,
        ): RegistrySupplier<Item> where I : Item, I : IAnimatable {
            val itemSupplier =
                geckoItemSupplier(itemFunc, itemSettings, modID, modelLocation, textureLocation, animationFileLocation)
            val myItem = Ytem.register(itemID, itemSupplier)
            geckoList.add(Pair(GeckoType.Item,
                arrayOf(myItem, modID, modelLocation, textureLocation, animationFileLocation)))
            return myItem
        }

        fun <T> registerArmor(
            itemID: String,
            item0: Pair<String, Supplier<T>>,
            item1: Pair<String, Supplier<T>>? = null,
            item2: Pair<String, Supplier<T>>? = null,
            item3: Pair<String, Supplier<T>>? = null,
            modelLocation: String = "geo/$itemID.geo.json",
            textureLocation: String = "textures/item/$itemID.png",
            animationFileLocation: String = "animations/$itemID.animation.json",
            modID: String = ExampleMod.MOD_ID,
        ): MutableList<RegistrySupplier<Item>> where T : GeoArmorItem {
            val myList: MutableList<RegistrySupplier<Item>> = mutableListOf()
            myList.add(Ytem.register(itemID + item0.first, item0.second))
            if (item1 != null) myList.add(Ytem.register(itemID + item1.first, item1.second))
            if (item2 != null) myList.add(Ytem.register(itemID + item2.first, item2.second))
            if (item3 != null) myList.add(Ytem.register(itemID + item3.first, item3.second))
            geckoList.add(Pair(GeckoType.Armor,
                arrayOf(myList.first(), modID, modelLocation, textureLocation, animationFileLocation)))
            return myList
        }
    }

    @Suppress("UNUSED_PARAMETER")
    @JvmStatic
    @ExpectPlatform
    fun <I> geckoItemSupplier(
        itemFunc: KFunction1<Item.Settings, I>,
        itemSettings: Item.Settings,
        modID: String,
        modelLocation: String,
        textureLocation: String,
        animationFileLocation: String,
    ): Supplier<out I> where I : Item, I : IAnimatable = throw AssertionError()

    enum class GeckoType {
        Item,
        Armor
    }
}