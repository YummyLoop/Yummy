package net.examplemod.integration.geckolib.forge

import net.minecraft.item.Item
import software.bernie.geckolib3.GeckoLib
import software.bernie.geckolib3.core.IAnimatable
import java.util.concurrent.Callable
import java.util.function.Supplier
import kotlin.reflect.KFunction1

object GeckoLibImpl {
    @JvmStatic
    fun initialize(): Unit = GeckoLib.initialize()

    @JvmStatic
    fun <I> geckoSupplier(
        itemFunc: KFunction1<Item.Settings, I>,
        itemSettings: Item.Settings,
        modID: String,
        modelLocation: String,
        textureLocation: String,
        animationFileLocation: String
    ): Supplier<out I> where I : Item, I : IAnimatable {
        val itemRenderer = GeckoUtils.GenericItemRenderer(
            GeckoUtils.GenericModel(
                modID,
                modelLocation,
                textureLocation,
                animationFileLocation
            )
        )
        val itemSupplier = itemFunc(itemSettings.setISTER { Callable { itemRenderer } })
        return Supplier { itemSupplier }
    }
}