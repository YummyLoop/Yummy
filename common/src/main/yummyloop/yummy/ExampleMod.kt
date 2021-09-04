package yummyloop.yummy

import dev.architectury.platform.Platform
import net.fabricmc.api.EnvType
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import software.bernie.geckolib3.GeckoLib
import yummyloop.common.registry.Registers
import yummyloop.yummy.integration.clothconfig.AutoConfig

internal val LOG: Logger = LogManager.getLogger("Yummy")

object ExampleMod {
    const val MOD_ID = "yummy" // when changing this forge.toml needs to be changed too
    val modConfig by lazy { AutoConfig() }
    val Register by lazy { Registers(MOD_ID) }

    fun onInitialize() {
        //GeckoLib.initialize()
        //ModContent
        if (Platform.isDevelopmentEnvironment() || modConfig.dev) dev()
        //Register.register()
    }

    fun onInitializeClient() {
        //Register.client.register()
    }

    private fun dev() {
        LOG.info("**************************")
        LOG.info("     YummY says hello!    ")
        LOG.info("**************************")
        //log.error("logger error")
        //log.warn("logger warn")
        //log.fatal("logger fatal")

        if (Platform.getEnv() == EnvType.CLIENT) {
            LOG.info("Its client")
        }

        //ModContent.Dev
    }
}
