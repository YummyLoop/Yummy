package yummyloop.example.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public final class Hat {
    @Inject(
            at = @At("HEAD"),
            method = "getPreferredEquipmentSlot",
            cancellable = true
    )
    private static void onGetPreferredEquipmentSlot(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> cir) {
        if (stack.getItem() instanceof yummyloop.example.item.armor.Hat) {
            cir.setReturnValue(EquipmentSlot.HEAD);
        }
    }
}
