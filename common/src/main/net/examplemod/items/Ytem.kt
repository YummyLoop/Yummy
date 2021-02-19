package net.examplemod.items

import me.shedaniel.architectury.registry.DeferredRegister
import me.shedaniel.architectury.registry.RegistrySupplier
import net.examplemod.ExampleMod
import net.minecraft.item.FoodComponent
import net.minecraft.item.ItemGroup
import net.minecraft.util.Rarity
import net.minecraft.util.registry.Registry
import java.util.function.Supplier
import net.minecraft.item.Item as VanillaItem

class Ytem(settings: VanillaItem.Settings) : VanillaItem(settings) {
    companion object {
        private val deferredRegister: DeferredRegister<VanillaItem> =
            DeferredRegister.create(ExampleMod.MOD_ID, Registry.ITEM_KEY)

        fun register(itemId: String, itemSupplier: Supplier<out VanillaItem>): RegistrySupplier<VanillaItem> =
            deferredRegister.register(itemId, itemSupplier)

        fun register() = deferredRegister.register()
    }

    class Settings : VanillaItem.Settings() {
        override fun food(foodComponent: FoodComponent): Settings {
            super.food(foodComponent); return this
        }

        override fun maxCount(maxCount: Int): Settings {
            super.maxCount(maxCount); return this
        }

        override fun maxDamageIfAbsent(maxDamage: Int): Settings {
            super.maxDamageIfAbsent(maxDamage); return this
        }

        override fun maxDamage(maxDamage: Int): Settings {
            super.maxDamage(maxDamage); return this
        }

        override fun recipeRemainder(recipeRemainder: VanillaItem): Settings {
            super.recipeRemainder(recipeRemainder); return this
        }

        override fun group(group: ItemGroup): Settings {
            super.group(group); return this
        }

        override fun rarity(rarity: Rarity): Settings {
            super.rarity(rarity); return this
        }

        override fun fireproof(): Settings {
            super.fireproof(); return this
        }
    }

}