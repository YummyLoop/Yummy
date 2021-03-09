package yummyloop.yummy.integration.geckolib.fabric

import me.shedaniel.architectury.registry.BlockEntityRenderers
import me.shedaniel.architectury.registry.RegistrySupplier
import me.shedaniel.architectury.registry.entity.EntityRenderers
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ArmorItem
import net.minecraft.item.Item
import net.minecraft.world.World
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.item.GeoArmorItem
import software.bernie.geckolib3.renderer.geo.GeoArmorRenderer
import software.bernie.geckolib3.renderer.geo.GeoBlockRenderer
import software.bernie.geckolib3.renderer.geo.GeoEntityRenderer
import software.bernie.geckolib3.renderer.geo.GeoItemRenderer
import yummyloop.yummy.integration.geckolib.GeckoUtils
import java.util.function.Supplier
import kotlin.reflect.KFunction1

internal object GeckoUtilsImpl {

    fun registerAll() {
        for (i in GeckoUtils.geckoList) {
            when (i.first) {
                GeckoUtils.GeckoType.Item -> Items.registerItemRenderer(i.second)
                GeckoUtils.GeckoType.Armor -> Items.registerArmorRenderer(i.second)
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

        fun registerItemRenderer(i: Array<Any>) {
            GeoItemRenderer.registerItemRenderer(
                @Suppress("UNCHECKED_CAST") (i[0] as RegistrySupplier<Item>).get(),
                GenericItemRendererImpl(
                    GeckoUtils.GenericModel(
                        modID = i[1] as String,
                        modelLocation = i[2] as String,
                        textureLocation = i[3] as String,
                        animationFileLocation = i[4] as String
                    )
                )
            )
        }

        fun registerArmorRenderer(i: Array<Any>) {
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
            gModel: GeckoUtils.GenericModel<T?>,
        ) : GeoEntityRenderer<T>(rendererDispatcherIn, gModel) where T : LivingEntity?, T : IAnimatable? {
            init {
                this.shadowRadius = 0.7F
            }
        }

        private abstract class GeckoEntity(type: EntityType<out LivingEntity>?, worldIn: World?) :
            LivingEntity(type, worldIn), IAnimatable

        fun registerEntityRenderer(i: Array<Any>) {
            EntityRenderers.register(
                @Suppress("UNCHECKED_CAST") (i[0] as RegistrySupplier<EntityType<GeckoEntity>>).get()
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

