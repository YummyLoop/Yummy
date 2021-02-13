package net.examplemod.forge

import net.minecraftforge.fml.loading.FMLPaths
import java.io.File

internal object ExampleExpectPlatformImpl {
    /**
     * This is our actual method to [ExampleExpectPlatform.getConfigDirectory].
     */
    @JvmStatic
    fun getConfigDirectory(): File = FMLPaths.CONFIGDIR.get().toFile()
}