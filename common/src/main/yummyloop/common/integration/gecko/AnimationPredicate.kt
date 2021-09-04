package yummyloop.common.integration.gecko

import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.core.PlayState
import software.bernie.geckolib3.core.controller.AnimationController
import software.bernie.geckolib3.core.event.predicate.AnimationEvent

@Suppress("UNCHECKED_CAST")
class AnimationPredicate<I>(private val predicate: (AnimationEvent<I>) -> PlayState) :
    AnimationController.IAnimationPredicate<I> where I : IAnimatable {

    override fun test(p0: AnimationEvent<I>): PlayState {
        return (predicate).invoke(p0)
    }

}