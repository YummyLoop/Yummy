package yummyloop.yummy.content.chest

import dev.architectury.registry.block.BlockProperties
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.item.Item
import yummyloop.yummy.ExampleMod.Register
import yummyloop.yummy.content.chest.doubleChest.*
import yummyloop.yummy.content.chest.iron.IronChest
import yummyloop.yummy.content.chest.iron.IronChestEntity
import yummyloop.yummy.integration.geckolib.GeckoUtils

object Chest {
    val chestBlock: Pair<RegistrySupplier<Block>, RegistrySupplier<Item>>

    init {
        //var chestItem = GeckoUtils.Items.register("chest2", ::ChestItem, Ytem.Settings())

        val ironChest =
            GeckoUtils.Items.registerBlockItem(
                "iron_chest",
                { IronChest(BlockProperties.of(Material.METAL).strength(1F).nonOpaque()) },
                ::ChestItem,
                ChestItemModel.IRON
            )
        //IronChestEntity.rType = Register.blockEntityType("iron_chest", ironChest.first) { IronChestEntity() } // Todo : Fix
        GeckoUtils.Blocks.register(IronChestEntity.rType!!, ChestModel.IRON)


        chestBlock =
            GeckoUtils.Items.registerBlockItem(
                "chest",
                { DoubleChestBlock(BlockProperties.of(Material.STONE).strength(1F).nonOpaque()) },
                ::ChestItem,
                ChestItemModel.STONE
            )
        //DoubleChestEntity.rType = Register.blockEntityType("chest", chestBlock.first) { DoubleChestEntity() } // Todo : Fix
        GeckoUtils.Blocks.register(DoubleChestEntity.rType!!, DoubleChestModel.STONE)

        // Screen stuff
        ChestScreenHandler.rType =
            Register.screenHandlerTypeExtended("chest", ::ChestScreenHandler) { ::ChestScreen }
    }
}