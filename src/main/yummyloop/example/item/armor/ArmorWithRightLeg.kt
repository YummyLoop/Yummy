package yummyloop.example.item.armor

import net.minecraft.client.util.ModelIdentifier

interface ArmorWithRightLeg {
    val rightLeg : ModelIdentifier?
    val mirrorRightLeg : Boolean
}