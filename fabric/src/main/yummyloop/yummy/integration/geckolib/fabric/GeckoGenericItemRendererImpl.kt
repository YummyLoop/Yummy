package yummyloop.yummy.integration.geckolib.fabric

import net.minecraft.item.Item
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.model.AnimatedGeoModel
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer

open class GeckoGenericItemRendererImpl<T>(model: AnimatedGeoModel<T>) :
    GeoItemRenderer<T>(model) where T : IAnimatable, T : Item