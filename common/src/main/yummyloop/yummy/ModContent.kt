package yummyloop.yummy

import io.netty.buffer.Unpooled
import me.shedaniel.architectury.event.events.GuiEvent
import me.shedaniel.architectury.event.events.PlayerEvent
import me.shedaniel.architectury.networking.NetworkManager
import me.shedaniel.architectury.registry.BlockProperties
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.client.gui.widget.TexturedButtonWidget
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ArmorMaterials
import net.minecraft.item.BlockItem
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Identifier
import yummyloop.common.client.screen.addWidget
import yummyloop.test.block.BoxScreen
import yummyloop.test.block.BoxScreenHandler
import yummyloop.test.block.TestBlockEntity
import yummyloop.test.block.TestBlockWithEntity
import yummyloop.test.event.Eve
import yummyloop.test.geckolib.GeoExampleEntity2
import yummyloop.test.geckolib.JackInTheBoxItem2
import yummyloop.test.geckolib.PotatoArmor2
import yummyloop.test.gui.Factory1
import yummyloop.test.gui.Screen1
import yummyloop.test.gui.ScreenHandler1
import yummyloop.yummy.ExampleMod.Register
import yummyloop.yummy.client.ToolTipPreview
import yummyloop.yummy.integration.geckolib.GeckoUtils
import yummyloop.yummy.items.Ytem
import yummyloop.yummy.items.YtemGroup
import yummyloop.yummy.items.baa.Ba
import yummyloop.yummy.items.baa.BaHandler
import yummyloop.yummy.items.baa.BaScreen
import java.util.function.Supplier

object ModContent {
    var EXAMPLE_ITEM =
        Register.item("example_item") { Ytem(Ytem.Settings().group(YtemGroup.EXAMPLE_TAB)) }

    var ba = Register.item("ba") { Ba() }

    init {
        Register.client.texture("gui/grid")
        Register.client.texture("gui/9x9")

        BaHandler.rType = Register.screenHandlerTypeExtended("side_screen", ::BaHandler) { ::BaScreen }

        ToolTipPreview // Initialize tooltip
    }

    /** Dev content */
    internal object Dev {
        init {
            G2
            G3
            //E1

            Eve.PLAYER_SCREEN_HANDLER_POST.register { playerInventory, onServer, playerEntity ->
                val inv = SimpleInventory(2)

                for (i in 0..1) {
                    //addSlot()
                }
            }
        }

        /** Button */
        object B1 {
            init {
                Register.client {
                    val client = MinecraftClient.getInstance()
                    GuiEvent.INIT_POST.register { screen, widgets, children ->
                        if (screen is InventoryScreen && !client.interactionManager!!.hasCreativeInventory()) {
                            LOG.info("This is message from a GUI init post event")
                            LOG.info(screen.javaClass.toGenericString())
                            val button = TexturedButtonWidget(
                                screen.width / 2 - 40,
                                screen.height / 2 - 22,
                                20,
                                18,
                                0,
                                0,
                                19,
                                Identifier("minecraft", "textures/gui/recipe_button.png")) {
                                LOG.info("A button was pressed!")
                            }
                            screen.addWidget(button)
                        }
                    }
                }
            }
        }

        /** Button with network packets*/
        object B2 {
            init {
                ScreenHandler1.rType = Register.screenHandlerTypeSimple("side_screen", ::ScreenHandler1)
                Register.client.screen(ScreenHandler1.rType!!) { ::Screen1 }

                NetworkManager.registerReceiver(
                    NetworkManager.Side.C2S,
                    Identifier("yummy", "packet")
                ) { packetByteBuf: PacketByteBuf, packetContext: NetworkManager.PacketContext ->
                    val player: PlayerEntity = packetContext.player
                    if (player is ServerPlayerEntity) {
                        player.openHandledScreen(Factory1())
                    }
                }

                Register.client {
                    val client = MinecraftClient.getInstance()
                    GuiEvent.INIT_POST.register { screen, widgets, children ->
                        if (screen is InventoryScreen && !client.interactionManager!!.hasCreativeInventory()) {
                            LOG.info("This is message from a GUI init post event")
                            LOG.info(screen.javaClass.toGenericString())

                            NetworkManager.sendToServer(Identifier("yummy", "packet"), PacketByteBuf(Unpooled.buffer()))
                        }
                    }
                }
            }
        }

        /** Events */
        object E1 {
            // Event
            init {
                PlayerEvent.OPEN_MENU.register { player, menu ->
                    LOG.info("This is message from a Player open menu event")
                }
                Register.client {
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
                Register.client.screen(BoxScreenHandler.type!!) { ::BoxScreen }
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

