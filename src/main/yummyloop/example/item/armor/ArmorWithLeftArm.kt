package yummyloop.example.item.armor

import net.minecraft.client.util.ModelIdentifier

interface ArmorWithLeftArm {
    val leftArm : ModelIdentifier?
    val mirrorLeftArm : Boolean
}