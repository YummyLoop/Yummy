package yummyloop.example.mixin.entity.attribute;

import kotlin.jvm.internal.Intrinsics;
import net.minecraft.entity.attribute.AbstractEntityAttribute;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({net.minecraft.entity.attribute.EntityAttributes.class})
public final class EntityAttributes {
    private static final double maxValue;
    @Shadow
    @Final
    @Mutable
    public static final EntityAttribute MAX_HEALTH;
    @Shadow
    @Final
    @Mutable
    public static final EntityAttribute ARMOR;
    @Shadow
    @Final
    @Mutable
    public static final EntityAttribute ARMOR_TOUGHNESS;

    static {
        maxValue =Double.MAX_VALUE;
        AbstractEntityAttribute var10000 = (new ClampedEntityAttribute(null, "generic.maxHealth", 20.0D, 0.0D, maxValue)).setName("Max Health").setTracked(true);
        Intrinsics.checkExpressionValueIsNotNull(var10000, "(ClampedEntityAttribute(…Health\").setTracked(true)");
        MAX_HEALTH = var10000;
        var10000 = (new ClampedEntityAttribute(null, "generic.armor", 0.0D, 0.0D, maxValue)).setTracked(true);
        Intrinsics.checkExpressionValueIsNotNull(var10000, "ClampedEntityAttribute(n…axValue).setTracked(true)");
        ARMOR = var10000;
        var10000 = (new ClampedEntityAttribute(null, "generic.armorToughness", 0.0D, 0.0D, maxValue)).setTracked(true);
        Intrinsics.checkExpressionValueIsNotNull(var10000, "(ClampedEntityAttribute(…xValue)).setTracked(true)");
        ARMOR_TOUGHNESS = var10000;
    }
}
