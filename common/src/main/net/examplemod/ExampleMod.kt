package net.examplemod

import me.shedaniel.architectury.platform.Platform
import net.examplemod.config.clothconfig.AutoConfig
import net.examplemod.items.Ytems
import net.fabricmc.api.EnvType
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import software.bernie.geckolib3.GeckoLib

object ExampleMod {
    const val MOD_ID = "yummy" // when changing this forge.toml needs to be changed too
    val log: Logger = LogManager.getLogger("Yummy")
    val modConfig = AutoConfig()

    fun init() {
        log.info("**************************")
        log.info("     YummY says hello!    ")
        log.info("**************************")
        //log.error("logger error")
        //log.warn("logger warn")
        //log.fatal("logger fatal")

        GeckoLib.initialize()
        Ytems
        println(ExampleExpectPlatform.getConfigDirectory().absolutePath)

        if (Platform.getEnv() == EnvType.CLIENT) {
            println("Its client")
        }
    }
}
