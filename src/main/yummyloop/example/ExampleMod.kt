package yummyloop.example

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import yummyloop.example.config.ModConfigSetup


object ExampleMod : ModInitializer, ClientModInitializer {
    const val id : String = "yummy-example"
    val log: Logger = LogManager.getLogger(id)
    val modConfig = ModConfigSetup()

    override fun onInitialize() {
        log.info("**************************")
        log.info("      Hello World !       ")
        log.info("**************************")
        log.error("logger error")
        log.warn("logger warn")
        log.fatal("logger fatal")

        //ModCompatibilityManager
        //Blocks.ini()
        //Items.ini()

    }

    override fun onInitializeClient() {
        //ClientManager.ini()
    }
}