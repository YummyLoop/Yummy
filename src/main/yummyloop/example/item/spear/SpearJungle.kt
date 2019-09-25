package yummyloop.example.item.spear

import net.fabricmc.fabric.api.client.render.EntityRendererRegistry
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import yummyloop.example.entity.AbstractSpearEntity
import yummyloop.example.render.entity.ThrownItemEntityRenderer
import yummyloop.example.util.registry.ClientManager
import yummyloop.example.util.registry.RegistryManager

object SpearJungle : AbstractSpear("spear_jungle", SpearSettings.Wooden.itemSettings) {

    override val attackDamage = SpearSettings.Wooden.attackDamage
    override val attackSpeed = SpearSettings.Wooden.attackSpeed
    override val velocityMod = SpearSettings.Wooden.velocityMod

    init {
        InternalEntity
        ClientManager.registerEntityRenderer(InternalEntity::class.java) //look for ways to replace the need for a class or use byteBuddy ?
        { entityRenderDispatcher: EntityRenderDispatcher, context: EntityRendererRegistry.Context -> ThrownItemEntityRenderer(entityRenderDispatcher, context, this) }
    }

    override fun getThrownEntity(player: PlayerEntity, stack: ItemStack): ProjectileEntity {
        return InternalEntity(player.world, player, stack)
    }

    private class InternalEntity : AbstractSpearEntity {
        companion object{
            private val registeredType= RegistryManager.registerMiscEntityType(
                    (itemName +"_entity"),
                    { entity: EntityType<InternalEntity>, world : World-> InternalEntity(entity, world) },
                    { world: World, x: Double, y: Double, z: Double -> InternalEntity(world, x,y,z) },
                    RegistryManager.EntitySettings().size(0.5f, 0.5f))
        }

        constructor(internalEntityType : EntityType<InternalEntity>, world : World)
                : super(internalEntityType, world)
        constructor(world : World, livingEntity : LivingEntity, stack : ItemStack)
                : super(registeredType, world, livingEntity, stack)
        constructor(world: World, x: Double, y: Double, z: Double)
                : super(registeredType, world, x, y, z)

        override var attackDamage: Float = SpearSettings.Wooden.entityAttackDamage
    }
}