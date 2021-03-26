package yummyloop.yummy.content.chest.doubleChest

import net.minecraft.block.enums.ChestType
import net.minecraft.util.Identifier

class DoubleChestModel(textureName: String = "chest") : AbstractChestModel<DoubleChestEntity>(textureName) {

    companion object {
        private val placebo = Triple(
            "geo/placebo.geo.json",
            "textures/block/placebo.png",
            "animations/placebo.animation.json"
        )
    }

    private val doubleChest = Triple(
        "geo/chest_double.geo.json",
        "textures/block/${textureName}_double.png",
        "animations/chest_double.animation.json"
    )

    override fun getModelLocation(obj: DoubleChestEntity): Identifier {
        try {
            val world = obj.world
            if (world != null) {
                val state = world.getBlockState(obj.pos)
                when (state.get(DoubleChestBlock.CHEST_TYPE)) {
                    ChestType.RIGHT -> return Identifier(modId, placebo.first)
                    ChestType.LEFT -> return Identifier(modId, doubleChest.first)
                    else -> {
                    }
                }
            }
        } catch (e: Exception) {
            //...
        }

        return super.getModelLocation(obj)
    }

    override fun getTextureLocation(obj: DoubleChestEntity): Identifier {
        try {
            val world = obj.world
            if (world != null) {
                val state = world.getBlockState(obj.pos)
                when (state.get(DoubleChestBlock.CHEST_TYPE)) {
                    ChestType.RIGHT -> return Identifier(modId, placebo.second)
                    ChestType.LEFT -> return Identifier(modId, doubleChest.second)
                    else -> {
                    }
                }
            }
        } catch (e: Exception) {
            //...
        }

        return super.getTextureLocation(obj)
    }


    override fun getAnimationFileLocation(obj: DoubleChestEntity): Identifier {
        try {
            val world = obj.world
            if (world != null) {
                val state = world.getBlockState(obj.pos)
                when (state.get(DoubleChestBlock.CHEST_TYPE)) {
                    ChestType.RIGHT -> return Identifier(modId, placebo.third)
                    ChestType.LEFT -> return Identifier(modId, doubleChest.third)
                    else -> {
                    }
                }
            }
        } catch (e: Exception) {
            //...
        }

        return super.getAnimationFileLocation(obj)
    }
}