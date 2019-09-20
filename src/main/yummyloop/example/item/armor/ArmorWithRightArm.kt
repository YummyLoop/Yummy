package yummyloop.example.item.armor

import net.minecraft.client.util.ModelIdentifier

interface ArmorWithRightArm {
    val rightArm : ModelIdentifier?
    val mirrorRightArm : Boolean
}