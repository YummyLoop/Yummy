package yummyloop.yummy.client

import com.mojang.blaze3d.systems.RenderSystem
import me.shedaniel.architectury.event.events.TooltipEvent
import me.shedaniel.architectury.event.events.client.ClientScreenInputEvent
import me.shedaniel.architectury.networking.NetworkManager
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
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.ActionResult
import net.minecraft.util.Identifier
import net.minecraft.util.collection.DefaultedList
import yummyloop.yummy.LOG
import yummyloop.yummy.nbt.getSortedInventory
import yummyloop.yummy.network.packets.PacketBuffer
import yummyloop.yummy.registry.Register
import kotlin.math.ceil

object ToolTipPreview {
    init {
        serverPacket()
        Register.Client { Client }
    }

    private fun serverPacket() {
        NetworkManager.registerReceiver(
            NetworkManager.Side.C2S,
            Identifier("yummy", "packet_tooltip_c2s")
        ) { packetByteBuf: PacketByteBuf, packetContext: NetworkManager.PacketContext ->
            // Server Side
            val player: PlayerEntity = packetContext.player
            if (player is ServerPlayerEntity) {
                val enderInventory = player.enderChestInventory
                val list = DefaultedList.ofSize(enderInventory.size(), ItemStack.EMPTY)
                list.forEachIndexed { index, _ -> list[index] = enderInventory.getStack(index) }

                val tag: CompoundTag = CompoundTag()
                Inventories.toTag(tag, list)

                NetworkManager.sendToPlayer(player, Identifier("yummy", "packet_tooltip_s2c"), PacketBuffer(tag))
            }
        }
    }


    @Environment(EnvType.CLIENT)
    private object Client {
        private var client: MinecraftClient = MinecraftClient.getInstance()
        private var hoveredStack: ItemStack = ItemStack.EMPTY
        private var pressedKeyCode: String = "key.keyboard.left.shift"
        private var isKeyPressed: Boolean = false
        private var invSize: Int = 1
        private var itemToolTipFilter: Regex = Regex("shulker_box")
        private var toolTipFilter: Regex = Regex("minecraft|shulkerBox")
        private var backgroundTexture: Texture = Texture("yummy", "textures/gui/grid.png", 300, 300)
        private var tooltipOffsetX = 0
        private var tooltipOffsetY = 0
        private var enderInventoryTag = CompoundTag()

        init {
            captureTooltip()
            captureRender()
            captureKey()
            clientPacket()
        }

        fun clientPacket() {
            NetworkManager.registerReceiver(
                NetworkManager.Side.S2C,
                Identifier("yummy", "packet_tooltip_s2c")
            ) { packetByteBuf: PacketByteBuf, packetContext: NetworkManager.PacketContext ->
                // Client Side
                try {
                    enderInventoryTag = packetByteBuf.readCompoundTag()!!
                } catch (e: Exception) {
                    LOG.warn("Received malformed packet: packet_tooltip_s2c !")
                }
            }
        }

        fun captureTooltip() = TooltipEvent.ITEM.register(this@Client::onAppendTooltip)
        fun captureRender() {
            TooltipEvent.RENDER_VANILLA_PRE.register { matrices, lines, x, y ->
                this@Client.onRenderTooltip(matrices, lines.size, x, y)
            }
            TooltipEvent.RENDER_FORGE_PRE.register { matrices, lines, x, y ->
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

        fun onRenderTooltip(matrices: MatrixStack, lines: Int, x: Int, y: Int): ActionResult {
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
            return ActionResult.SUCCESS
        }

        @Suppress("UNUSED_PARAMETER")
        fun onKeyPressed(
            client: MinecraftClient,
            screen: Screen,
            keyCode: Int,
            scanCode: Int,
            modifiers: Int,
        ): ActionResult {
            if (InputUtil.fromTranslationKey(pressedKeyCode).code == keyCode) isKeyPressed = true
            return ActionResult.PASS
        }

        @Suppress("UNUSED_PARAMETER")
        fun onKeyReleased(
            client: MinecraftClient,
            screen: Screen,
            keyCode: Int,
            scanCode: Int,
            modifiers: Int,
        ): ActionResult {
            if (InputUtil.fromTranslationKey(pressedKeyCode).code == keyCode) isKeyPressed = false
            return ActionResult.PASS
        }

        fun getInventoryFromStack(itemStack: ItemStack): Inventory? {
            if (itemStack != ItemStack.EMPTY) {
                var tag: CompoundTag? = itemStack.tag
                if (itemStack.item == Items.ENDER_CHEST) {
                    NetworkManager.sendToServer(Identifier("yummy", "packet_tooltip_c2s"), PacketBuffer())
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
            val itemRenderer = client.itemRenderer
            val textRenderer = client.textRenderer

            val itemCountString: String = itemStack.count.let {
                if (it > 999) return@let "${itemStack.count / 1000}k"
                if (it > 9999) return@let "+9k"
                return@let "${itemStack.count}"
            }

            matrices.push()
            itemRenderer.renderInGuiWithOverrides(itemStack, x, y)
            RenderSystem.pushMatrix()
            var scale = 1F
            if (itemStack.count in 100..999) {
                scale = 0.8F
                RenderSystem.translatef(1 / scale, 1 / scale, 1F)
                RenderSystem.scalef(scale, scale, 1F)
            }
            itemRenderer.renderGuiItemOverlay(textRenderer, itemStack,
                ceil(x / scale).toInt(),
                ceil(y / scale).toInt(),
                itemCountString)
            RenderSystem.popMatrix()
            matrices.pop()
        }

        fun renderItems(matrices: MatrixStack, inv: Inventory, x: Int, y: Int) {
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
            client.textureManager.bindTexture(backgroundTexture.get())
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

            matrices.pop()
        }
    }
}