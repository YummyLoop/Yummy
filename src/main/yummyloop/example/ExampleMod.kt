package yummyloop.example

import kotlinx.coroutines.runBlocking
import net.fabricmc.api.ModInitializer
import yummyloop.example.block.Blocks
import yummyloop.example.config.Config
import yummyloop.example.item.Items
import yummyloop.example.util.Logger

class ExampleMod : ModInitializer {
    companion object {
        const val id : String = "example"
    }
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

        Blocks.ini()
        Items.ini()

        val cc = Config("a/b/Hello.jsonc")
        cc.add("Hello")
        val i1 = "Hello World !"
        cc.add(yummyloop.example.test.java())
        cc.add(arrayOf ("sa","sb","sc"))
        cc.add("End")
        if (!cc.load()) {cc.save()}
        println(cc.toJson())
        //val test : Array<String> = cc.get(1)
        //println( test[0] )

    }
}