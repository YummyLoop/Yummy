package yummyloop.yummy.content.chest.doubleChest

import net.minecraft.block.Block
import software.bernie.geckolib3.core.PlayState
import software.bernie.geckolib3.core.builder.AnimationBuilder
import software.bernie.geckolib3.core.controller.AnimationController
import software.bernie.geckolib3.core.event.predicate.AnimationEvent
import software.bernie.geckolib3.core.manager.AnimationData
import yummyloop.common.integration.gecko.AnimationPredicate
import yummyloop.common.item.AnimatableBlockItem

open class DoubleChestItem(block: Block, settings: Settings) : AnimatableBlockItem(block, settings) {

    protected open fun <P> predicate(event: AnimationEvent<P>): PlayState where P : DoubleChestItem {
        val animationBuilder = AnimationBuilder()
        animationBuilder.addAnimation("idle", true)
        event.controller.setAnimation(animationBuilder)
        return PlayState.CONTINUE
    }

    override fun registerControllers(data: AnimationData) {
        val animationController = AnimationController(
            this,
            "controller",
            1F,
            AnimationPredicate(this::predicate)
        )
        data.addAnimationController(animationController)
        animationController.markNeedsReload()
    }
}