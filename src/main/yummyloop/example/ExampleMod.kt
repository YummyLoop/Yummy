package yummyloop.example

import kotlinx.coroutines.runBlocking
import net.fabricmc.api.ModInitializer
import net.minecraft.block.Material
import yummyloop.example.block.Block
import yummyloop.example.config.Config
import yummyloop.example.item.BlockItem
import yummyloop.example.item.Item
import yummyloop.example.item.ItemGroup
import yummyloop.example.item.Items
import yummyloop.example.util.Logger

class ExampleMod : ModInitializer {

    private val logger: Logger = Logger("LoggerTest")

    override fun onInitialize() = runBlocking {
        logger.setLevel("ALL")
        logger.info("**************************")
        logger.info("      Hello World !       ")
        logger.info("**************************")
        logger.debug("logger debug") // shows nothing
        logger.error("logger error")
        logger.trace("logger trace") // shows nothing
        logger.warn("logger warn")
        logger.fatal("logger fatal")
        logger.trace("logger trace") // shows nothing

        Items()
        val itemGroup = ItemGroup("tutorial", "hello")
        val itemX = Item("tutorial", "fabric_item", itemGroup)

        itemX.addTooltip("item.tutorial.fabric_item.tooltip")

        val blockA = Block("tutorial", "example_block", Block.Settings.of(Material.METAL).lightLevel(10))
        val blockItemA = BlockItem("tutorial", "example_block", blockA, itemGroup)

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
/*
    // import org.apache.logging.log4j.core.LoggerContext;
    // import org.apache.logging.log4j.core.config.Configuration;
    // import org.apache.logging.log4j.core.config.LoggerConfig;

    LoggerContext context = (LoggerContext) LogManager.getContext(false);
    Configuration config = context.getConfiguration();
    LoggerConfig rootConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
    rootConfig.setLevel(Level.DEBUG);

    // You could also specify the actual logger name as below
    // and it will return the LoggerConfig used by the Logger.
    LoggerConfig loggerConfig = config.getLoggerConfig("com.apache.test");
    loggerConfig.setLevel(Level.TRACE);

    // This causes all Loggers to refetch information from their LoggerConfig.
    context.updateLoggers();
 */