package yummyloop.example.config

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer

internal object ModConfigSetup {
    private var modConfig: ModConfig
    init {
        AutoConfig.register(
            ModConfig::class.java
        ) { definition: Config?, configClass: Class<ModConfig?>? ->
            JanksonConfigSerializer(
                definition,
                configClass
            )
        }

        modConfig = AutoConfig.getConfigHolder(ModConfig::class.java).config
    }

    fun get(): ModConfig {
        return modConfig
    }

    operator fun invoke(): ModConfig {
        return modConfig
    }
}