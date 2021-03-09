package yummyloop.test.geckolib

import me.shedaniel.architectury.registry.RegistrySupplier
import net.minecraft.entity.EntityType
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.world.World
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.core.PlayState
import software.bernie.geckolib3.core.builder.AnimationBuilder
import software.bernie.geckolib3.core.controller.AnimationController
import software.bernie.geckolib3.core.event.predicate.AnimationEvent
import software.bernie.geckolib3.core.manager.AnimationData
import software.bernie.geckolib3.core.manager.AnimationFactory


class GeoExampleEntity2(
    itype: EntityType<out PathAwareEntity?>?,
    worldIn: World?,
) : PathAwareEntity(itype, worldIn), IAnimatable {
    companion object {
        var type: RegistrySupplier<EntityType<PathAwareEntity>>? = null

        fun createAttributes(): DefaultAttributeContainer.Builder {
            return createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
        }
    }

    private val animationFactory = AnimationFactory(this)
    override fun getFactory(): AnimationFactory = this.animationFactory

    private fun <E : IAnimatable?> predicate(event: AnimationEvent<E>): PlayState {
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
                0F,
                animationPredicate
            )
        )
    }

    init {
        this.ignoreCameraFrustum = true
    }
}