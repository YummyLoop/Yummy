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
import net.minecraft.client.gui.DrawableHelper
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
import kotlin.math.ceil

object ModContent {
    var EXAMPLE_ITEM =
        Register.item("example_item") { Ytem(Ytem.Settings().group(YtemGroup.EXAMPLE_TAB)) }

    var ba = Register.item("ba") { Ba() }

    init {
        Register.Client.texture("gui/grid")
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
                var invSize = 1

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
                    val client = MinecraftClient.getInstance()
                    val itemRenderer = client.itemRenderer
                    val textRenderer = client.textRenderer

                    val itemCountString : String = itemStack.count.let {
                        if (it > 999) return@let "${itemStack.count/1000}k"
                        if (it > 9999) return@let "+9k"
                        return@let "${itemStack.count}"
                    }

                    matrices.push()
                    itemRenderer.renderInGuiWithOverrides(itemStack, x, y)
                    RenderSystem.pushMatrix()
                    var scale = 1F
                    if (itemStack.count in 100..999) {
                        scale = 0.8F
                        RenderSystem.translatef(1 / scale,1 / scale, 1F)
                        RenderSystem.scalef(scale, scale, 1F)
                    }
                    itemRenderer.renderGuiItemOverlay(textRenderer, itemStack,
                        ceil(x/scale).toInt(),
                        ceil(y/scale).toInt(),
                        itemCountString)
                    RenderSystem.popMatrix()
                    matrices.pop()
                }

                fun renderItems(matrices: MatrixStack, inv: Inventory, x: Int, y: Int) {
                    val client = MinecraftClient.getInstance()
                    val itemRenderer = client.itemRenderer
                    val maxSize = 15

                    matrices.push()
                    fun renderI(col: Int) {
                        for (i in 0 until invSize) {
                            if (i >= maxSize - 1) break
                            renderItem(matrices,
                                inv.getStack(i),
                                x + (i % col) * 18,
                                y + (i / col) * 18)
                        }
                    }

                    RenderSystem.translatef(0.0f, 0.0f, 32.0f)
                    itemRenderer.zOffset = 200.0f

                    when (invSize) {
                        4 -> renderI(2)
                        5 -> renderI(3)
                        6 -> renderI(3)
                        7 -> renderI(4)
                        8 -> renderI(3)
                        9 -> renderI(3)
                        10 -> {
                            for (i in 0 until 7) {
                                renderItem(matrices,
                                    inv.getStack(i),
                                    x + (i % 4) * 18,
                                    y + (i / 4) * 18)
                            }
                            for (i in 7 until 10) {
                                renderItem(matrices,
                                    inv.getStack(i),
                                    x + ((i - 7) % 3) * 18,
                                    y + ((i - 7) / 3) * 18 + 2 * 18)
                            }
                        }
                        11 -> renderI(4)
                        12 -> renderI(4)
                        13 -> {
                            for (i in 0 until 9) {
                                renderItem(matrices,
                                    inv.getStack(i),
                                    x + (i % 5) * 18,
                                    y + (i / 5) * 18)
                            }
                            for (i in 9 until 13) {
                                renderItem(matrices,
                                    inv.getStack(i),
                                    x + ((i - 9) % 4) * 18,
                                    y + ((i - 9) / 4) * 18 + 2 * 18)
                            }
                        }
                        else -> renderI(5)
                    }
                    itemRenderer.zOffset = 0.0f
                    matrices.pop()
                }

                fun renderBackground(matrices: MatrixStack, x: Int, y: Int) {
                    matrices.push()
                    client.textureManager.bindTexture(Identifier("yummy", "textures/gui/grid.png"))
                    RenderSystem.translatef(0F, 0F, 400F)

                    val offSetX = -8
                    val offSetY = -8

                    fun draw(xOffset: Int, yOffset: Int, u: Float, v: Float, width: Int, height: Int) =
                        DrawableHelper.drawTexture(matrices,
                            x + offSetX + xOffset,
                            y + offSetY + yOffset,
                            u,
                            v,
                            width,
                            height,
                            300,
                            300)

                    fun draw(u: Float, v: Float, width: Int, height: Int) = draw(0, 0, u, v, width, height)
                    when (invSize) {
                        1 -> draw(0F, 0F, 32, 32)
                        2 -> draw(33F, 0F, 50, 32)
                        3 -> draw(84F, 0F, 68, 32)
                        4 -> draw(0F, 33F, 50, 50)
                        5 -> draw(51F, 33F, 68, 50)
                        6 -> draw(120F, 33F, 68, 50)
                        7 -> draw(0F, 84F, 86, 50)
                        8 -> draw(87F, 84F, 68, 68)
                        9 -> draw(156F, 84F, 68, 68)
                        10 -> draw(0F, 135F, 86, 68)
                        11 -> draw(87F, 153F, 86, 68)
                        12 -> draw(174F, 153F, 86, 68)
                        13 -> {
                            draw(105F, 223F, 79, 68)
                            draw(79, 0, 61F, 135F, 25, 68)
                        }
                        14 -> draw(105F, 223F, 104, 68)
                        else -> draw(0F, 223F, 104, 68)
                    }

                    matrices.pop()
                }

                TooltipEvent.ITEM.register { itemStack: ItemStack, lines: MutableList<Text>, tooltipContext: TooltipContext ->
                    if (client.world != null) {
                        stack = itemStack

                        if (shulkerList.any { Regex(it).containsMatchIn(stack.item.translationKey) }) {
                            lines.removeAll {
                                if (it == lines.first()) {
                                    //LOG.info("skipped: $it")
                                    return@removeAll false
                                }
                                if (it is TranslatableText
                                    && (it.toString().contains(Regex("minecraft|shulkerBox")))
                                ) {
                                    //LOG.info("removed : $it")
                                    return@removeAll true
                                }
                                return@removeAll false
                            }
                        }
                    }
                }

                TooltipEvent.RENDER_VANILLA_PRE.register { matrices: MatrixStack, lines: MutableList<out OrderedText>, x: Int, y: Int ->
                    if (client.world != null && isKeyPressed) {
                        val inv = getInv(stack)
                        if (inv != null) {
                            val offsetX = 14
                            val offsetY = 3 + 10 * (lines.size - 1) + (if (lines.size > 1) 2 else 0) + 3

                            val maxSize = 15
                            invSize = if (inv.size() <= maxSize) inv.size() else maxSize

                            renderBackground(matrices, x + offsetX, y + offsetY)
                            renderItems(matrices, inv, x + offsetX, y + offsetY)
                        }
                    }
                    stack= ItemStack.EMPTY
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

