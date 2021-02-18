package net.examplemod.integration.geckolib.fabric

import me.shedaniel.architectury.registry.RegistrySupplier
import net.examplemod.integration.geckolib.GeckoUtils
import net.minecraft.item.Item
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.renderer.geo.GeoItemRenderer
import java.util.function.Supplier
import kotlin.reflect.KFunction1

object GeckoUtilsImpl {
    open class GenericItemRenderer<T>(gModel: GeckoUtils.GenericModel<T>) :
        GeoItemRenderer<T>(gModel) where T : IAnimatable, T : Item

    @Suppress("UNUSED_PARAMETER")
    @JvmStatic
    fun <I> geckoSupplier(
        itemFunc: KFunction1<Item.Settings, I>,
        itemSettings: Item.Settings,
        modID: String,
        modelLocation: String,
        textureLocation: String,
        animationFileLocation: String
    ): Supplier<out I> where I : Item, I : IAnimatable {
        return Supplier { itemFunc(itemSettings) }
    }

    object Items {
        fun registerAll() {
            for (i in GeckoUtils.Items.itemList) {
                GeoItemRenderer.registerItemRenderer(
                    @Suppress("UNCHECKED_CAST") (i[0] as RegistrySupplier<Item>).get(),
                    GenericItemRenderer(
                        GeckoUtils.GenericModel(
                            modID = i[1] as String,
                            modelLocation = i[2] as String,
                            textureLocation = i[3] as String,
                            animationFileLocation = i[4] as String,
                        )
                    )
                )
            }
        }
    }
}

