package yummyloop.common.item

import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.core.manager.AnimationData
import software.bernie.geckolib3.core.manager.AnimationFactory

open class AnimatableBlockItem(block: Block, settings: Settings) : BlockItem(block, settings), IAnimatable {
    protected val animationFactory by lazy { AnimationFactory(this) }
    override fun getFactory(): AnimationFactory = this.animationFactory
    override fun registerControllers(data: AnimationData) {}
}