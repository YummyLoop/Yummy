package yummyloop.common.integration.gecko

import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.core.controller.AnimationController
import software.bernie.geckolib3.core.event.SoundKeyframeEvent

@Suppress("UNCHECKED_CAST")
class SoundListener<I>(private val predicate: (SoundKeyframeEvent<I>) -> Unit) :
    AnimationController.ISoundListener where I : IAnimatable {

    override fun <A> playSound(event: SoundKeyframeEvent<A>) where A : IAnimatable {
        (predicate as (SoundKeyframeEvent<A>) -> Unit).invoke(event)
    }

}