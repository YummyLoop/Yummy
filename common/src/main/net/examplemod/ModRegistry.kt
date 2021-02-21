package net.examplemod

import me.shedaniel.architectury.registry.BlockProperties
import me.shedaniel.architectury.registry.DeferredRegister
import me.shedaniel.architectury.registry.RegistrySupplier
import net.examplemod.integration.geckolib.GeckoUtils
import net.examplemod.integration.geckolib.JackInTheBoxItem2
import net.examplemod.integration.geckolib.PotatoArmor2
import net.examplemod.items.Ytem
import net.examplemod.items.YtemGroup
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorMaterials
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry
import java.util.function.Supplier

object ModRegistry {
    /** Final registry of the content */
    internal fun register() {
        Content
        Register.blockRegister.register()
        Register.itemRegister.register()
    }

    /** Initializes the dev content */
    internal fun dev() {
        Content.Dev
    }

    /** Contains the declared registers, and the functions to register new content */
    internal object Register {
        /** Block register */
        val blockRegister: DeferredRegister<Block> =
            DeferredRegister.create(ExampleMod.MOD_ID, Registry.BLOCK_KEY)

        /** Item register */
        val itemRegister: DeferredRegister<Item> =
            DeferredRegister.create(ExampleMod.MOD_ID, Registry.ITEM_KEY)

        // We can use this if we don't want to use DeferredRegister
        // val REGISTRIES by lazyOf(Registries.get(ExampleMod.MOD_ID))
        // var lazyItems = REGISTRIES.get(Registry.ITEM_KEY)
        // var lazyItem = lazyItems.registerSupplied(Identifier(ExampleMod.MOD_ID, "example_lazy_item"), ::Ytem)}

        /**
         * Registers a new item
         *
         * @param itemId Id of the item
         * @param itemSupplier The supplier used to create the item
         */
        fun item(
            itemId: String,
            itemSupplier: Supplier<out Item> = Supplier { Ytem() },
        ): RegistrySupplier<Item> = itemRegister.register(itemId, itemSupplier)

        /**
         * Registers a new block
         *
         * @param blockId Id of the block
         * @param blockSupplier The supplier used to create the block
         */
        fun block(
            blockId: String,
            blockSupplier: Supplier<out Block> = Supplier { Block(BlockProperties.of(Material.SOIL)) },
        ): RegistrySupplier<Block> = blockRegister.register(blockId, blockSupplier)

        /**
         * Registers a new block and a corresponding item
         *
         * @param blockItemId Id of the block and item
         * @param blockSupplier The supplier used to create the block
         * @param itemSettings The item settings used to create the item
         */
        fun blockItem(
            blockItemId: String,
            blockSupplier: Supplier<out Block> = Supplier { Block(BlockProperties.of(Material.SOIL)) },
            itemSettings: Item.Settings = Ytem.Settings(),
        ): Pair<RegistrySupplier<Block>, RegistrySupplier<Item>> {
            val block = this.block(blockItemId, blockSupplier)
            val item = this.item(blockItemId) { BlockItem(block.get(), itemSettings) }
            return Pair(block, item)
        }
    }

    object Content {
        var EXAMPLE_ITEM = Register.item("example_item") { Ytem(Ytem.Settings().group(YtemGroup.EXAMPLE_TAB)) }

        internal object Dev {
            var aa = Register.block("test_block") { Block(BlockProperties.of(Material.METAL).strength(1F)) }
            var aaa = Register.item("test_block_item") { BlockItem(aa.get(), Ytem.Settings()) }

            var ab = Register.blockItem("test_block2")

            var test_ITEM = Register.item("test_item")

            // Gecko
            var JACK_IN_THE_BOX2 =
                GeckoUtils.Items.register("jack",
                    ::JackInTheBoxItem2, Item.Settings().group(YtemGroup.Dev.devGroup))

            //var JACK_IN_THE_BOX2  = ITEMS.register("jack") { JackInTheBoxItem2(Item.Settings().group(EXAMPLE_TAB)) }
            var armor = GeckoUtils.Items.registerArmor(
                "potato_armor",
                "_head" to Supplier { PotatoArmor2(ArmorMaterials.DIAMOND, EquipmentSlot.HEAD, Ytem.Settings()) },
                "_chestplate" to Supplier {
                    PotatoArmor2(ArmorMaterials.DIAMOND,
                        EquipmentSlot.CHEST,
                        Ytem.Settings())
                },
                "_leggings" to Supplier { PotatoArmor2(ArmorMaterials.DIAMOND, EquipmentSlot.LEGS, Ytem.Settings()) },
                "_boots" to Supplier { PotatoArmor2(ArmorMaterials.DIAMOND, EquipmentSlot.FEET, Ytem.Settings()) },
            )
        }
    }
}