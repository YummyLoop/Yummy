package yummyloop.yummy.integration.geckolib.forge

import me.shedaniel.architectury.registry.BlockEntityRenderers
import me.shedaniel.architectury.registry.RegistrySupplier
import me.shedaniel.architectury.registry.entity.EntityRenderers
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import software.bernie.geckolib3.item.GeoArmorItem
import software.bernie.geckolib3.model.AnimatedGeoModel
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer
import yummyloop.common.integration.gecko.AnimatableArmor
import yummyloop.common.integration.gecko.AnimatableBlockEntity
import yummyloop.common.integration.gecko.AnimatableItem
import yummyloop.common.integration.gecko.AnimatableLivingEntity
import yummyloop.yummy.integration.geckolib.GeckoUtils
import java.util.concurrent.Callable
import kotlin.reflect.KFunction1

internal object GeckoUtilsImpl {

    /**
     * Registers all the gecko renderers, stored for late registry
     */
    fun registerAll() {
        for (i in GeckoUtils.geckoEntryList) {
            when (i.type) {
                GeckoUtils.GeckoType.Armor -> Items.registerArmorRenderer(i)
                GeckoUtils.GeckoType.Block -> Blocks.registerBlockRenderer(i)
                GeckoUtils.GeckoType.Entity -> Entities.registerEntityRenderer(i)
                else -> continue
            }
        }
        GeckoUtils.geckoEntryList.clear()
    }

    object Items {
        /**
         * Registers a Armor Renderer
         */
        @Suppress("UNCHECKED_CAST")
        fun registerArmorRenderer(i: GeckoUtils.Entry) {
            GeoArmorRenderer.registerArmorRenderer(
                ((i.obj as RegistrySupplier<Item>).get() as GeoArmorItem).javaClass,
                (GeckoGenericArmorRendererImpl(i.model as AnimatedGeoModel<AnimatableArmor>))
            )
        }
    }

    object Blocks {
        /**
         * Registers a Block Entity Renderer
         */
        @Suppress("UNCHECKED_CAST")
        fun registerBlockRenderer(i: GeckoUtils.Entry) {
            BlockEntityRenderers.registerRenderer(
                (i.obj as RegistrySupplier<BlockEntityType<BlockEntity>>).get()) {
                GeckoGenericBlockRenderImpl(it, i.model as AnimatedGeoModel<AnimatableBlockEntity>)
            }
        }
    }

    object Entities {
        /**
         * Registers a Entity Renderer
         */
        @Suppress("UNCHECKED_CAST")
        fun registerEntityRenderer(i: GeckoUtils.Entry) {
            EntityRenderers.register(
                (i.obj as RegistrySupplier<EntityType<AnimatableLivingEntity<LivingEntity>>>).get()) {
                GeckoGenericEntityRenderImpl(it, i.model as AnimatedGeoModel<AnimatableLivingEntity<LivingEntity>>)
            }
        }
    }

    /**
     * Returns the item supplier after applying the gecko item renderer with forge
     */
    @JvmStatic
    fun geckoItemSettings(
        itemSettings: Item.Settings,
        model: AnimatedGeoModel<out AnimatableItem>,
    ): Item.Settings {
        return itemSettings.setISTER { Callable { GeckoGenericItemRendererImpl(model) } }
    }
}

