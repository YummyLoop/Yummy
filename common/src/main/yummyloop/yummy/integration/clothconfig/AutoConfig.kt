package yummyloop.yummy.integration.clothconfig

import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer
import yummyloop.yummy.config.ModConfig

internal object AutoConfig {
    private var modConfig = AutoConfig.register(ModConfig::class.java, ::JanksonConfigSerializer).config
    operator fun invoke(): ModConfig = modConfig
}