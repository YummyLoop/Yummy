package yummyloop.example.item.entity

import net.minecraft.block.Block
import net.minecraft.client.network.packet.ItemPickupAnimationS2CPacket
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.LightningEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvent
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.GameRules
import net.minecraft.world.RayTraceContext
import kotlin.random.Random

object EntityHelper {
    fun channelingEnchant(entityHit : Entity, owner : Entity?, hitThunderSound: SoundEvent, stack : ItemStack) : Boolean{
        // If the weather is thundering && has channeling enchant
        val world = entityHit.world
        if (world is ServerWorld && world.isThundering && EnchantmentHelper.hasChanneling(stack)) {
            val blockPos = entityHit.blockPos
            // If can see the sky
            if (world.isSkyVisible(blockPos)) {
                // Create lightning entity
                val lightningEntity = LightningEntity(world, blockPos.x.toDouble() + 0.5, blockPos.y.toDouble(), blockPos.z.toDouble() + 0.5, false)
                lightningEntity.setChanneller(if (owner is ServerPlayerEntity) owner else null)
                // Spawn lightning entity
                world.addLightning(lightningEntity)
                // Play sound
                entityHit.playSound(hitThunderSound, 5.0f, 1.0f)
                // Return true for successful channeling
                return true
            }
        }
        return false
    }
}

fun Entity.extinguishWhenWet(){
    if (this.isInsideWaterOrRain) {
        this.extinguish()
    }
}

fun Entity.didCollideWithBlock() : Boolean{
    val currentPosition = BlockPos(this.x, this.y, this.z)
    val currentBlock = this.world.getBlockState(currentPosition)
    if (!currentBlock.isAir && !noClip) {
        val blockCollisionBox = currentBlock.getCollisionShape(this.world, currentPosition)
        if (!blockCollisionBox.isEmpty) {
            val it = blockCollisionBox.boundingBoxes.iterator()
            while (it.hasNext()) {
                val box = it.next() as Box
                if (box.offset(currentPosition).contains(Vec3d(this.x, this.y, this.z))) {
                    return true
                }
            }
        }
    }
    return false
}

fun Entity.spawnCriticalParticles(){
    velocity = this.velocity
    val velocityX = velocity.x
    val velocityY = velocity.y
    val velocityZ = velocity.z
    for (i in 0..3) {
        this.world.addParticle(
                ParticleTypes.CRIT,
                this.x + velocityX * i.toDouble() / 4.0,
                this.y + velocityY * i.toDouble() / 4.0,
                this.z + velocityZ * i.toDouble() / 4.0,
                -velocityX,
                -velocityY + 0.2,
                -velocityZ
        )
    }
}

fun Entity.setInitialProjectilePitchYaw(){
    if (this.prevPitch == 0.0f && this.prevYaw == 0.0f) {
        val norm = MathHelper.sqrt(Entity.squaredHorizontalLength(velocity))
        this.yaw = Math.toDegrees(MathHelper.atan2(velocity.x, velocity.z)).toFloat()
        this.pitch = Math.toDegrees(MathHelper.atan2(velocity.y, norm.toDouble())).toFloat()
        this.prevYaw = this.yaw
        this.prevPitch = this.pitch
    }
}

fun Entity.handleEnvironmentDrag(airDrag : Float, waterDrag: Float){
    velocity = this.velocity
    val velocityX = velocity.x
    val velocityY = velocity.y
    val velocityZ = velocity.z
    var drag = airDrag
    if (this.isInsideWater) {
        for (i in 0..3) {
            this.world.addParticle(
                    ParticleTypes.BUBBLE,
                    this.x - velocityX * 0.25,
                    this.y - velocityY * 0.25,
                    this.z - velocityZ * 0.25,
                    velocityX,
                    velocityY,
                    velocityZ
            )
        }
        drag = waterDrag
    }
    this.velocity = velocity.multiply(drag.toDouble())
}

fun Entity.handleGravity(){
    if (!this.hasNoGravity() && !noClip) {
        val currentVelocity = this.velocity
        this.setVelocity(currentVelocity.x, currentVelocity.y - 0.05000000074505806, currentVelocity.z)
    }
}

fun Entity.setNextPosition(){
    val currentPosition = Vec3d(this.x, this.y, this.z)
    val nextPosition = currentPosition.add(this.velocity)
    this.setPosition(nextPosition.x, nextPosition.y, nextPosition.z)
}

fun Entity.handleProjectilePitchYaw(){
    velocity = this.velocity
    val velocityX = velocity.x
    val velocityY = velocity.y
    val velocityZ = velocity.z

    val norm = MathHelper.sqrt(Entity.squaredHorizontalLength(velocity))
    if (noClip) {
        this.yaw = Math.toDegrees(MathHelper.atan2(-velocityX, -velocityZ)).toFloat()
    } else {
        this.yaw = Math.toDegrees(MathHelper.atan2(velocityX, velocityZ)).toFloat()
    }

    this.pitch = Math.toDegrees(MathHelper.atan2(velocityY, norm.toDouble())).toFloat()
    while (this.pitch - this.prevPitch < -180.0f) {
        this.prevPitch -= 360.0f
    }

    while (this.pitch - this.prevPitch >= 180.0f) {
        this.prevPitch += 360.0f
    }

    while (this.yaw - this.prevYaw < -180.0f) {
        this.prevYaw -= 360.0f
    }

    while (this.yaw - this.prevYaw >= 180.0f) {
        this.prevYaw += 360.0f
    }

    this.pitch = MathHelper.lerp(0.2f, this.prevPitch, this.pitch)
    this.yaw = MathHelper.lerp(0.2f, this.prevYaw, this.yaw)
}

fun Entity.scaleVelocityByRandomFactor(factor : Float){
    val random = Random
    this.velocity = velocity.multiply((random.nextFloat() * factor).toDouble(), (random.nextFloat() * factor).toDouble(), (random.nextFloat() * factor).toDouble())
}

fun Entity.dropItemStack(stack: ItemStack) {
    if (!world.isClient) {
        this.remove()
        if (this.world.gameRules.getBoolean(GameRules.DO_ENTITY_DROPS)) {
            if (this.hasCustomName()) {
                stack.setCustomName(this.customName)
            }
            this.dropStack(stack)
        }
    }
}

fun Entity.placeBlock(at : BlockPos , block : Block){
    val blockAt = world.getBlockState(at)
    world.setBlockState(at, block.defaultState, 2)
    world.playLevelEvent(2001, at, Block.getRawIdFromState(blockAt))
}

fun Entity.hitResult(currentPosition : Vec3d, destinationPosition : Vec3d) : HitResult?{
    return this.world.rayTrace(RayTraceContext(currentPosition, destinationPosition, RayTraceContext.ShapeType.COLLIDER, RayTraceContext.FluidHandling.NONE, this))
}
fun Entity.hitResult(destinationPosition : Vec3d) : HitResult?{
    val currentPosition = Vec3d(this.x, this.y, this.z)
    return this.hitResult(currentPosition, destinationPosition)
}

fun Entity.sendItemPickup(entity: Entity, quantity: Int) {
    if (!entity.removed && !this.world.isClient) {
        (this.world as ServerWorld).method_14178().sendToOtherNearbyPlayers(entity, ItemPickupAnimationS2CPacket(entity.entityId, this.entityId, quantity))
    }
}
