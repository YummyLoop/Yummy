package yummyloop.yummy.integration.geckolib.fabric

import net.minecraft.client.render.entity.model.EntityModelLayer
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.item.GeoArmorItem
import software.bernie.geckolib3.model.AnimatedGeoModel
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer
import software.bernie.geckolib3.util.GeoArmorRendererFactory

open class GeckoGenericArmorRendererImpl<T>(model: AnimatedGeoModel<T>, ctx: GeoArmorRendererFactory.Context?,
                                            layer: EntityModelLayer?
) :
    GeoArmorRenderer<T>(model, ctx, layer) where T : IAnimatable, T : GeoArmorItem {
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