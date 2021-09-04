package yummyloop.yummy

import dev.architectury.event.events.common.PlayerEvent
import dev.architectury.networking.NetworkManager
import dev.architectury.registry.block.BlockProperties
import dev.architectury.event.events.client.ClientGuiEvent
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.client.gui.widget.TexturedButtonWidget
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
//import yummyloop.common.client.screen.addWidget
import yummyloop.common.integration.gecko.GeckoGenericModel
import yummyloop.common.network.packets.PacketBuffer
import yummyloop.test.event.Eve
import yummyloop.test.geckolib.GeoExampleEntity2
import yummyloop.test.geckolib.PotatoArmor2
import yummyloop.test.gui.Factory1
//import yummyloop.test.gui.Screen1
import yummyloop.test.gui.ScreenHandler1
import yummyloop.yummy.ExampleMod.Register
//import yummyloop.yummy.client.ToolTipPreview
import yummyloop.yummy.content.chest.Chest
import yummyloop.yummy.integration.geckolib.GeckoUtils
import yummyloop.yummy.item.Ytem
import yummyloop.yummy.item.YtemGroup
import yummyloop.yummy.item.baa.Ba
import yummyloop.yummy.item.baa.BaHandler
import yummyloop.yummy.item.baa.BaScreen

object ModContent {
    var EXAMPLE_ITEM =
        Register.item("example_item") { Ytem(Ytem.Settings().group(YtemGroup.EXAMPLE_TAB)) }

    var ba = Register.item("ba") { Ba() }

    init {
        Register.client.texture("gui/grid", 512)
        Register.client.texture("gui/9x9")

        BaHandler.rType = Register.screenHandlerTypeExtended("side_screen", ::BaHandler) { ::BaScreen }

        //ToolTipPreview // Initialize tooltip

        Chest
    }

    /** Dev content *//*
    internal object Dev {
        init {
            //G1
            //G3
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
                    ClientGuiEvent.INIT_POST.register { screen, widgets, children ->
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
                    ClientGuiEvent.INIT_POST.register { screen, widgets, children ->
                        if (screen is InventoryScreen && !client.interactionManager!!.hasCreativeInventory()) {
                            LOG.info("This is message from a GUI init post event")
                            LOG.info(screen.javaClass.toGenericString())

                            NetworkManager.sendToServer(Identifier("yummy", "packet"), PacketBuffer())
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
                    ClientGuiEvent.INIT_PRE.register { screen, widgets, children ->
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

        /** Gecko armor */
        object G1 {
            var armor = GeckoUtils.Items.registerArmor(
                "potato_armor",
                "_head" to { PotatoArmor2(ArmorMaterials.DIAMOND, EquipmentSlot.HEAD, Ytem.Settings()) },
                "_chestplate" to { PotatoArmor2(ArmorMaterials.DIAMOND, EquipmentSlot.CHEST, Ytem.Settings()) },
                "_leggings" to { PotatoArmor2(ArmorMaterials.DIAMOND, EquipmentSlot.LEGS, Ytem.Settings()) },
                "_boots" to { PotatoArmor2(ArmorMaterials.DIAMOND, EquipmentSlot.FEET, Ytem.Settings()) },
            )
        }

        /** Gecko entity */
        object G3 {
            init {
                //GeoExampleEntity2.type = Register.entityType("geo_ex", ::GeoExampleEntity2, SpawnGroup.CREATURE)
                //Register.entityAttributeLink(GeoExampleEntity2.type!!) { GeoExampleEntity2.createAttributes() }
                GeoExampleEntity2.type = Register.entityTypeWithAttributes(
                    "geo_ex",
                    ::GeoExampleEntity2,
                    SpawnGroup.CREATURE,
                    GeoExampleEntity2.Companion::createAttributes
                )

                GeckoUtils.Entities.register(GeoExampleEntity2.type!!, GeckoGenericModel(ExampleMod.MOD_ID,
                    "geo/jack.geo.json",
                    "textures/item/jack.png",
                    "animations/jack.animation.json"))

            }
        }
    }
*/
}

