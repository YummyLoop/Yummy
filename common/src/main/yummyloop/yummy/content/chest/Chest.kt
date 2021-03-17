package yummyloop.yummy.content.chest

import me.shedaniel.architectury.registry.BlockProperties
import net.minecraft.block.Material
import yummyloop.yummy.ExampleMod
import yummyloop.yummy.integration.geckolib.GeckoUtils

object Chest {
    init {
        //var chestItem = GeckoUtils.Items.register("chest2", ::ChestItem, Ytem.Settings())

        val chestBlock = GeckoUtils.Items.registerBlockItem("chest", { ChestBlock(BlockProperties.of(Material.METAL).strength(1F).nonOpaque()) })

        ChestEntity.type = ExampleMod.Register.blockEntityType("chest", chestBlock.first) { ChestEntity() }

        GeckoUtils.Blocks.register(ChestEntity.type!!)


        // Screen stuff
        ChestScreenHandler.type = ExampleMod.Register.screenHandlerTypeExtended("chest", ::ChestScreenHandler )
        ExampleMod.Register.client.screen(ChestScreenHandler.type!!) { ::ChestScreen }
    }
}