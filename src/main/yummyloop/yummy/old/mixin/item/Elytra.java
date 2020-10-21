package yummyloop.yummy.old.mixin.item;

import net.minecraft.item.DyeableItem;
import net.minecraft.item.ElytraItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ElytraItem.class)
public final class Elytra implements DyeableItem {
}
