package yummyloop.common.integration.gecko

import software.bernie.geckolib3.core.AnimationState
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.core.builder.AnimationBuilder
import software.bernie.geckolib3.core.event.predicate.AnimationEvent

fun <T : IAnimatable> AnimationEvent<T>.isAnimationStopped(): Boolean {
    return if (this.controller.currentAnimation == null) true else this.controller.animationState == AnimationState.Stopped
}

fun <T : IAnimatable> AnimationEvent<T>.isAnimationRunning(): Boolean {
    return if (this.controller.currentAnimation == null) false else this.controller.animationState == AnimationState.Running
}

fun <T : IAnimatable> AnimationEvent<T>.isAnimationTransitioning(): Boolean {
    return if (this.controller.currentAnimation == null) false else this.controller.animationState == AnimationState.Transitioning
}

fun <T : IAnimatable> AnimationEvent<T>.isCurrentAnimation(animationName: String): Boolean {
    return if (this.controller.currentAnimation == null) false else this.controller.currentAnimation.animationName == animationName
}

fun <T : IAnimatable> AnimationEvent<T>.isController(controllerName : String): Boolean {
    return this.controller.name == controllerName
}

fun <T : IAnimatable> AnimationEvent<T>.setAnimation(animationName : String) {
    this.controller.setAnimation(AnimationBuilder().addAnimation(animationName))
}

fun <T : IAnimatable> AnimationEvent<T>.setLoopingAnimation(animationName : String) {
    this.controller.setAnimation(AnimationBuilder().addAnimation(animationName, true))
}