package yummyloop.yummy.content.chest.doubleChest

import net.minecraft.block.enums.ChestType
import net.minecraft.util.Identifier
import software.bernie.geckolib3.model.AnimatedGeoModel
import yummyloop.yummy.ExampleMod

class DoubleChestModel : AnimatedGeoModel<DoubleChestEntity>() {
    private val modId = ExampleMod.MOD_ID
    private val singleChest = Triple(
        "geo/single_chest.geo.json",
        "textures/block/single_chest.png",
        "animations/single_chest.animation.json"
    )

    private val doubleChest = Triple(
        "geo/double_chest.geo.json",
        "textures/block/double_chest.png",
        "animations/double_chest.animation.json"
    )

    private val empty = Triple(
        "geo/empty.geo.json",
        "textures/block/empty.png",
        "animations/empty.animation.json"
    )

    override fun getModelLocation(obj: DoubleChestEntity): Identifier {
        val world = obj.world
        if (world != null) {
            val state = world.getBlockState(obj.pos)
            try {
                when (state.get(DoubleChestBlock.CHEST_TYPE)) {
                    ChestType.RIGHT -> return Identifier(modId, empty.first)
                    ChestType.LEFT -> return Identifier(modId, doubleChest.first)
                    else -> {
                    }
                }
            } catch (e: Exception) {
                //...
            }
        }
        return Identifier(modId, singleChest.first)
    }

    override fun getTextureLocation(obj: DoubleChestEntity): Identifier {
        val world = obj.world
        if (world != null) {
            val state = world.getBlockState(obj.pos)
            try {
                when (state.get(DoubleChestBlock.CHEST_TYPE)) {
                    ChestType.RIGHT -> return Identifier(modId, empty.second)
                    ChestType.LEFT -> return Identifier(modId, doubleChest.second)
                    else -> {
                    }
                }
            } catch (e: Exception) {
                //...
            }
        }
        return Identifier(modId, singleChest.second)
    }


    override fun getAnimationFileLocation(obj: DoubleChestEntity): Identifier {
        val world = obj.world
        if (world != null) {
            val state = world.getBlockState(obj.pos)
            try {
                when (state.get(DoubleChestBlock.CHEST_TYPE)) {
                    ChestType.RIGHT -> return Identifier(modId, empty.third)
                    ChestType.LEFT -> return Identifier(modId, doubleChest.third)
                    else -> {
                    }
                }
            } catch (e: Exception) {
                //...
            }
        }
        return Identifier(modId, singleChest.third)
    }
}