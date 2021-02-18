package net.examplemod.integration.geckolib.forge

import net.examplemod.integration.geckolib.GeckoUtils
import net.minecraft.item.Item
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer
import java.util.concurrent.Callable
import java.util.function.Supplier
import kotlin.reflect.KFunction1

object GeckoUtilsImpl {
    open class GenericItemRenderer<T>(gModel: GeckoUtils.GenericModel<T>) :
        GeoItemRenderer<T>(gModel) where T : IAnimatable, T : Item

    @JvmStatic
    fun <I> geckoSupplier(
        itemFunc: KFunction1<Item.Settings, I>,
        itemSettings: Item.Settings,
        modID: String,
        modelLocation: String,
        textureLocation: String,
        animationFileLocation: String
    ): Supplier<out I> where I : Item, I : IAnimatable {
        val itemSupplier = itemFunc(itemSettings.setISTER {
            Callable {
                GenericItemRenderer(
                    GeckoUtils.GenericModel(
                        modID,
                        modelLocation,
                        textureLocation,
                        animationFileLocation
                    )
                )
            }
        })
        return Supplier { itemSupplier }
    }
}

