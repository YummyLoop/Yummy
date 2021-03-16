package yummyloop.common.integration.gecko

import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import software.bernie.geckolib3.core.IAnimatable

abstract class AnimatableBlockItem(block: Block, settings: Settings) : BlockItem(block, settings), IAnimatable