package net.examplemod.block

import me.shedaniel.architectury.registry.RegistrySupplier
import net.examplemod.ExampleMod
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.core.PlayState
import software.bernie.geckolib3.core.builder.AnimationBuilder
import software.bernie.geckolib3.core.controller.AnimationController
import software.bernie.geckolib3.core.event.predicate.AnimationEvent
import software.bernie.geckolib3.core.manager.AnimationData
import software.bernie.geckolib3.core.manager.AnimationFactory

class TestBlockEntity : BlockEntity(type?.get()), IAnimatable{
    companion object {
        var type: RegistrySupplier<BlockEntityType<BlockEntity>>? = null
    }
    private val animationFactory = AnimationFactory(this)
    override fun getFactory(): AnimationFactory = this.animationFactory

    init {
        ExampleMod.log.info("Calling from TestBlockEntity")

    }

    private fun <P : IAnimatable> predicate(event: AnimationEvent<P>): PlayState {
        event.controller.setAnimation(AnimationBuilder().addAnimation("Soaryn_chest_popup", true))
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