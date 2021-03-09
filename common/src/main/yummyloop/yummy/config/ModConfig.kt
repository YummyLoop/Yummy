package yummyloop.yummy.config

import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import yummyloop.yummy.ExampleMod.MOD_ID

// For reference see :
//https://shedaniel.gitbook.io/cloth-config/auto-config/introduction-to-auto-config-1u
@Config(name = MOD_ID)
class ModConfig : ConfigData {
    var dev: Boolean = false
    var mixinSideScreen: Boolean = true
    var a: Boolean = true
    var b: Boolean = false
}