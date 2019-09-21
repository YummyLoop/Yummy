package yummyloop.example

import kotlinx.coroutines.runBlocking
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityCategory
import net.minecraft.entity.EntityType
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import yummyloop.example.block.Blocks
import yummyloop.example.modCompatibility.ModCompatibilityManager
import yummyloop.example.item.Items
import yummyloop.example.item.spear.Spear
import yummyloop.example.util.Logger
import yummyloop.example.util.registry.ClientManager

object ExampleMod : ModInitializer, ClientModInitializer {
    const val id : String = "example"
    val logger: Logger = Logger(id.toUpperCase(), "ALL", true)

    override fun onInitialize() = runBlocking {
        logger.info("**************************")
        logger.info("      Hello World !       ")
        logger.info("**************************")
        logger.debug("logger debug")
        logger.error("logger error")
        logger.trace("logger trace")
        logger.warn("logger warn")
        logger.fatal("logger fatal")

        ModCompatibilityManager
        Blocks.ini()
        Items.ini()

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
        ClientManager.ini()
    }
}