package yummyloop.yummy.integration.geckolib.fabric

import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.model.AnimatedGeoModel
import software.bernie.geckolib3.renderer.geo.GeoBlockRenderer

class GeckoGenericBlockRenderImpl<T>(
    rendererDispatcherIn: BlockEntityRenderDispatcher,
    model: AnimatedGeoModel<T>,
) : GeoBlockRenderer<T>(rendererDispatcherIn, model) where T : BlockEntity, T : IAnimatable