package net.examplemod.integration.geckolib.forge

import me.shedaniel.architectury.registry.BlockEntityRenderers
import me.shedaniel.architectury.registry.RegistrySupplier
import net.examplemod.integration.geckolib.GeckoUtils
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ArmorItem
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.world.World
import net.minecraftforge.fml.client.registry.RenderingRegistry
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.item.GeoArmorItem
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer
import java.util.concurrent.Callable
import java.util.function.Supplier
import kotlin.reflect.KFunction1


internal object GeckoUtilsImpl {

    fun registerAll() {
        for (i in GeckoUtils.geckoList) {
            when (i.first) {
                GeckoUtils.GeckoType.Item -> continue
                GeckoUtils.GeckoType.Armor -> Items.registerArmorRender(i.second)
                GeckoUtils.GeckoType.Block -> Blocks.registerBlockRenderer(i.second)
                GeckoUtils.GeckoType.Entity -> Entities.registerEntityRenderer(i.second)
                else -> continue
            }
        }
        GeckoUtils.geckoList.clear()
    }

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

        fun registerArmorRender(i: Array<Any>) {
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

    object Blocks {
        class GenericBlockRenderImpl<T>(
            rendererDispatcherIn: BlockEntityRenderDispatcher?,
            gModel: GeckoUtils.GenericModel<T?>,
        ) : GeoBlockRenderer<T>(rendererDispatcherIn, gModel) where T : BlockEntity?, T : IAnimatable?

        fun registerBlockRenderer(i: Array<Any>) {
            BlockEntityRenderers.registerRenderer(
                @Suppress("UNCHECKED_CAST") (i[0] as RegistrySupplier<BlockEntityType<BlockEntity>>).get()
            ) {
                GenericBlockRenderImpl(it,
                    GeckoUtils.GenericModel(
                        modID = i[1] as String,
                        modelLocation = i[2] as String,
                        textureLocation = i[3] as String,
                        animationFileLocation = i[4] as String
                    )
                )
            }
        }
    }

    object Entities {
        class GenericEntityRenderImpl<T>(
            rendererDispatcherIn: EntityRenderDispatcher?,
            var gModel: GeckoUtils.GenericModel<T?>,
        ) : GeoEntityRenderer<T>(rendererDispatcherIn, gModel) where T : LivingEntity?, T : IAnimatable? {
            init {
                this.shadowRadius = 0.7F
            }

            override fun getTexture(arg: T): Identifier {
                return gModel.getTextureLocation(arg)
            }
        }

        private abstract class GeckoEntity(type: EntityType<out LivingEntity>?, worldIn: World?) : LivingEntity(type, worldIn), IAnimatable

        fun registerEntityRenderer(i: Array<Any>) {
            RenderingRegistry.registerEntityRenderingHandler(
                @Suppress("UNCHECKED_CAST") (i[0] as RegistrySupplier<EntityType<GeckoEntity>> ).get()
            ) {
                GenericEntityRenderImpl(it,
                    GeckoUtils.GenericModel(
                        modID = i[1] as String,
                        modelLocation = i[2] as String,
                        textureLocation = i[3] as String,
                        animationFileLocation = i[4] as String
                    )
                )
            }
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

