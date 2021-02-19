package net.examplemod.integration.geckolib.forge

import me.shedaniel.architectury.registry.RegistrySupplier
import net.examplemod.integration.geckolib.GeckoUtils
import net.minecraft.item.ArmorItem
import net.minecraft.item.Item
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.item.GeoArmorItem
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer
import java.util.concurrent.Callable
import java.util.function.Supplier
import kotlin.reflect.KFunction1


object GeckoUtilsImpl {
    object Items {
        open class GenericItemRendererImpl<T>(gModel: GeckoUtils.GenericModel<T>) :
            GeoItemRenderer<T>(gModel) where T : IAnimatable, T : Item

        open class GenericArmorRendererImpl<T>(gModel: GeckoUtils.GenericModel<T>) :
            GeoArmorRenderer<T>(gModel) where T : IAnimatable, T : GeoArmorItem {
            init {
                //These values are what each bone name is in blockbench. So if your head bone is named "bone545",
                // make sure to do this.headBone = "bone545";

                // The default values are the ones that come with the default armor template in the geckolib blockbench plugin.
                this.headBone = "helmet"
                this.bodyBone = "chestplate"
                this.rightArmBone = "rightArm"
                this.leftArmBone = "leftArm"
                this.rightLegBone = "rightLeg"
                this.leftLegBone = "leftLeg"
                this.rightBootBone = "rightBoot"
                this.leftBootBone = "leftBoot"
            }
        }

        fun registerAll() {
            for (i in GeckoUtils.geckoList) {
                when (i.first) {
                    GeckoUtils.GeckoType.Item -> continue
                    GeckoUtils.GeckoType.Armor -> registerArmorRender(i.second)
                }
            }
            GeckoUtils.geckoList.clear()
        }

        private fun registerArmorRender(i: Array<Any>) {
            GeoArmorRenderer.registerArmorRenderer(
                @Suppress("UNCHECKED_CAST") ((i[0] as RegistrySupplier<Item>).get() as ArmorItem).javaClass,
                GenericArmorRendererImpl(
                    GeckoUtils.GenericModel(
                        modID = i[1] as String,
                        modelLocation = i[2] as String,
                        textureLocation = i[3] as String,
                        animationFileLocation = i[4] as String
                    )
                )
            )
        }
    }


    @JvmStatic
    fun <I> geckoItemSupplier(
        itemFunc: KFunction1<Item.Settings, I>,
        itemSettings: Item.Settings,
        modID: String,
        modelLocation: String,
        textureLocation: String,
        animationFileLocation: String,
    ): Supplier<out I> where I : Item, I : IAnimatable {
        val itemSupplier = itemFunc(itemSettings.setISTER {
            Callable {
                Items.GenericItemRendererImpl(
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

