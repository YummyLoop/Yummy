package yummyloop.example.item.spear

import net.fabricmc.fabric.api.client.render.EntityRendererRegistry
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ItemStack
import yummyloop.example.item.ItemGroup
import yummyloop.example.util.registry.ClientManager
import net.minecraft.item.ItemGroup as VanillaItemGroup

open class Spear(itemName: String, settings : Settings) : AbstractSpear(itemName, settings) {

    override val attackDamage = 4.5
    override val attackSpeed = 1.1
    override val velocityMod = 0.85F

    constructor(itemName : String) :
            this(itemName, Settings().group(VanillaItemGroup.COMBAT).maxDamage(60))

    override fun ini() {
        SpearEntity
        ClientManager.registerEntityRenderer(SpearEntity::class.java) { entityRenderDispatcher: EntityRenderDispatcher, context: EntityRendererRegistry.Context -> ThrownItemEntityRenderer(entityRenderDispatcher, context, this) }
    }

    override fun getThrownEntity(player: PlayerEntity, stack: ItemStack): ProjectileEntity {
        return SpearEntity(player.world, player, stack)
    }
}