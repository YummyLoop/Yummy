package yummyloop.yummy.mixin.common;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yummyloop.yummy.screen.slot.YSlot;


@Mixin({PlayerScreenHandler.class})
public abstract class PlayerScreenHandlerMixin extends AbstractRecipeScreenHandler<CraftingInventory> {

    public PlayerScreenHandlerMixin(ScreenHandlerType screenHandlerType, int alwaysZero) {
        super(screenHandlerType, alwaysZero);
    }

    @Inject(
            at = {@At("RETURN")},
            method = {"<init>"}
    )
    protected final void init(PlayerInventory inventory, boolean onServer, PlayerEntity owner, CallbackInfo info) {
        Inventory inv = owner.getEnderChestInventory();

        int xOffset = -30;
        int yOffset = 2;
        YSlot tempSlot;

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < inv.size() / 3; j++) {
                tempSlot = new YSlot(inv, i + j * 3, xOffset - i * 18, yOffset + j * 18);
                this.addSlot(tempSlot);
                ((SlotMixin) (Object) tempSlot).setXPosition(Integer.MIN_VALUE);
            }
        }
    }
}
