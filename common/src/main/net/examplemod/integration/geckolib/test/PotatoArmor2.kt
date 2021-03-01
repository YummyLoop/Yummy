package net.examplemod.integration.geckolib.test

import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.ItemGroup
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.core.PlayState
import software.bernie.geckolib3.core.builder.AnimationBuilder
import software.bernie.geckolib3.core.controller.AnimationController
import software.bernie.geckolib3.core.event.predicate.AnimationEvent
import software.bernie.geckolib3.core.manager.AnimationData
import software.bernie.geckolib3.core.manager.AnimationFactory
import software.bernie.geckolib3.item.GeoArmorItem

class PotatoArmor2(
    materialIn: ArmorMaterial,
    slot: EquipmentSlot,
    builder: Settings,
) : GeoArmorItem(materialIn, slot, builder.group(ItemGroup.COMBAT)), IAnimatable {
    private val animationFactory = AnimationFactory(this)
    override fun getFactory(): AnimationFactory = this.animationFactory

    private fun <P : IAnimatable?> predicate(event: AnimationEvent<P>): PlayState {
        event.controller.setAnimation(AnimationBuilder().addAnimation("animation.potato_armor.new", true))
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