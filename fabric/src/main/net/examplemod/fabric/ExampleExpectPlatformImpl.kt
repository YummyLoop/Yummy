package net.examplemod.fabric

import net.fabricmc.loader.api.FabricLoader
import java.io.File

internal object ExampleExpectPlatformImpl {
    /**
     * This is our actual method to [ExampleExpectPlatform.getConfigDirectory].
     */
    @JvmStatic
    fun getConfigDirectory(): File = FabricLoader.getInstance().configDir.toFile()
}