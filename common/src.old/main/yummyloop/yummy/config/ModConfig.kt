package yummyloop.yummy.config

import me.sargunvohra.mcmods.autoconfig1u.ConfigData
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config
import yummyloop.yummy.Yummy

// For reference see :
//https://shedaniel.gitbook.io/cloth-config/auto-config/introduction-to-auto-config-1u
@Config(name = Yummy.id)
class ModConfig : ConfigData{
    var a : Boolean = true
    var b : Boolean = false
}