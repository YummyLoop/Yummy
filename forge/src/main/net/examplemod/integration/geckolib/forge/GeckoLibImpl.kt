package net.examplemod.integration.geckolib.forge

import net.minecraft.item.Item
import software.bernie.geckolib3.GeckoLib
import software.bernie.geckolib3.core.IAnimatable
import java.util.concurrent.Callable
import java.util.function.Supplier
import kotlin.reflect.KFunction1
import net.examplemod.integration.geckolib.GeckoLib as MyGeckoLib

object GeckoLibImpl {
    @JvmStatic
    fun initialize(): Unit = GeckoLib.initialize()

    @JvmStatic
    fun <I> GeckoSupplier(
        itemFunc: KFunction1<Item.Settings, I>,
        itemSettings: Item.Settings,
        modID: String,
        modelLocation: String,
        textureLocation: String,
        animationFileLocation: String
    ): Supplier<out I> where I : Item, I : IAnimatable {
        var a = GeckoUtils.GenericItemRenderer(GeckoUtils.GenericModel(modID, modelLocation, textureLocation, animationFileLocation))
        var b = itemFunc(itemSettings.setISTER { Callable {a} })
        return Supplier { b }


        /*
        var a = GeckoUtils.GenericItemRenderer(GeckoUtils.GenericModel(modID, modelLocation, textureLocation, animationFileLocation))
        var b = itemFunc(itemSettings.setISTER { Callable {a} })

        return MyGeckoLib.Items.GeckoItem(b)
*/
        //return MyGeckoLib.Items.GeckoItem()
        /*
        JackInTheBoxItem2(Item.Settings().setISTER { Callable { GenericItemRenderer(
                    GenericModel(
                        modID = i[1] as String,
                        modelLocation = i[2] as String,
                        textureLocation = i[3] as String,
                        animationFileLocation = i[4] as String,
                    )
                ) } })
         */
    }
}