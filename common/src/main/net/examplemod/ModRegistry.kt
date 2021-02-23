package net.examplemod

import me.shedaniel.architectury.registry.BlockProperties
import me.shedaniel.architectury.registry.DeferredRegister
import me.shedaniel.architectury.registry.RegistrySupplier
import net.examplemod.block.TestBlockEntity
import net.examplemod.block.TestBlockWithEntity
import net.examplemod.integration.geckolib.GeckoUtils
import net.examplemod.integration.geckolib.JackInTheBoxItem2
import net.examplemod.integration.geckolib.PotatoArmor2
import net.examplemod.items.Ytem
import net.examplemod.items.YtemGroup
import net.minecraft.block.Material
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorMaterials
import net.minecraft.item.BlockItem
import net.minecraft.util.registry.Registry
import java.util.function.Supplier
import net.minecraft.block.Block as VanillaBlock
import net.minecraft.item.Item as VanillaItem

object ModRegistry {
    /** Final registry of the content */
    internal fun register() {
        Content
        Register.blockRegister.register()
        Register.blockEntityTypeRegister.register()
        Register.itemRegister.register()
    }

    /** Initializes the dev content */
    internal fun dev() {
        Content.Dev
    }

    /** Contains the declared registers, and the functions to register new content */
    internal object Register {
        /** Block register */
        val blockRegister: DeferredRegister<VanillaBlock> =
            DeferredRegister.create(ExampleMod.MOD_ID, Registry.BLOCK_KEY)

        /** Block Entity Type register */
        val blockEntityTypeRegister: DeferredRegister<BlockEntityType<*>> =
            DeferredRegister.create(ExampleMod.MOD_ID, Registry.BLOCK_ENTITY_TYPE_KEY)

        /** Item register */
        val itemRegister: DeferredRegister<VanillaItem> =
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
         * @return A RegistrySupplier for the item
         */
        fun item(
            itemId: String,
            itemSupplier: Supplier<out VanillaItem> = Supplier { Ytem() },
        ): RegistrySupplier<VanillaItem> = itemRegister.register(itemId, itemSupplier)

        /**
         * Registers a new block
         *
         * @param blockId Id of the block
         * @param blockSupplier The supplier used to create the block
         * @return A RegistrySupplier for the block
         */
        fun block(
            blockId: String,
            blockSupplier: Supplier<out VanillaBlock> = Supplier { VanillaBlock(BlockProperties.of(Material.SOIL)) },
        ): RegistrySupplier<VanillaBlock> = blockRegister.register(blockId, blockSupplier)

        /**
         * Registers a new block and a corresponding item
         *
         * @param blockItemId Id of the block and item
         * @param blockSupplier The supplier used to create the block
         * @param itemSettings The item settings used to create the item
         * @return A Pair of RegistrySuppliers for the block and item
         */
        fun blockItem(
            blockItemId: String,
            blockSupplier: Supplier<out VanillaBlock> = Supplier { VanillaBlock(BlockProperties.of(Material.SOIL)) },
            itemSettings: VanillaItem.Settings = Ytem.Settings(),
        ): Pair<RegistrySupplier<VanillaBlock>, RegistrySupplier<VanillaItem>> {
            val block = this.block(blockItemId, blockSupplier)
            val item = this.item(blockItemId) { BlockItem(block.get(), itemSettings) }
            return Pair(block, item)
        }

        /**
         * Registers/Associates a blockEntityType with multiple blocks
         *
         * @param blockEntityTypeId Id of the block entity type
         * @param blockEntityTypeSupplier The supplier used to create the block entity
         * @param blocks The blocks to be associated with the block entity type
         * @return A RegistrySupplier for the block entity type
         */
        fun <T> blockEntityType(
            blockEntityTypeId: String,
            blockEntityTypeSupplier: Supplier<T>,
            vararg blocks: RegistrySupplier<VanillaBlock>,
        ): RegistrySupplier<BlockEntityType<T>> where T : BlockEntity {
            return blockEntityTypeRegister.register(blockEntityTypeId) {
                BlockEntityType.Builder.create(
                    blockEntityTypeSupplier,
                    *blocks.map { it.get() }.toTypedArray()
                ).build(null)
            }
        }
    }

    object Content {
        var EXAMPLE_ITEM = Register.item("example_item") { Ytem(Ytem.Settings().group(YtemGroup.EXAMPLE_TAB)) }

        internal object Dev {
            //Block item
            var aa = Register.block("test_block") { VanillaBlock(BlockProperties.of(Material.METAL).strength(1F)) }
            var aaa = Register.item("test_block_item") { BlockItem(aa.get(), Ytem.Settings()) }

            var ab = Register.blockItem("test_block2")


            //Item
            var test_ITEM = Register.item("test_item")

            // Gecko
            var JACK_IN_THE_BOX2 = GeckoUtils.Items.register("jack", ::JackInTheBoxItem2, Ytem.Settings())

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

            //Block Entity
            val testBlockEntity_block = Register.blockItem(
                "test_block_e", { TestBlockWithEntity(BlockProperties.of(Material.METAL).strength(1F).nonOpaque()) })

            init {
                //Block Entity
                TestBlockEntity.type =
                    Register.blockEntityType("test_block_ee", { TestBlockEntity() }, testBlockEntity_block.first)
                GeckoUtils.Blocks.register(TestBlockEntity.type!!,
                    "geo/jack.geo.json",
                    "textures/item/jack.png",
                    "animations/jack.animation.json")
            }
        }
    }
}