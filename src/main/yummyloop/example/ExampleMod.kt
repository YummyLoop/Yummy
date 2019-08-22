package yummyloop.example

import kotlinx.coroutines.runBlocking
import net.fabricmc.api.ModInitializer
import yummyloop.example.config.Config
import yummyloop.example.item.Items
import yummyloop.example.util.Logger

class ExampleMod : ModInitializer {

    private val logger: Logger = Logger("LoggerTest")

    override fun onInitialize() = runBlocking {
        logger.setLevel("ALL")
        logger.info("**************************")
        logger.info("      Hello World !       ")
        logger.info("**************************")
        logger.debug("logger debug")
        logger.error("logger error")
        logger.trace("logger trace")
        logger.warn("logger warn")
        logger.fatal("logger fatal")

        Items()

        val cc = Config("a/b/Hello.json")
        cc.add("Hello")
        cc.add(arrayOf ("sa","sb","sc"))
        cc.add("End")
        if (!cc.load()) {cc.save()}
        println(cc.toJson())
        val test : Array<String> = cc.get(1)
        println( test[0] )

    }
}