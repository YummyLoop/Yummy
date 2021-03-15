package yummyloop.yummy.integration.geckolib.forge

import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.entity.LivingEntity
import net.minecraft.util.Identifier
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.model.AnimatedGeoModel
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer

class GeckoGenericEntityRenderImpl<T>(
    rendererDispatcherIn: EntityRenderDispatcher,
    var model: AnimatedGeoModel<T>,
) : GeoEntityRenderer<T>(rendererDispatcherIn, model) where T : LivingEntity, T : IAnimatable {
    init {
        this.shadowRadius = 0.7F
    }

    override fun getTexture(arg: T): Identifier {
        return model.getTextureLocation(arg)
    }
}