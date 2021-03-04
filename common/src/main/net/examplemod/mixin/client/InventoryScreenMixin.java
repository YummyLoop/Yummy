package net.examplemod.mixin.client;

import net.examplemod.mixin.common.SlotMixin;
import net.examplemod.screen.slot.YSlot;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> implements RecipeBookProvider {

    private List<YSlot> myYummySlots;

    public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory inventory, Text text) {
        super(screenHandler, inventory, text);
    }

    @Inject(at = @At("TAIL"), method = "init")
    public void init(CallbackInfo info) {

        myYummySlots = new ArrayList<>();
        for (Slot slot : this.getScreenHandler().slots) {
            if (slot instanceof YSlot) {
                YSlot mySlot = (YSlot) slot;
                myYummySlots.add(mySlot);
                if (!mySlot.getVisible() || getRecipeBookWidget().isOpen()) {
                    ((SlotMixin) slot).setXPosition(Integer.MIN_VALUE);
                } else {
                    ((SlotMixin) (Object) mySlot).setXPosition(mySlot.getDefaultX());
                    ((SlotMixin) (Object) mySlot).setYPosition(mySlot.getDefaultY());
                }
            }
        }
    }

    @Inject(at = @At(value = "TAIL"), method = "tick")
    protected void tick(CallbackInfo info) {
        for (YSlot mySlot : myYummySlots) {
            if (!mySlot.getVisible() || getRecipeBookWidget().isOpen()) {
                ((SlotMixin) (Object) mySlot).setXPosition(Integer.MIN_VALUE);
            } else {
                ((SlotMixin) (Object) mySlot).setXPosition(mySlot.getDefaultX());
            }
        }
    }


    @Inject(at = @At("TAIL"), method = "drawBackground")
    protected void drawBackground(MatrixStack matrices, float f, int x, int y, CallbackInfo info) {
        if (!getRecipeBookWidget().isOpen()) {
            final Identifier texture = new Identifier("minecraft", "textures/gui/recipe_book.png");
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            minecraftClient.getTextureManager().bindTexture(texture);

            this.drawTexture(matrices,
                    myYummySlots.get(myYummySlots.size() - 1).getDefaultX() + this.x,
                    myYummySlots.get(0).getDefaultY() + this.y,
                    30,
                    207,
                    22,
                    22);
            //this.drawTexture(matrices, this.x + this.width / 2, this.y, 200 - this.width / 2, 46, 256, 256);
        }
    }

    @Inject(at = @At(value = "TAIL"), method = "drawForeground")
    protected void drawForeground(MatrixStack matrices, int x, int y, CallbackInfo info) {
        // render ?
        //super.drawForeground(matrices, x, y);
        //RenderSystem.disableLighting();
    }

    @Inject(at = @At("HEAD"), method = "isClickOutsideBounds", cancellable = true)
    protected void isClickOutsideBounds(double x, double y, int i, int j, int k, CallbackInfoReturnable<Boolean> info) {
        if (inBounds(x - this.x, y - this.y)) info.setReturnValue(false);
    }

    public boolean inBounds(double x, double y) {
        if (getRecipeBookWidget().isOpen()) return false;
        int x1 = myYummySlots.get(0).getDefaultX();
        int y1 = myYummySlots.get(0).getDefaultY();
        int x2 = myYummySlots.get(myYummySlots.size() - 1).getDefaultX();
        int y2 = myYummySlots.get(myYummySlots.size() - 1).getDefaultY();
        return x > x2 && y > y1 && x < x1 + 18 && y < y2 + 18;
    }
}