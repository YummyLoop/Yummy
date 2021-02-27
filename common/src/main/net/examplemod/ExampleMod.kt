package net.examplemod

import me.shedaniel.architectury.platform.Platform
import net.examplemod.integration.clothconfig.AutoConfig
import net.examplemod.registry.Register
import net.fabricmc.api.EnvType
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import software.bernie.geckolib3.GeckoLib

object ExampleMod {
    const val MOD_ID = "yummy" // when changing this forge.toml needs to be changed too
    val log: Logger = LogManager.getLogger("Yummy")
    val modConfig = AutoConfig()

    fun init() {
        GeckoLib.initialize()
        if (Platform.isDevelopmentEnvironment() || modConfig.dev) dev()
        Register.register()
    }

    private fun dev() {
        log.info("**************************")
        log.info("     YummY says hello!    ")
        log.info("**************************")
        //log.error("logger error")
        //log.warn("logger warn")
        //log.fatal("logger fatal")

        if (Platform.getEnv() == EnvType.CLIENT) {
            log.info("Its client")
        }

        ModContent.Dev
    }
}
