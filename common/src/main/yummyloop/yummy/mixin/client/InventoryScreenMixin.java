package yummyloop.yummy.mixin.client;

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
import yummyloop.yummy.mixin.common.SlotMixin;
import yummyloop.common.screen.slot.YSlot;

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
            }
        }
    }

    @Inject(at = @At(value = "TAIL"), method = "tick")
    protected void tick(CallbackInfo info) {
        for (YSlot mySlot : myYummySlots) {
            if (!mySlot.getVisible() || getRecipeBookWidget().isOpen()) {
                ((SlotMixin) (Object) mySlot).setXPosition(Integer.MIN_VALUE);
            } else {
                boolean hasStatusEffects = this.client.player.getStatusEffects().size() != 0;
                if (hasStatusEffects) {
                    ((SlotMixin) (Object) mySlot).setXPosition(mySlot.getDefaultX() - 124);
                } else {
                    ((SlotMixin) (Object) mySlot).setXPosition(mySlot.getDefaultX());
                }
                ((SlotMixin) (Object) mySlot).setYPosition(mySlot.getDefaultY());
            }
        }
    }


    @Inject(at = @At("TAIL"), method = "drawBackground")
    protected void drawBackground(MatrixStack matrices, float f, int x, int y, CallbackInfo info) {
        if (!getRecipeBookWidget().isOpen() && myYummySlots.size() > 0) {
            final Identifier texture = new Identifier("yummy", "textures/gui/9x9.png");
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            minecraftClient.getTextureManager().bindTexture(texture);

            int xdim = (myYummySlots.size() / 9 - 1) * 18 + 6;
            int xStart = myYummySlots.get(myYummySlots.size() - 1).x + this.x - 8;

            this.drawTexture(matrices,
                    xStart,
                    myYummySlots.get(0).y + this.y - 8,
                    0,
                    0,
                    xdim,
                    175
            );
            this.drawTexture(matrices,
                    xStart + xdim,
                    myYummySlots.get(0).y + this.y - 8,
                    150,
                    0,
                    25,
                    175
            );
        }
    }

    @Inject(at = @At("HEAD"), method = "isClickOutsideBounds", cancellable = true)
    protected void isClickOutsideBounds(double x, double y, int i, int j, int k, CallbackInfoReturnable<Boolean> info) {
        if (inBounds(x - this.x, y - this.y)) info.setReturnValue(false);
    }

    public boolean inBounds(double x, double y) {
        if (getRecipeBookWidget().isOpen() || myYummySlots.size() == 0) return false;
        int x1 = myYummySlots.get(0).x;
        int y1 = myYummySlots.get(0).y;
        int x2 = myYummySlots.get(myYummySlots.size() - 1).x;
        int y2 = myYummySlots.get(myYummySlots.size() - 1).y;
        return x > x2 && y > y1 && x < x1 + 18 && y < y2 + 18;
    }
}