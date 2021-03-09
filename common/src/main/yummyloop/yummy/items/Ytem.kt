package yummyloop.yummy.items

import net.minecraft.client.item.TooltipContext
import net.minecraft.item.FoodComponent
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Rarity
import net.minecraft.world.World
import net.minecraft.item.Item as VanillaItem

open class Ytem(settings: VanillaItem.Settings = Settings()) : VanillaItem(settings) {

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
        init {
            super.group(YtemGroup.Dev.devGroup)
        }

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