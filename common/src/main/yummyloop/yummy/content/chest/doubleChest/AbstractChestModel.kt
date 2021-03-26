package yummyloop.yummy.content.chest.doubleChest

import net.minecraft.util.Identifier
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.model.AnimatedGeoModel
import yummyloop.yummy.ExampleMod

abstract class AbstractChestModel<T>(textureName: String = "chest") : AnimatedGeoModel<T>() where T : IAnimatable {

    companion object {
        const val modId = ExampleMod.MOD_ID
        private const val geoModelLocation = "geo/chest.geo.json"
        private const val geoModelAnimation = "animations/chest.animation.json"
    }

    private val textureLocation = "textures/block/${textureName}.png"

    override fun getModelLocation(obj: T): Identifier {
        return Identifier(modId, geoModelLocation)
    }

    override fun getTextureLocation(obj: T): Identifier {
        return Identifier(modId, textureLocation)
    }

    override fun getAnimationFileLocation(obj: T): Identifier {
        return Identifier(modId, geoModelAnimation)
    }
}