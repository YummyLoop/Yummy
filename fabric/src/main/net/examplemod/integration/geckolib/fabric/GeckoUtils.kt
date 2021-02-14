package net.examplemod.integration.geckolib.fabric

import me.shedaniel.architectury.registry.RegistrySupplier
import net.examplemod.integration.geckolib.GeckoLib
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.model.AnimatedGeoModel
import software.bernie.geckolib3.renderer.geo.GeoItemRenderer

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

    open class GenericItemRenderer<T>(gModel: GenericModel<T>) :
        GeoItemRenderer<T>(gModel) where T : IAnimatable, T : Item

    object Items {
        fun registerAll() {
            for (i in GeckoLib.Items.itemList) {
                GeoItemRenderer.registerItemRenderer(
                    (i[0] as RegistrySupplier<Item>).get(),
                    GenericItemRenderer(
                        GenericModel(
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

