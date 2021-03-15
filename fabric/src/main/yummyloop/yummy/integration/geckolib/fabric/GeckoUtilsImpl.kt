package yummyloop.yummy.integration.geckolib.fabric

import me.shedaniel.architectury.registry.BlockEntityRenderers
import me.shedaniel.architectury.registry.RegistrySupplier
import me.shedaniel.architectury.registry.entity.EntityRenderers
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.Item
import net.minecraft.world.World
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.item.GeoArmorItem
import software.bernie.geckolib3.model.AnimatedGeoModel
import software.bernie.geckolib3.renderer.geo.GeoArmorRenderer
import software.bernie.geckolib3.renderer.geo.GeoBlockRenderer
import software.bernie.geckolib3.renderer.geo.GeoEntityRenderer
import software.bernie.geckolib3.renderer.geo.GeoItemRenderer
import yummyloop.yummy.integration.geckolib.GeckoGenericModel
import yummyloop.yummy.integration.geckolib.GeckoUtils
import java.util.function.Supplier
import kotlin.reflect.KFunction1

internal object GeckoUtilsImpl {

    fun registerAll() {
        for (i in GeckoUtils.geckoEntryList){
            when (i.type){
                GeckoUtils.GeckoType.Item -> Items.registerItemRenderer(i)
                GeckoUtils.GeckoType.Armor -> Items.registerArmorRenderer(i)
                GeckoUtils.GeckoType.Block -> Blocks.registerBlockRenderer(i)
                GeckoUtils.GeckoType.Entity -> Entities.registerEntityRenderer(i)
                else -> continue
            }
        }
        GeckoUtils.geckoEntryList.clear()
    }

    object Items {
        open class GenericItemRendererImpl<T>(gModel: AnimatedGeoModel<T>) :
            GeoItemRenderer<T>(gModel) where T : IAnimatable, T : Item

        open class GenericArmorRendererImpl<T>(gModel: AnimatedGeoModel<T>) :
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


        abstract class AnimatableItem(settings: Settings?) : Item(settings), IAnimatable
        fun registerItemRenderer(i: GeckoUtils.Entry<*>){
            GeoItemRenderer.registerItemRenderer(
                @Suppress("UNCHECKED_CAST") (i.obj as RegistrySupplier<Item>).get(),
                @Suppress("UNCHECKED_CAST") GenericItemRendererImpl(i.model as AnimatedGeoModel<AnimatableItem> )
            )
        }

        abstract class AnimatableArmor(materialIn: ArmorMaterial, slot: EquipmentSlot, builder: Settings,) : GeoArmorItem(materialIn, slot, builder), IAnimatable
        fun registerArmorRenderer(i: GeckoUtils.Entry<*>){
            GeoArmorRenderer.registerArmorRenderer(
                @Suppress("UNCHECKED_CAST") ((i.obj as RegistrySupplier<Item>).get() as GeoArmorItem).javaClass,
                @Suppress("UNCHECKED_CAST") GenericArmorRendererImpl(i.model as AnimatedGeoModel<AnimatableArmor>)
            )
        }
    }

    object Blocks {
        class GenericBlockRenderImpl<T>(
            rendererDispatcherIn: BlockEntityRenderDispatcher?,
            gModel: AnimatedGeoModel<T>,
        ) : GeoBlockRenderer<T>(rendererDispatcherIn, gModel) where T : BlockEntity, T : IAnimatable

        abstract class AnimatableBlockEntity(type: BlockEntityType<*>) : BlockEntity(type), IAnimatable
        fun registerBlockRenderer(i: GeckoUtils.Entry<*>) {
            BlockEntityRenderers.registerRenderer(
                @Suppress("UNCHECKED_CAST") (i.obj as RegistrySupplier<BlockEntityType<BlockEntity>>).get()
            ) {
                GenericBlockRenderImpl(it,
                    @Suppress("UNCHECKED_CAST") (i.model as AnimatedGeoModel<AnimatableBlockEntity>)
                )
            }
        }
    }

    object Entities {
        class GenericEntityRenderImpl<T>(
            rendererDispatcherIn: EntityRenderDispatcher?,
            gModel: AnimatedGeoModel<T>,
        ) : GeoEntityRenderer<T>(rendererDispatcherIn, gModel) where T : LivingEntity?, T : IAnimatable? {
            init {
                this.shadowRadius = 0.7F
            }
        }

        private abstract class GeckoEntity(type: EntityType<out LivingEntity>?, worldIn: World?) :
            LivingEntity(type, worldIn), IAnimatable

        fun registerEntityRenderer(i: GeckoUtils.Entry<*>) {
            EntityRenderers.register(
                @Suppress("UNCHECKED_CAST") (i.obj as RegistrySupplier<EntityType<GeckoEntity>>).get()
            ) {
                GenericEntityRenderImpl(it,
                    @Suppress("UNCHECKED_CAST") (i.model as AnimatedGeoModel<GeckoEntity>)
                )
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    @JvmStatic
    fun <I> geckoItemSupplier(
        itemFunc: KFunction1<Item.Settings, I>,
        itemSettings: Item.Settings,
        model : AnimatedGeoModel<I>
    ): Supplier<out I> where I : Item, I : IAnimatable {
        return Supplier { itemFunc(itemSettings) }
    }
}

