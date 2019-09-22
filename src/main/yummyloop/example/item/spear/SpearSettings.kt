package yummyloop.example.item.spear

import net.minecraft.item.Item
import net.minecraft.item.ItemGroup

object SpearSettings {
    object Wooden{
        const val attackDamage = 4.5F
        const val entityAttackDamage = attackDamage
        const val attackSpeed = 1.1F
        const val velocityMod = 0.85F
        val itemSettings: Item.Settings = Item.Settings().group(ItemGroup.COMBAT).maxDamage(60)
    }
}