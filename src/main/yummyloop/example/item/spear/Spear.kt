package yummyloop.example.item.spear

import net.fabricmc.fabric.api.client.render.EntityRendererRegistry
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import yummyloop.example.util.registry.ClientManager
import yummyloop.example.util.registry.RegistryManager
import net.minecraft.item.ItemGroup as VanillaItemGroup

open class Spear(itemName: String, settings : Settings) : AbstractSpear(itemName, settings) {

    override val attackDamage = 4.5F
    override val attackSpeed = 1.1F
    override val velocityMod = 0.85F

    constructor(itemName : String) :
            this(itemName, Settings().group(VanillaItemGroup.COMBAT).maxDamage(60))

    override fun ini() {
        InternalEntity
        ClientManager.registerEntityRenderer(InternalEntity::class.java)
        { entityRenderDispatcher: EntityRenderDispatcher, context: EntityRendererRegistry.Context -> ThrownItemEntityRenderer(entityRenderDispatcher, context, this) }
    }

    override fun getThrownEntity(player: PlayerEntity, stack: ItemStack): ProjectileEntity {
        return InternalEntity(player.world, player, stack)
    }

    private class InternalEntity : AbstractSpearEntity {
        companion object{
            private var name : String = "spear"
            private val loyalty: TrackedData<Byte> = DataTracker.registerData<Byte>(InternalEntity::class.java, TrackedDataHandlerRegistry.BYTE)
            private val registeredType = RegistryManager.registerMiscEntityType(
                    (name+"_entity"),
                    { entity: EntityType<InternalEntity>, world : World-> InternalEntity(entity, world) },
                    { world: World, x: Double, y: Double, z: Double -> InternalEntity(world, x,y,z) })
        }

        constructor(internalEntityType : EntityType<InternalEntity>, world : World)
                : super(internalEntityType, world)
        constructor(world : World, livingEntity : LivingEntity, stack : ItemStack)
                : super(registeredType, world, livingEntity, stack)
        constructor(world: World, x: Double, y: Double, z: Double)
                : super(registeredType, world, x, y, z)

        override var attackDamage: Float = 4.5F

        override fun getLoyalty(): TrackedData<Byte> {
            return loyalty
        }
    }
}