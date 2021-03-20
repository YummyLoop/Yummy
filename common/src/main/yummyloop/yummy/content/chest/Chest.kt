package yummyloop.yummy.content.chest

import me.shedaniel.architectury.registry.BlockProperties
import net.minecraft.block.Material
import yummyloop.yummy.ExampleMod.Register
import yummyloop.yummy.integration.geckolib.GeckoUtils

object Chest {
    init {
        //var chestItem = GeckoUtils.Items.register("chest2", ::ChestItem, Ytem.Settings())

        val chestBlock = GeckoUtils.Items.registerBlockItem("chest",
            { ChestBlock(BlockProperties.of(Material.METAL).strength(1F).nonOpaque()) })

        ChestEntity.rType = Register.blockEntityType("chest", chestBlock.first) { ChestEntity() }

        GeckoUtils.Blocks.register(ChestEntity.rType!!)


        // Screen stuff
        ChestScreenHandler.rType =
            Register.screenHandlerTypeExtended("chest", ::ChestScreenHandler) { ::ChestScreen }
    }
}