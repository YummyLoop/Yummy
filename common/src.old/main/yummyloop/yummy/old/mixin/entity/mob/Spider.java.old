package yummyloop.yummy.old.mixin.entity.mob;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yummyloop.yummy.old.item.Items;
import yummyloop.yummy.old.entity.AbstractCobwebProjectileEntity;
import yummyloop.yummy.old.item.thrown.Cobweb;

@Mixin(SpiderEntity.class)
public final class Spider extends HostileEntity implements RangedAttackMob {
    protected Spider(EntityType<? extends HostileEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    @Inject(
            at = @At("RETURN"),
            method = "initGoals()V"
    )
    private void onInitGoals(CallbackInfo info) {
        this.goalSelector.add(4, new ProjectileAttackGoal(this, 1.0D, 40, 9.0F));
    }

    @Override
    public void attack(LivingEntity target, float var2) {
        AbstractCobwebProjectileEntity projectile = new Cobweb.InternalEntity(this.world, this, new ItemStack(Items.INSTANCE.get("cobweb")));
        double x = target.x - this.x;
        double y = target.getBoundingBox().minY + (double)(target.getHeight() / 3.0F) - projectile.y;
        double z = target.z - this.z;
        double absXYZ = MathHelper.sqrt(x * x + z * z);
        projectile.setVelocity(x, y + absXYZ * 0.20000000298023224D, z, 2.4F, (float)(14 - this.world.getDifficulty().getId() * 4));
        this.world.spawnEntity(projectile);
    }
}
