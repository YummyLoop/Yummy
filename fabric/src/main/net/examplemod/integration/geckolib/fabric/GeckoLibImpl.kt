package net.examplemod.integration.geckolib.fabric

import net.minecraft.item.Item
import software.bernie.geckolib3.GeckoLib
import software.bernie.geckolib3.core.IAnimatable
import java.util.function.Supplier
import kotlin.reflect.KFunction1

object GeckoLibImpl {
    @JvmStatic
    fun initialize(): Unit = GeckoLib.initialize()

    @JvmStatic
    fun <I> geckoSupplier(
        itemFunc: KFunction1<Item.Settings, I>,
        itemSettings: Item.Settings,
        modID: String,
        modelLocation: String,
        textureLocation: String,
        animationFileLocation: String
    ): Supplier<out I> where I : Item, I : IAnimatable {
        return Supplier { itemFunc(itemSettings) }
    }
}
