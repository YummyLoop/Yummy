package net.examplemod.config.clothconfig

import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer
import net.examplemod.config.ModConfig

internal object AutoConfig {
    private var modConfig = AutoConfig.register(ModConfig::class.java, ::JanksonConfigSerializer).config
    operator fun invoke(): ModConfig = modConfig
}