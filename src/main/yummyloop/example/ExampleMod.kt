package yummyloop.example

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import yummyloop.example.config.ModConfigSetup

//import yummyloop.example.block.Blocks
//import yummyloop.example.modCompatibility.ModCompatibilityManager
//import yummyloop.example.item.Items
//import yummyloop.example.util.registry.ClientManager

object ExampleMod : ModInitializer, ClientModInitializer {
    const val id : String = "yummy-example"
    private val logger: Logger = LogManager.getLogger(id.toUpperCase())
    val modConfig = ModConfigSetup()

    override fun onInitialize() {
        logger.info("**************************")
        logger.info("      Hello World !       ")
        logger.info("**************************")
        logger.error("logger error")
        logger.warn("logger warn")
        logger.fatal("logger fatal")

        //ModCompatibilityManager
        //Blocks.ini()
        //Items.ini()

    }

    override fun onInitializeClient() {
        //ClientManager.ini()
    }
}