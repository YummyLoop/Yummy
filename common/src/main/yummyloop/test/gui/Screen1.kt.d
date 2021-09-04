package yummyloop.test.gui

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.TexturedButtonWidget
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.slot.Slot
import net.minecraft.screen.slot.SlotActionType
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import yummyloop.yummy.LOG

// See CraftingScreen
class Screen1(
    handler: ScreenHandler1?,
    inventory: PlayerInventory?,
    title: Text?,
) : HandledScreen<ScreenHandler1>(handler, inventory, title), RecipeBookProvider {
    companion object {
        private val TEXTURE: Identifier = Identifier("minecraft", "textures/gui/container/dispenser.png")
        private val RECIPE_BUTTON_TEXTURE = Identifier("textures/gui/recipe_button.png")
    }

    private val recipeBook = RecipeBookWidget()
    private var narrow = this.width < 379

    init {
        LOG.info(this.javaClass.toGenericString())
    }

    override fun drawBackground(matrices: MatrixStack?, delta: Float, mouseX: Int, mouseY: Int) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f)
        client!!.textureManager.bindTexture(BACKGROUND_TEXTURE)
        val i = x
        val j = (height - backgroundHeight) / 2
        this.drawTexture(matrices, i, j, 0, 0, backgroundWidth, backgroundHeight)
    }

    override fun init() {
        super.init()
        narrow = width < 379
        recipeBook.initialize(width, height, client, narrow, this.handler)
        x = recipeBook.findLeftEdge(narrow, width, backgroundWidth) - 100
        children.add(recipeBook)
        setInitialFocus(recipeBook)
        addButton(TexturedButtonWidget(x + 5, height / 2, 20, 18, 0, 0, 19, RECIPE_BUTTON_TEXTURE
        ) { buttonWidget: ButtonWidget ->
            recipeBook.reset(narrow)
            recipeBook.toggleOpen()
            x = recipeBook.findLeftEdge(narrow, width, backgroundWidth)
            (buttonWidget as TexturedButtonWidget).setPos(x + 5,
                height / 2 - 49)
        })
        titleX = 29
    }

    override fun tick() {
        super.tick()
        recipeBook.update()
    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        this.renderBackground(matrices)
        if (recipeBook.isOpen && narrow) {
            drawBackground(matrices, delta, mouseX, mouseY)
            recipeBook.render(matrices, mouseX, mouseY, delta)
        } else {
            recipeBook.render(matrices, mouseX, mouseY, delta)
            super.render(matrices, mouseX, mouseY, delta)
            recipeBook.drawGhostSlots(matrices, x, y, true, delta)
        }
        drawMouseoverTooltip(matrices, mouseX, mouseY)
        recipeBook.drawTooltip(matrices, x, y, mouseX, mouseY)
    }

    override fun isPointWithinBounds(
        xPosition: Int,
        yPosition: Int,
        width: Int,
        height: Int,
        pointX: Double,
        pointY: Double,
    ): Boolean {
        return (!narrow || !recipeBook.isOpen) && super.isPointWithinBounds(xPosition,
            yPosition,
            width,
            height,
            pointX,
            pointY)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return if (recipeBook.mouseClicked(mouseX, mouseY, button)) {
            focused = recipeBook
            true
        } else {
            if (this.narrow && recipeBook.isOpen) true else super.mouseClicked(mouseX, mouseY, button)
        }
    }

    override fun isClickOutsideBounds(mouseX: Double, mouseY: Double, left: Int, top: Int, button: Int): Boolean {
        val bl = mouseX < left.toDouble()
                || mouseY < top.toDouble()
                || mouseX >= (left + backgroundWidth)
                || mouseY >= (top + backgroundHeight)
        return recipeBook.isClickOutsideBounds(mouseX, mouseY, x, y, backgroundWidth, backgroundHeight, button) && bl
    }

    override fun onMouseClick(slot: Slot?, invSlot: Int, clickData: Int, actionType: SlotActionType?) {
        super.onMouseClick(slot, invSlot, clickData, actionType)
        recipeBook.slotClicked(slot)
    }

    override fun refreshRecipeBook() = recipeBook.refresh()

    override fun removed() {
        recipeBook.close()
        super.removed()
    }

    override fun getRecipeBookWidget(): RecipeBookWidget = recipeBook
}

