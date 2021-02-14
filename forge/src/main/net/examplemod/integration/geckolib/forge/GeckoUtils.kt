package net.examplemod.integration.geckolib.forge

import me.shedaniel.architectury.registry.RegistrySupplier
import net.examplemod.integration.geckolib.GeckoLib
import net.examplemod.integration.geckolib.JackInTheBoxItem2
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.model.AnimatedGeoModel
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer
import java.util.concurrent.Callable

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

    open class GenericItemRenderer<T>(gModel: GenericModel<T>) : GeoItemRenderer<T>(gModel) where T : IAnimatable, T : Item


    class JackInTheBoxRenderer3 :
        GeoItemRenderer<JackInTheBoxItem2>(GenericModel("a", "b", "c", "d"))

    class JackInTheBoxRenderer2 :
        GenericItemRenderer<JackInTheBoxItem2>(GenericModel("a", "b", "c", "d"))


    //new JackInTheBoxItem(new Item.Properties().setISTER(() -> JackInTheBoxRenderer::new))
    object Items {

        fun registerAll() {

            for (i in GeckoLib.Items.itemList) {
                //half working ?
                //JackInTheBoxItem2(Item.Settings().setISTER { Callable { JackInTheBoxRenderer2() } })

                    val a = Item(Item.Settings())
                var b = a

                JackInTheBoxItem2(Item.Settings().setISTER { Callable { GenericItemRenderer(
                    GenericModel(
                        modID = i[1] as String,
                        modelLocation = i[2] as String,
                        textureLocation = i[3] as String,
                        animationFileLocation = i[4] as String,
                    )
                ) } })

                /*

                var r = GenericItemRenderer(
                    GenericModel(
                        modID = i[1] as String,
                        modelLocation = i[2] as String,
                        textureLocation = i[3] as String,
                        animationFileLocation = i[4] as String,
                    )
                )

                 */

                /*
                    GeoItemRenderer.registerItemRenderer(
                        (i[0] as RegistrySupplier<Item>).get(),
                        GeckoUtils.GenericItemRenderer(
                            GeckoUtils.GenericModel(
                                modID = i[1] as String,
                                modelLocation = i[2] as String,
                                textureLocation = i[3] as String,
                                animationFileLocation = i[4] as String,
                            )
                        )
                    )
                */
            }
        }
    }
}

