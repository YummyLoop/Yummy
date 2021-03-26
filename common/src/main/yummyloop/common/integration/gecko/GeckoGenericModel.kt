package yummyloop.common.integration.gecko

import net.minecraft.util.Identifier
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.model.AnimatedGeoModel

/**
 * Generic Model for gecko
 *
 * @param modID Mod id
 * @param modelLocation Model location
 * @param textureLocation Texture location
 * @param animationFileLocation Animation location
 */
open class GeckoGenericModel<T>(
    private val modID: String,
    private val modelLocation: String,
    private val textureLocation: String,
    private val animationFileLocation: String,
) : AnimatedGeoModel<T>() where T : IAnimatable? { // Crashes if it has a null check
    override fun getModelLocation(obj: T) = Identifier(modID, modelLocation)
    override fun getTextureLocation(obj: T) = Identifier(modID, textureLocation)
    override fun getAnimationFileLocation(obj: T) = Identifier(modID, animationFileLocation)

    companion object {
        fun <T> item(
            modID: String,
            itemID: String,
        ): GeckoGenericModel<T> where T : IAnimatable {
            return GeckoGenericModel(
                modID,
                modelLocation = "geo/$itemID.geo.json",
                textureLocation = "textures/item/$itemID.png",
                animationFileLocation = "animations/$itemID.animation.json",
            )
        }

        fun <T> armor(
            modID: String,
            itemID: String,
        ): GeckoGenericModel<T> where T : IAnimatable {
            return item(modID, itemID)
        }

        fun <T> block(
            modID: String,
            id: String,
        ): GeckoGenericModel<T> where T : IAnimatable {
            return GeckoGenericModel(
                modID,
                modelLocation = "geo/$id.geo.json",
                textureLocation = "textures/block/$id.png",
                animationFileLocation = "animations/$id.animation.json",
            )
        }

        fun <T> entity(
            modID: String,
            id: String,
        ): GeckoGenericModel<T> where T : IAnimatable {
            return GeckoGenericModel(
                modID,
                modelLocation = "geo/$id.geo.json",
                textureLocation = "textures/model/$id.png",
                animationFileLocation = "animations/$id.animation.json",
            )
        }
    }
}