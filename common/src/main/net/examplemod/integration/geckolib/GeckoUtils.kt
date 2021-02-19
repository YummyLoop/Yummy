package net.examplemod.integration.geckolib

import me.shedaniel.architectury.annotations.ExpectPlatform
import me.shedaniel.architectury.registry.RegistrySupplier
import net.examplemod.ExampleMod
import net.examplemod.items.Ytem
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.model.AnimatedGeoModel
import java.util.function.Supplier
import kotlin.reflect.KFunction1

object GeckoUtils {
    open class GenericModel<T>(
        private val modID: String,
        private val modelLocation: String,
        private val textureLocation: String,
        private val animationFileLocation: String
    ) : AnimatedGeoModel<T>() where T : IAnimatable {
        override fun getModelLocation(obj: T) = Identifier(modID, modelLocation)
        override fun getTextureLocation(obj: T) = Identifier(modID, textureLocation)
        override fun getAnimationFileLocation(obj: T) = Identifier(modID, animationFileLocation)
    }

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
        ): RegistrySupplier<Item> where I : Item, I : IAnimatable {
            val itemSupplier =
                geckoItemSupplier(itemFunc, itemSettings, modID, modelLocation, textureLocation, animationFileLocation)
            val myItem = Ytem.register(itemID, itemSupplier)
            itemList.add(arrayOf(myItem, modID, modelLocation, textureLocation, animationFileLocation))
            return myItem
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
        animationFileLocation: String
    ): Supplier<out I> where I : Item, I : IAnimatable = throw AssertionError()
}