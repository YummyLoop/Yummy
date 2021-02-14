package net.examplemod.integration.geckolib;

import net.minecraft.item.Item;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class JackInTheBoxItem2 extends Item implements IAnimatable {
    public AnimationFactory factory = new AnimationFactory(this);

    public JackInTheBoxItem2(Settings properties) {
        super(properties);
    }

    public static <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("Soaryn_chest_popup", true));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(
                this,
                "controller",
                20,
                JackInTheBoxItem2::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}