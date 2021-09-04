package yummyloop.yummy.client

import dev.architectury.event.EventResult
import dev.architectury.networking.NetworkManager
import dev.architectury.event.events.client.ClientTooltipEvent
import dev.architectury.event.events.client.ClientScreenInputEvent
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.item.TooltipContext
import net.minecraft.client.util.InputUtil
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.ActionResult
import net.minecraft.util.Identifier
import net.minecraft.util.collection.DefaultedList
import yummyloop.common.client.*
import yummyloop.common.nbt.getSortedInventory
import yummyloop.common.network.packets.PacketBuffer
import yummyloop.common.network.packets.sendToPlayer
import yummyloop.common.network.packets.sendToServer
import yummyloop.yummy.ExampleMod.Register
import yummyloop.yummy.LOG
import java.awt.Color
import kotlin.math.ceil

object ToolTipPreview {
    init {
        serverPacket()
        Register.client { Client }
    }

    private fun serverPacket() {
        Register.onReceivePacketFromClient("packet_tooltip_c2s")
        { packetByteBuf: PacketByteBuf, packetContext: NetworkManager.PacketContext ->
            // Server Side
            val player: PlayerEntity = packetContext.player
            if (player is ServerPlayerEntity) {
                val enderInventory = player.enderChestInventory
                val list = DefaultedList.ofSize(enderInventory.size(), ItemStack.EMPTY)
                list.forEachIndexed { index, _ -> list[index] = enderInventory.getStack(index) }

                val tag = NbtCompound()
                Inventories.writeNbt(tag, list)

                PacketBuffer(tag).sendToPlayer(player, Identifier("yummy", "packet_tooltip_s2c"))
            }
        }
    }


    @Environment(EnvType.CLIENT)
    private object Client {
        private val client by lazy { MinecraftClient.getInstance() }
        private var hoveredStack: ItemStack = ItemStack.EMPTY
        private var pressedKeyCode: String = "key.keyboard.left.shift"
        private var isKeyPressed: Boolean = false
        private var invSize: Int = 1
        private var itemToolTipFilter: Regex = Regex("shulker_box")
        private var toolTipFilter: Regex = Regex("minecraft|shulkerBox")
        private var backgroundTexture: Texture = Texture("yummy", "textures/gui/grid.png", 512)
        private var tooltipOffsetX = 0
        private var tooltipOffsetY = 0
        private var enderInventoryTag = NbtCompound()

        init {
            captureTooltip()
            captureRender()
            captureKey()
            clientPacket()
        }

        fun clientPacket() {
            Register.client.onReceivePacketFromServer("packet_tooltip_s2c")
            { packetByteBuf: PacketByteBuf, packetContext: NetworkManager.PacketContext ->
                // Client Side
                try {
                    enderInventoryTag = packetByteBuf.readNbt()!!
                } catch (e: Exception) {
                    LOG.warn("Received malformed packet: packet_tooltip_s2c !")
                }
            }
        }

        fun captureTooltip() = ClientTooltipEvent.ITEM.register(this@Client::onAppendTooltip)
        fun captureRender() {
            ClientTooltipEvent.RENDER_VANILLA_PRE.register { matrices, lines, x, y ->
                this@Client.onRenderTooltip(matrices, lines.size, x, y)
            }
            ClientTooltipEvent.RENDER_FORGE_PRE.register { matrices, lines, x, y ->
                this@Client.onRenderTooltip(matrices, lines.size, x, y)
            }
        }

        fun captureKey() {
            ClientScreenInputEvent.KEY_PRESSED_PRE.register(this@Client::onKeyPressed)
            ClientScreenInputEvent.KEY_RELEASED_PRE.register(this@Client::onKeyReleased)
        }

        @Suppress("UNUSED_PARAMETER")
        fun onAppendTooltip(itemStack: ItemStack, lines: MutableList<Text>, tooltipContext: TooltipContext) {
            if (client.world != null) {
                hoveredStack = itemStack

                if (itemToolTipFilter.containsMatchIn(hoveredStack.item.translationKey)) {
                    lines.removeAll {
                        if (it == lines.first()) return@removeAll false

                        if (it is TranslatableText
                            && (it.toString().contains(toolTipFilter))
                        ) return@removeAll true

                        return@removeAll false
                    }
                }
            }
        }

        fun onRenderTooltip(matrices: MatrixStack, lines: Int, x: Int, y: Int): EventResult {
            if (client.world != null && isKeyPressed) {
                val inv = getInventoryFromStack(hoveredStack)
                if (inv != null) {
                    val offsetX = 14 + tooltipOffsetX
                    val offsetY = 3 + 10 * (lines - 1) + (if (lines > 1) 2 else 0) + 3 + tooltipOffsetY

                    val maxSize = 15
                    invSize = if (inv.size() <= maxSize) inv.size() else maxSize

                    renderBackground(matrices, x + offsetX, y + offsetY)
                    renderItems(matrices, inv, x + offsetX, y + offsetY)
                }
            }
            hoveredStack = ItemStack.EMPTY
            return EventResult.pass()
        }

        @Suppress("UNUSED_PARAMETER")
        fun onKeyPressed(
            client: MinecraftClient,
            screen: Screen,
            keyCode: Int,
            scanCode: Int,
            modifiers: Int,
        ): EventResult {
            if (InputUtil.fromTranslationKey(pressedKeyCode).code == keyCode) isKeyPressed = true
            return EventResult.pass()
        }

        @Suppress("UNUSED_PARAMETER")
        fun onKeyReleased(
            client: MinecraftClient,
            screen: Screen,
            keyCode: Int,
            scanCode: Int,
            modifiers: Int,
        ): EventResult {
            if (InputUtil.fromTranslationKey(pressedKeyCode).code == keyCode) isKeyPressed = false
            return EventResult.pass()
        }

        fun getInventoryFromStack(itemStack: ItemStack): Inventory? {
            if (itemStack != ItemStack.EMPTY) {
                var tag: NbtCompound? = itemStack.nbt
                if (itemStack.item == Items.ENDER_CHEST) {
                    PacketBuffer().sendToServer(Identifier("yummy", "packet_tooltip_c2s"))
                    tag = enderInventoryTag
                }
                if (tag != null) {
                    // Custom inventories
                    var inventory = tag.getSortedInventory()
                    if (inventory != null) return inventory

                    // Vanilla inventories
                    val subTag = tag.getCompound("BlockEntityTag")
                    if (!subTag.isEmpty) {
                        inventory = subTag.getSortedInventory()
                        if (inventory != null) return inventory
                    }
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
            val itemCountString: String = itemStack.count.let {
                if (it > 999) return@let "${itemStack.count / 1000}k"
                if (it > 9999) return@let "+9k"
                return@let "${itemStack.count}"
            }

            Render(matrices) {
                itemZOffset = 400.0f
                itemInGuiWithOverrides(itemStack, x, y)

                var scale = 1F
                if (itemStack.count in 100..999) {
                    scale = 0.8F
                    this.translate(1 / scale, 1 / scale, 1F)
                    this.scale(scale, scale, 1F)
                }
                itemGuiOverlay(itemStack,
                    ceil(x / scale).toInt(),
                    ceil(y / scale).toInt(),
                    itemCountString)

                itemZOffset = 0.0f
            }
        }

        fun renderItems(matrices: MatrixStack, inv: Inventory, x: Int, y: Int) {
            val maxSize = 15

            fun renderI(col: Int) {
                for (i in 0 until invSize) {
                    if (i >= maxSize - 1) break
                    renderItem(matrices,
                        inv.getStack(i),
                        x + (i % col) * 18,
                        y + (i / col) * 18)
                }
            }

            Render(matrices) {
                this.translate(0.0f, 0.0f, 32.0f)
                itemZOffset = 200.0f

                when (invSize) {
                    4 -> renderI(2)
                    5 -> renderI(3)
                    6 -> renderI(3)
                    7 -> renderI(4)
                    8 -> renderI(3)
                    9 -> renderI(3)
                    10 -> {
                        for (i in 0 until 7) {
                            renderItem(it,
                                inv.getStack(i),
                                x + (i % 4) * 18,
                                y + (i / 4) * 18)
                        }
                        for (i in 7 until 10) {
                            renderItem(it,
                                inv.getStack(i),
                                x + ((i - 7) % 3) * 18,
                                y + ((i - 7) / 3) * 18 + 2 * 18)
                        }
                    }
                    11 -> renderI(4)
                    12 -> renderI(4)
                    13 -> {
                        for (i in 0 until 9) {
                            renderItem(it,
                                inv.getStack(i),
                                x + (i % 5) * 18,
                                y + (i / 5) * 18)
                        }
                        for (i in 9 until 13) {
                            renderItem(it,
                                inv.getStack(i),
                                x + ((i - 9) % 4) * 18,
                                y + ((i - 9) / 4) * 18 + 2 * 18)
                        }
                    }
                    else -> renderI(5)
                }
                itemZOffset = 0.0f
            }
        }

        fun renderBackground(matrices: MatrixStack, x: Int, y: Int) {
            Render(matrices) {
                this.bindTexture(backgroundTexture)
                this.translate(0F, 0F, 400F)
                this.color(getColor())

                val offSetX = -8
                val offSetY = -8

                fun draw(xOffset: Int, yOffset: Int, u: Float, v: Float, width: Int, height: Int) =
                    DrawableHelper.drawTexture(it,
                        x + offSetX + xOffset,
                        y + offSetY + yOffset,
                        u,
                        v,
                        width,
                        height,
                        backgroundTexture.xSize,
                        backgroundTexture.ySize)

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
            }
        }

        fun getColor(): Color {
            //LOG.info(hoveredStack.item.translationKey)
            return when (hoveredStack.item.translationKey) {
                "block.minecraft.shulker_box" -> Color(150, 100, 150)
                "block.minecraft.white_shulker_box" -> Color(225, 230, 230)
                "block.minecraft.orange_shulker_box" -> Color(240, 110, 10)
                "block.minecraft.magenta_shulker_box" -> Color(180, 60, 170)
                "block.minecraft.light_blue_shulker_box" -> Color(55, 175, 215)
                "block.minecraft.yellow_shulker_box" -> Color(255, 200, 40)
                "block.minecraft.lime_shulker_box" -> Color(110, 185, 25)
                "block.minecraft.pink_shulker_box" -> Color(240, 130, 165)
                "block.minecraft.gray_shulker_box" -> Color(60, 65, 70)
                "block.minecraft.light_gray_shulker_box" -> Color(140, 140, 130)
                "block.minecraft.cyan_shulker_box" -> Color(20, 130, 140)
                "block.minecraft.purple_shulker_box" -> Color(115, 40, 170)
                "block.minecraft.blue_shulker_box" -> Color(50, 50, 150)
                "block.minecraft.brown_shulker_box" -> Color(110, 70, 40)
                "block.minecraft.green_shulker_box" -> Color(85, 105, 30)
                "block.minecraft.red_shulker_box" -> Color(155, 35, 35)
                "block.minecraft.black_shulker_box" -> Color(40, 40, 40)
                "block.minecraft.ender_chest" -> Color(50, 70, 70)
                else -> Color(255, 255, 255, 255)
            }
        }
    }
}