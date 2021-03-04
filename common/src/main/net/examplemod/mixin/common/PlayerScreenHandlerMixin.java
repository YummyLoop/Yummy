package net.examplemod.mixin.common;

import net.examplemod.screen.slot.YSlot;
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


@Mixin({PlayerScreenHandler.class})
public abstract class PlayerScreenHandlerMixin extends AbstractRecipeScreenHandler<CraftingInventory> {

    @Inject(
            at = {@At("RETURN")},
            method = {"<init>"}
    )
    protected final void init(PlayerInventory inventory, boolean onServer, PlayerEntity owner, CallbackInfo info) {
        Inventory inv = owner.getEnderChestInventory();

        System.out.println("Mixin is handler");
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < inv.size() / 3; j++) {
                this.addSlot(new YSlot(inv, i + j * 3, -18 - i * 18, 2 + j * 18));
            }
        }

    }

    public PlayerScreenHandlerMixin(ScreenHandlerType screenHandlerType, int alwaysZero) {
        super(screenHandlerType, alwaysZero);
    }
}
