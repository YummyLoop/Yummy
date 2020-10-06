package yummyloop.example

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

//import yummyloop.example.block.Blocks
//import yummyloop.example.modCompatibility.ModCompatibilityManager
//import yummyloop.example.item.Items
//import yummyloop.example.util.registry.ClientManager

object ExampleMod : ModInitializer, ClientModInitializer {
    const val id : String = "example"
    private val logger: Logger = LogManager.getLogger(id.toUpperCase())

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

        /*
        val cc = ConfigFile("a/b/Hello.jsonc")
        cc.add("Hello")
        val i1 = "Hello World !"
        cc.add(yummyloop.example.test.java())
        cc.add(arrayOf ("sa","sb","sc"))
        cc.add("End")
        if (!cc.load()) {cc.save()}
        println(cc.toJson())
        //val test : Array<String> = cc.get(1)
        //println( test[0] )
         */

    }

    override fun onInitializeClient() {
        //ClientManager.ini()
    }
}