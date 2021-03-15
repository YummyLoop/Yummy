package yummyloop.common.integration.gecko

import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import software.bernie.geckolib3.core.IAnimatable

abstract class AnimatableBlockEntity<T>(type: BlockEntityType<T>) :
    BlockEntity(type), IAnimatable where T : BlockEntity