package yummyloop.example.config

import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.ModContainer
import yummyloop.example.ExampleMod
import java.io.File

object Config {
    //-----------------------------------------------------------------------------------------------------------------
    // Settings
    //-----------------------------------------------------------------------------------------------------------------
    const val modId = ExampleMod.id
    val logger = ExampleMod.logger

    object External {

    }

    //-----------------------------------------------------------------------------------------------------------------
    // Mod Compatibility
    //-----------------------------------------------------------------------------------------------------------------
    object Compatibility {
        val trinkets= isModLoaded("trinkets")
    }


    //-----------------------------------------------------------------------------------------------------------------
    // Others
    //-----------------------------------------------------------------------------------------------------------------
    private val loader = FabricLoader.getInstance()
    //-----------------------------------------------------------------------------------------------------------------
    private fun configDir(): File? {
        return loader.configDirectory
    }

    fun isClient() : Boolean{
        return loader.environmentType == EnvType.CLIENT
    }
    fun isServer() : Boolean{
        return loader.environmentType == EnvType.SERVER
    }
    fun isDev() : Boolean{
        return loader.isDevelopmentEnvironment
    }

    private fun modList(): MutableCollection<ModContainer>? {
        return loader.allMods
    }
    private fun isModLoaded(mod : String) : Boolean {
        return loader.isModLoaded(mod)
    }
}