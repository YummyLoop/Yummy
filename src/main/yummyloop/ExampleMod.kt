package yummyloop

import kotlinx.coroutines.runBlocking
import net.fabricmc.api.ModInitializer
import net.minecraft.block.Material
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import yummyloop.example.block.Block
import yummyloop.example.config.Config
import yummyloop.example.item.BlockItem
import yummyloop.example.item.Item
import yummyloop.example.item.ItemGroup

class ExampleMod : ModInitializer {

    private val logger: Logger = LogManager.getFormatterLogger("KotlinLanguageTest")

    override fun onInitialize() = runBlocking {
        logger.info("**************************")
        logger.info("      Hello World !       ")
        logger.info("**************************")


        val itemGroup : ItemGroup = ItemGroup("tutorial", "hello")
        val itemX : Item = Item("tutorial", "fabric_item", itemGroup)

        itemX.addTooltip("item.tutorial.fabric_item.tooltip")

        val blockA : Block = Block("tutorial", "example_block", Block.Settings.of(Material.METAL).lightLevel(10))
        val blockItemA : BlockItem = BlockItem("tutorial", "example_block", blockA, itemGroup)

        val cc : Config = Config("a/b/Hello.json")
        cc.add("Hello")
        cc.add(arrayOf ("sa","sb","sc"))
        cc.add("End");
        if (!cc.load()) {cc.save()}
        println(cc.toJson())
        val test : Array<String> = cc.get(1)
        println( test[0] )
        
    }
}