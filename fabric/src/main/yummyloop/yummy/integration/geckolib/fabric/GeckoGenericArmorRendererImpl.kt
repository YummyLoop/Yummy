package yummyloop.yummy.integration.geckolib.fabric

import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.item.GeoArmorItem
import software.bernie.geckolib3.model.AnimatedGeoModel
import software.bernie.geckolib3.renderer.geo.GeoArmorRenderer

open class GeckoGenericArmorRendererImpl<T>(model: AnimatedGeoModel<T>) :
    GeoArmorRenderer<T>(model) where T : IAnimatable, T : GeoArmorItem {
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