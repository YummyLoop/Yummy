package net.examplemod.integration.geckolib.fabric

import me.shedaniel.architectury.registry.RegistrySupplier
import net.examplemod.integration.geckolib.GeckoUtils
import net.minecraft.item.ArmorItem
import net.minecraft.item.Item
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.item.GeoArmorItem
import software.bernie.geckolib3.renderer.geo.GeoArmorRenderer
import software.bernie.geckolib3.renderer.geo.GeoItemRenderer
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
                    GeckoUtils.GeckoType.Item -> registerItemRenderer(
                        @Suppress("UNCHECKED_CAST") (i.second[0] as RegistrySupplier<Item>),
                        modID = i.second[1] as String,
                        modelLocation = i.second[2] as String,
                        textureLocation = i.second[3] as String,
                        animationFileLocation = i.second[4] as String)
                    GeckoUtils.GeckoType.Armor -> registerArmorRender(i.second)
                }
            }
            GeckoUtils.geckoList.clear()
        }

        private fun registerItemRenderer(
            item: RegistrySupplier<Item>,
            modID: String,
            modelLocation: String,
            textureLocation: String,
            animationFileLocation: String,
        ): Unit = GeoItemRenderer.registerItemRenderer(
            item.get(),
            GenericItemRendererImpl(
                GeckoUtils.GenericModel(
                    modID,
                    modelLocation,
                    textureLocation,
                    animationFileLocation
                )
            )
        )

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

    @Suppress("UNUSED_PARAMETER")
    @JvmStatic
    fun <I> geckoItemSupplier(
        itemFunc: KFunction1<Item.Settings, I>,
        itemSettings: Item.Settings,
        modID: String,
        modelLocation: String,
        textureLocation: String,
        animationFileLocation: String,
    ): Supplier<out I> where I : Item, I : IAnimatable {
        return Supplier { itemFunc(itemSettings) }
    }
}

