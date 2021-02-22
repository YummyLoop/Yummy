package net.examplemod.integration.geckolib

import me.shedaniel.architectury.annotations.ExpectPlatform
import me.shedaniel.architectury.registry.RegistrySupplier
import net.examplemod.ExampleMod
import net.examplemod.ModRegistry
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.item.GeoArmorItem
import software.bernie.geckolib3.model.AnimatedGeoModel
import java.util.function.Supplier
import kotlin.reflect.KFunction1

object GeckoUtils {
    /**
     * Generic Model for gecko
     *
     * @param modID Mod id
     * @param modelLocation Model location
     * @param textureLocation Texture location
     * @param animationFileLocation Animation location
     */
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

    /**
     * List of gecko items for late render registry on different platforms (forge/fabric)
     */
    var geckoList: MutableList<Pair<GeckoType, Array<Any>>> = mutableListOf()

    object Items {

        /**
         * Registers a Gecko Item
         *
         * @param itemID Base item name
         * @param itemFunc Constructor of the item Class
         * @param itemSettings Pair of ItemName to append + the item settings for said item
         * @param modelLocation Model location
         * @param textureLocation Texture location
         * @param animationFileLocation Animation location
         * @param modID Mod id
         */
        fun <I> register(
            itemID: String,
            itemFunc: KFunction1<Item.Settings, I>,
            itemSettings: Item.Settings,
            modelLocation: String = "geo/$itemID.geo.json",
            textureLocation: String = "textures/item/$itemID.png",
            animationFileLocation: String = "animations/$itemID.animation.json",
            modID: String = ExampleMod.MOD_ID,
        ): RegistrySupplier<Item> where I : Item, I : IAnimatable {
            val myItem = ModRegistry.Register.item(
                itemID,
                geckoItemSupplier(itemFunc, itemSettings, modID, modelLocation, textureLocation, animationFileLocation))
            geckoList.add(Pair(GeckoType.Item,
                arrayOf(myItem, modID, modelLocation, textureLocation, animationFileLocation)))
            return myItem
        }

        /**
         * Registers a gecko Armor + their items as Gecko Items
         *
         * @param itemID Base item name
         * @param itemFunc Constructor of the item Class
         * @param itemSettings0 Pair of ItemName to append + the item settings for said item
         * @param itemSettings1 Pair of ItemName to append + the item settings for said item
         * @param itemSettings2 Pair of ItemName to append + the item settings for said item
         * @param itemSettings3 Pair of ItemName to append + the item settings for said item
         * @param modelLocation Model location
         * @param textureLocation Texture location
         * @param animationFileLocation Animation location
         * @param modID Mod id
         */
        fun <I> registerArmor(
            itemID: String,
            itemFunc: KFunction1<Item.Settings, I>,
            itemSettings0: Pair<String, Item.Settings>,
            itemSettings1: Pair<String, Item.Settings>? = null,
            itemSettings2: Pair<String, Item.Settings>? = null,
            itemSettings3: Pair<String, Item.Settings>? = null,
            modelLocation: String = "geo/$itemID.geo.json",
            textureLocation: String = "textures/item/$itemID.png",
            animationFileLocation: String = "animations/$itemID.animation.json",
            modID: String = ExampleMod.MOD_ID,
        ): MutableList<RegistrySupplier<Item>> where I : Item, I : IAnimatable {
            val myList: MutableList<RegistrySupplier<Item>> = mutableListOf()
            myList.add(register(itemID + itemSettings0.first,
                itemFunc,
                itemSettings0.second,
                modelLocation,
                textureLocation,
                animationFileLocation,
                modID))
            if (itemSettings1 != null) myList.add(register(itemID + itemSettings1.first,
                itemFunc,
                itemSettings1.second,
                modelLocation,
                textureLocation,
                animationFileLocation,
                modID))
            if (itemSettings2 != null) myList.add(register(itemID + itemSettings2.first,
                itemFunc,
                itemSettings2.second,
                modelLocation,
                textureLocation,
                animationFileLocation,
                modID))
            if (itemSettings3 != null) myList.add(register(itemID + itemSettings3.first,
                itemFunc,
                itemSettings3.second,
                modelLocation,
                textureLocation,
                animationFileLocation,
                modID))

            geckoList.add(Pair(GeckoType.Armor,
                arrayOf(myList.first(), modID, modelLocation, textureLocation, animationFileLocation)))
            return myList
        }

        /**
         * Registers a gecko Armor (their items are registered as vanilla items)
         *
         * @param itemID Base item name
         * @param item0 Pair of ItemName to append + the item supplier for said item
         * @param item1 Pair of ItemName to append + the item supplier for said item
         * @param item2 Pair of ItemName to append + the item supplier for said item
         * @param item3 Pair of ItemName to append + the item supplier for said item
         * @param modelLocation Model location
         * @param textureLocation Texture location
         * @param animationFileLocation Animation location
         * @param modID Mod id
         */
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
            myList.add(ModRegistry.Register.item(itemID + item0.first, item0.second))
            if (item1 != null) myList.add(ModRegistry.Register.item(itemID + item1.first, item1.second))
            if (item2 != null) myList.add(ModRegistry.Register.item(itemID + item2.first, item2.second))
            if (item3 != null) myList.add(ModRegistry.Register.item(itemID + item3.first, item3.second))
            geckoList.add(Pair(GeckoType.Armor,
                arrayOf(myList.first(), modID, modelLocation, textureLocation, animationFileLocation)))
            return myList
        }
    }

    object Blocks {

    }
    /**
     * Gets a platform dependent (forge) Gecko Supplier
     *
     * @param itemFunc Constructor of the item Class
     * @param itemSettings Pair of ItemName to append + the item settings for said item
     * @param modelLocation Model location
     * @param textureLocation Texture location
     * @param animationFileLocation Animation location
     * @param modID Mod id
     */
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
        Armor,
        Block
    }
}