package yummyloop.common.block.entity

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.math.BlockPos
import software.bernie.geckolib3.core.IAnimatable

abstract class AnimatableBlockEntity(type: BlockEntityType<out BlockEntity>, pos: BlockPos?, state: BlockState?) :
    BlockEntity(type, pos, state), IAnimatable