package yummyloop.yummy

import com.mojang.blaze3d.systems.RenderSystem
import io.netty.buffer.Unpooled
import me.shedaniel.architectury.event.events.GuiEvent
import me.shedaniel.architectury.event.events.PlayerEvent
import me.shedaniel.architectury.event.events.TooltipEvent
import me.shedaniel.architectury.event.events.client.ClientScreenInputEvent
import me.shedaniel.architectury.networking.NetworkManager
import me.shedaniel.architectury.registry.BlockProperties
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.client.gui.widget.TexturedButtonWidget
import net.minecraft.client.item.TooltipContext
import net.minecraft.client.util.InputUtil
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ArmorMaterials
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.OrderedText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.ActionResult
import net.minecraft.util.Identifier
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
import yummyloop.yummy.client.gui.screen.addWidget
import yummyloop.yummy.integration.geckolib.GeckoUtils
import yummyloop.yummy.items.Ytem
import yummyloop.yummy.items.YtemGroup
import yummyloop.yummy.items.baa.Ba
import yummyloop.yummy.items.baa.BaHandler
import yummyloop.yummy.items.baa.BaScreen
import yummyloop.yummy.nbt.getSortedInventory
import yummyloop.yummy.registry.Register
import java.util.function.Supplier

object ModContent {
    var EXAMPLE_ITEM =
        Register.item("example_item") { Ytem(Ytem.Settings().group(YtemGroup.EXAMPLE_TAB)) }

    var ba = Register.item("ba") { Ba() }

    init {
        Register.Client.texture("gui/9x9_wood")
        Register.Client.texture("gui/9x9")

        BaHandler.rType = Register.screenHandlerTypeExtended("side_screen", ::BaHandler)
        Register.Client.screen(BaHandler.rType!!) { return@screen ::BaScreen }
    }

    /** Dev content */
    internal object Dev {
        init {
            G2
            G3
            //E1

            Register.Client {
                val client = MinecraftClient.getInstance()
                var stack = ItemStack.EMPTY
                val pressedKeyCode = "key.keyboard.left.shift"
                var isKeyPressed = false

                val shulkerList = mutableListOf("shulker_box")

                fun getInv(s: ItemStack): Inventory? {
                    //LOG.info(MinecraftClient.getInstance().player!!.enderChestInventory.getStack(0).item) // needs to be on the server side
                    val tag: CompoundTag? = s.tag
                    if (s != ItemStack.EMPTY && tag != null) {
                        // Custom inventories
                        var inv = tag.getSortedInventory()
                        if (inv != null) return inv

                        // Vanilla inventories
                        val subTag = tag.getCompound("BlockEntityTag")
                        if (!subTag.isEmpty) {
                            inv = subTag.getSortedInventory()
                            if (inv != null) return inv
                        }
                    }
                    return null
                }

                /**
                 * Draws an item
                 *
                 * @see HandledScreen
                 */
                fun renderItem(matrices: MatrixStack, itemStack: ItemStack, x: Int, y: Int) {
                    val itemRenderer = client.itemRenderer
                    val textRenderer = client.textRenderer

                    val itemCountString = if (itemStack.count > 99) "+99" else itemStack.count.toString()
                    val textScale = 0.85F

                    matrices.push()
                    RenderSystem.translatef(0.0f, 0.0f, 32.0f)
                    itemRenderer.zOffset = 200.0f
                    itemRenderer.renderInGuiWithOverrides(itemStack, x, y)
                    RenderSystem.scalef(textScale, textScale, 1F)
                    itemRenderer.renderGuiItemOverlay(textRenderer,
                        itemStack,
                        (x / textScale).toInt(),
                        (y / textScale).toInt(),
                        itemCountString)
                    RenderSystem.scalef(1 / textScale, 1 / textScale, 1F)
                    itemRenderer.zOffset = 0.0f
                    matrices.pop()
                }

                TooltipEvent.ITEM.register { itemStack: ItemStack, mutableList: MutableList<Text>, tooltipContext: TooltipContext ->
                    if (client.world != null) {
                        stack = itemStack

                        if (shulkerList.any { Regex(it).containsMatchIn(stack.item.translationKey) }) {
                            mutableList.removeAll {
                                if (it == mutableList.first()) return@removeAll false
                                //if (it == mutableList.last()) return@removeAll false
                                if (it is TranslatableText
                                    && (it.toString().contains(Regex("minecraft|shulkerBox")))
                                ) return@removeAll true
                                return@removeAll false
                            }
                        }
                    }
                }

                TooltipEvent.RENDER_VANILLA_PRE.register { matrices: MatrixStack, lines: MutableList<out OrderedText>, x: Int, y: Int ->
                    if (client.world != null && isKeyPressed) {
                        val inv = getInv(stack)
                        if (inv != null) {
                            val offsetX = 10
                            val offsetY = 3 + 10 * (lines.size - 1) + if (lines.size > 1) 2 else 0

                            for (i in 0 until inv.size()) {
                                renderItem(matrices,
                                    inv.getStack(i),
                                    x + offsetX + (i % 9) * 18,
                                    y + offsetY + (i / 9) * 18)
                            }
                        }
                    }
                    return@register ActionResult.SUCCESS
                }

                ClientScreenInputEvent.KEY_PRESSED_PRE.register { client: MinecraftClient, screen: Screen, keyCode: Int, scanCode: Int, modifiers: Int ->
                    if (InputUtil.fromTranslationKey(pressedKeyCode).code == keyCode) isKeyPressed = true
                    return@register ActionResult.PASS
                }
                ClientScreenInputEvent.KEY_RELEASED_PRE.register { client: MinecraftClient, screen: Screen, keyCode: Int, scanCode: Int, modifiers: Int ->
                    if (InputUtil.fromTranslationKey(pressedKeyCode).code == keyCode) isKeyPressed = false
                    return@register ActionResult.PASS
                }
                return@Client Unit
            }





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
                Register.Client {
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
                Register.Client.screen(ScreenHandler1.rType!!) { return@screen ::Screen1 }

                NetworkManager.registerReceiver(
                    NetworkManager.Side.C2S,
                    Identifier("yummy", "packet")
                ) { packetByteBuf: PacketByteBuf, packetContext: NetworkManager.PacketContext ->
                    val player: PlayerEntity = packetContext.player
                    if (player is ServerPlayerEntity) {
                        player.openHandledScreen(Factory1())
                    }
                }

                Register.Client {
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
                Register.Client.screen(BoxScreenHandler.type!!) { return@screen ::BoxScreen }
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

