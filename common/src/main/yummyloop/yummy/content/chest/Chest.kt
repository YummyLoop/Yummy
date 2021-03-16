package yummyloop.yummy.content.chest

import me.shedaniel.architectury.registry.BlockProperties
import net.minecraft.block.Material
import yummyloop.yummy.ExampleMod
import yummyloop.yummy.integration.geckolib.GeckoUtils
import yummyloop.yummy.items.Ytem

object Chest {
    init {
        var chestItem = GeckoUtils.Items.register("chest2", ::ChestItem, Ytem.Settings())

        val chestBlock = ExampleMod.Register.blockItem(
            "chest", { ChestBlock(BlockProperties.of(Material.METAL).strength(1F).nonOpaque()) })


        ChestEntity.type = ExampleMod.Register.blockEntityType("chest", chestBlock.first) { ChestEntity() }

        GeckoUtils.Blocks.register(ChestEntity.type!!)


        // Screen stuff
        ChestScreenHandler.type =
            ExampleMod.Register.screenHandlerTypeSimple("chest", ::ChestScreenHandler)
        ExampleMod.Register.client.screen(ChestScreenHandler.type!!) { ::ChestScreen }
    }
}