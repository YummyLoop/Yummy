package yummyloop.common.integration.gecko

import software.bernie.geckolib3.core.AnimationState
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.core.controller.AnimationController

/**
 * If animation matches the name and is stopped
 */
fun <T : IAnimatable> AnimationController<T>.isAnimationStopped(animationName: String): Boolean {
    if (this.currentAnimation != null) {
        if (this.animationState == AnimationState.Stopped) {
            if (this.currentAnimation.animationName == animationName) {
                return true
            }
        }
    }
    return false
}

fun <T : IAnimatable> AnimationController<T>.isCurrentAnimation(animationName: String): Boolean {
    if (this.currentAnimation != null) {
        if (this.currentAnimation.animationName == animationName) {
            return true
        }
    }
    return false
}