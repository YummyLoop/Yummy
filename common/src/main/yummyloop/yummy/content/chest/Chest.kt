package yummyloop.yummy.content.chest

import me.shedaniel.architectury.registry.BlockProperties
import me.shedaniel.architectury.registry.RegistrySupplier
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.item.Item
import yummyloop.yummy.ExampleMod.Register
import yummyloop.yummy.integration.geckolib.GeckoUtils

object Chest {
    val chestBlock: Pair<RegistrySupplier<Block>, RegistrySupplier<Item>>

    init {
        //var chestItem = GeckoUtils.Items.register("chest2", ::ChestItem, Ytem.Settings())

        // todo: fix item, appearing the double and single chest at the same time

        val ironChest = GeckoUtils.Items.registerBlockItem("iron_chest", {
            IronChest(BlockProperties.of(Material.METAL).strength(1F).nonOpaque())
        })
        IronChestEntity.rType = Register.blockEntityType("iron_chest", ironChest.first) { IronChestEntity() }
        GeckoUtils.Blocks.register(IronChestEntity.rType!!)



        chestBlock = GeckoUtils.Items.registerBlockItem("chest", {
            ChestBlock(BlockProperties.of(Material.STONE).strength(1F).nonOpaque())
        })
        ChestEntity.rType = Register.blockEntityType("chest", chestBlock.first) { ChestEntity() }
        GeckoUtils.Blocks.register(ChestEntity.rType!!)

        // Screen stuff
        ChestScreenHandler.rType =
            Register.screenHandlerTypeExtended("chest", ::ChestScreenHandler) { ::ChestScreen }
    }
}