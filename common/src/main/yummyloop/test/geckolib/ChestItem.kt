package yummyloop.test.geckolib

import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.core.PlayState
import software.bernie.geckolib3.core.builder.AnimationBuilder
import software.bernie.geckolib3.core.controller.AnimationController
import software.bernie.geckolib3.core.event.predicate.AnimationEvent
import software.bernie.geckolib3.core.manager.AnimationData
import software.bernie.geckolib3.core.manager.AnimationFactory
import yummyloop.common.integration.gecko.AnimatableItem

class ChestItem(properties: Settings) : AnimatableItem(properties) {
    private val animationFactory = AnimationFactory(this)
    override fun getFactory(): AnimationFactory = this.animationFactory

    private fun <P : IAnimatable> predicate(event: AnimationEvent<P>): PlayState {
        event.controller.setAnimation(AnimationBuilder().addAnimation("open_chest", true))
        return PlayState.CONTINUE
    }

    override fun registerControllers(data: AnimationData) {
        val animationPredicate = object : AnimationController.IAnimationPredicate<IAnimatable> {
            override fun <P : IAnimatable> test(event: AnimationEvent<P>): PlayState = predicate(event)
        }
        data.addAnimationController(
            AnimationController(
                this,
                "controller",
                20F,
                animationPredicate
            )
        )
    }
}