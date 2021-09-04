package yummyloop.common.integration.gecko

import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.core.controller.AnimationController
import software.bernie.geckolib3.core.event.SoundKeyframeEvent

@Suppress("UNCHECKED_CAST")
class SoundListener<I>(private val predicate: (SoundKeyframeEvent<I>) -> Unit) :
    AnimationController.ISoundListener<I> where I : IAnimatable {

    override fun playSound(p0: SoundKeyframeEvent<I>) = predicate.invoke(p0)

}