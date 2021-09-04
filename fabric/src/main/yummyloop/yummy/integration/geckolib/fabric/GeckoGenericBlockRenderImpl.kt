package yummyloop.yummy.integration.geckolib.fabric

import net.minecraft.block.entity.BlockEntity
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.model.AnimatedGeoModel
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer

class GeckoGenericBlockRenderImpl<T>(
    model: AnimatedGeoModel<T>,
) : GeoBlockRenderer<T>(model) where T : BlockEntity, T : IAnimatable