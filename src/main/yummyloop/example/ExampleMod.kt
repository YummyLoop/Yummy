package yummyloop.example

import kotlinx.coroutines.runBlocking
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.entity.EntityCategory
import net.minecraft.entity.EntityType
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import yummyloop.example.block.Blocks
import yummyloop.example.config.Config
import yummyloop.example.item.Items
import yummyloop.example.item.Spear
import yummyloop.example.util.Logger

object ExampleMod : ModInitializer {
    const val id : String = "example"
    val logger: Logger = Logger(id.toUpperCase(), "ALL", true)

    val trinketsExist=FabricLoader.getInstance().isModLoaded("trinkets")

    val spearType: EntityType<out ProjectileEntity> = Registry.register(
    Registry.ENTITY_TYPE,
    Identifier (id, "spear"),
    FabricEntityTypeBuilder.create(EntityCategory.MISC) { entity: EntityType<out ProjectileEntity>, world-> Spear.SpearEntity(entity, world) }.build())


    override fun onInitialize() = runBlocking {
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