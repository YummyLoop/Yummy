package yummyloop.yummy.mixin.common;

import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Slot.class)
public interface SlotMixin {

    @Accessor("x")
    int getXPosition();

    @Accessor("x")
    void setXPosition(int x);

    @Accessor("y")
    int getYPosition();

    @Accessor("y")
    void setYPosition(int y);
}