package yummyloop.common.item

import net.minecraft.item.Item
import software.bernie.geckolib3.core.IAnimatable

abstract class AnimatableItem(settings: Settings) :
    Item(settings), IAnimatable