package yummyloop.common.integration.gecko

import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.world.World
import software.bernie.geckolib3.core.IAnimatable

abstract class AnimatableLivingEntity<T>(type: EntityType<T>, world: World?) :
    LivingEntity(type, world), IAnimatable where T : LivingEntity