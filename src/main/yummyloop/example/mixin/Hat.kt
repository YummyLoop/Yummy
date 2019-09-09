@file:JvmName("Hat")
@file:Mixin(MobEntity::class)

package yummyloop.example.mixin

import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.mob.MobEntity
import net.minecraft.item.ItemStack
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import yummyloop.example.item.armor.Hat

@Inject(at = [At("HEAD")], method = ["getPreferredEquipmentSlot"], cancellable = true)
private fun onGetPreferredEquipmentSlot(stack: ItemStack, cir: CallbackInfoReturnable<EquipmentSlot>) {
    if (stack.item is Hat) {
        cir.returnValue = EquipmentSlot.HEAD
    }
    return
}
