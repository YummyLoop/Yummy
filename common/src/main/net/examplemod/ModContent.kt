package net.examplemod

import me.shedaniel.architectury.event.events.GuiEvent
import me.shedaniel.architectury.event.events.PlayerEvent
import me.shedaniel.architectury.registry.BlockProperties
import net.examplemod.block.test.BoxScreen
import net.examplemod.block.test.BoxScreenHandler
import net.examplemod.block.test.TestBlockEntity
import net.examplemod.block.test.TestBlockWithEntity
import net.examplemod.integration.geckolib.GeckoUtils
import net.examplemod.integration.geckolib.test.GeoExampleEntity2
import net.examplemod.integration.geckolib.test.JackInTheBoxItem2
import net.examplemod.integration.geckolib.test.PotatoArmor2
import net.examplemod.items.Ytem
import net.examplemod.items.YtemGroup
import net.examplemod.registry.Register
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.SpawnGroup
import net.minecraft.item.ArmorMaterials
import net.minecraft.item.BlockItem
import net.minecraft.util.ActionResult
import java.util.function.Supplier

object ModContent {
    var EXAMPLE_ITEM =
        Register.item("example_item") { Ytem(Ytem.Settings().group(YtemGroup.EXAMPLE_TAB)) }

    init {

    }

    /** Dev content */
    internal object Dev {
        init {
            G2
            G3
            E1
        }

        /** Events */
        object E1 {
            // Event
            init {
                PlayerEvent.OPEN_MENU.register { player, menu ->
                    LOG.info("This is message from a Player open menu event")
                }
                Register.Client {
                    GuiEvent.INIT_PRE.register { screen, widgets, children ->
                        LOG.info("This is message from a GUI init pre event")
                        return@register ActionResult.SUCCESS
                    }
                }
            }
        }

        /** Blocks and Items */
        object D1 {
            //Block item
            var aa = Register.block("test_block") {
                Block(BlockProperties.of(Material.METAL).strength(1F))
            }
            var aaa = Register.item("test_block_item") { BlockItem(aa.get(), Ytem.Settings()) }

            var ab = Register.blockItem("test_block2")

            //Item
            var test_ITEM = Register.item("test_item")
        }

        /** Gecko Item and armor */
        object G1 {
            var JACK_IN_THE_BOX2 = GeckoUtils.Items.register("jack", ::JackInTheBoxItem2, Ytem.Settings())

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

        /** Gecko Block Entity */
        object G2 {
            val testBlockEntity_block = Register.blockItem(
                "test_block_e", { TestBlockWithEntity(BlockProperties.of(Material.METAL).strength(1F).nonOpaque()) })

            init {
                TestBlockEntity.type = Register.blockEntityType(
                    "test_block_ee",
                    testBlockEntity_block.first
                ) { TestBlockEntity() }

                GeckoUtils.Blocks.register(TestBlockEntity.type!!,
                    "geo/jack.geo.json",
                    "textures/item/jack.png",
                    "animations/jack.animation.json")

                // Screen stuff
                BoxScreenHandler.type =
                    Register.screenHandlerTypeSimple("test_screen_type", ::BoxScreenHandler)
                Register.Client { Register.Client.screen(BoxScreenHandler.type!!.get(), ::BoxScreen) }
            }
        }

        /** Gecko entity */
        object G3 {
            init {
                GeoExampleEntity2.type = Register.entityType("geo_ex") {
                    EntityType.Builder.create(::GeoExampleEntity2, SpawnGroup.CREATURE)
                }

                GeckoUtils.Entities.register(GeoExampleEntity2.type!!,
                    "geo/jack.geo.json",
                    "textures/item/jack.png",
                    "animations/jack.animation.json")
                Register.entityAttributeLink(GeoExampleEntity2.type!!) { GeoExampleEntity2.createAttributes() }
            }

        }
    }
}

