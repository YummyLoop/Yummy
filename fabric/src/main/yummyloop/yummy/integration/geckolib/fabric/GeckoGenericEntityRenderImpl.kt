package yummyloop.yummy.integration.geckolib.fabric

import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.entity.LivingEntity
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.model.AnimatedGeoModel
import software.bernie.geckolib3.renderer.geo.GeoEntityRenderer

class GeckoGenericEntityRenderImpl<T>(
    rendererDispatcherIn: EntityRenderDispatcher,
    model: AnimatedGeoModel<T>,
) : GeoEntityRenderer<T>(rendererDispatcherIn, model) where T : LivingEntity, T : IAnimatable {
    init {
        this.shadowRadius = 0.7F
    }
}