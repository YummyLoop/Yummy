package net.examplemod.items

import me.shedaniel.architectury.registry.DeferredRegister
import me.shedaniel.architectury.registry.RegistrySupplier
import net.examplemod.ExampleMod
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.FoodComponent
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Rarity
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import java.util.function.Supplier
import net.minecraft.item.Item as VanillaItem

class Ytem(settings: VanillaItem.Settings = Settings().group(ItemGroup.MISC)) : VanillaItem(settings) {
    companion object {
        private val deferredRegister: DeferredRegister<VanillaItem> =
            DeferredRegister.create(ExampleMod.MOD_ID, Registry.ITEM_KEY)

        internal fun register(
            itemId: String,
            itemSupplier: Supplier<out VanillaItem> = Supplier { Ytem() },
        ): RegistrySupplier<VanillaItem> = deferredRegister.register(itemId, itemSupplier)

        internal fun register() = deferredRegister.register()

        // We can use this if we don't want to use DeferredRegister
//        val REGISTRIES by lazyOf(Registries.get(ExampleMod.MOD_ID))
//        var lazyItems = REGISTRIES.get(Registry.ITEM_KEY)
//        var lazyItem =
//            lazyItems.registerSupplied(Identifier(ExampleMod.MOD_ID, "example_lazy_item"), ::Ytem)
    }

    private val tooltip by lazy { ArrayList<Text>() }
    fun addTooltip(tooltip: String) = this.tooltip.add(TranslatableText(tooltip))
    override fun appendTooltip(
        itemStack: ItemStack,
        world: World?,
        tooltip: MutableList<Text>,
        tooltipContext: TooltipContext,
    ) {
        tooltip.addAll(this.tooltip)
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
/* Reference
    // New Custom ItemStack
    ItemStack b = new ItemStack(a);
    b.getOrCreateTag().putInt("CustomModelData", 1);
    b.setCustomName(new LiteralText("Hello"));
*/