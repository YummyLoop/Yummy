package yummyloop.common.block.entity

import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.enums.ChestType
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.sound.SoundEvents
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.core.PlayState
import software.bernie.geckolib3.core.builder.AnimationBuilder
import software.bernie.geckolib3.core.controller.AnimationController
import software.bernie.geckolib3.core.event.SoundKeyframeEvent
import software.bernie.geckolib3.core.event.predicate.AnimationEvent
import software.bernie.geckolib3.core.manager.AnimationData
import software.bernie.geckolib3.core.manager.AnimationFactory
import yummyloop.common.integration.gecko.AnimationPredicate
import yummyloop.common.integration.gecko.SoundListener
import yummyloop.common.integration.gecko.isCurrentAnimation
import yummyloop.yummy.content.chest.doubleChest.DoubleChestBlock

abstract class AnimatableChestContainerBlockEntity(type: BlockEntityType<*>, size: Int) : IAnimatable,
    LootableContainerBlockEntityImpl(type, size) {
    var isOpen = -1
    protected var playedSound = 0
    protected open val animationFactory: AnimationFactory by lazy { AnimationFactory(this) }

    override fun getFactory(): AnimationFactory = this.animationFactory

    /** Gecko animation predicate */
    protected fun <P> openPredicate(event: AnimationEvent<P>): PlayState where P : AnimatableChestContainerBlockEntity {
        val animationBuilder = AnimationBuilder()
        val world = event.animatable.world
        val pos = event.animatable.pos
        var isDoubleChest = 0

        if (world != null) {
            val state = world.getBlockState(pos)
            try {
                when (state.get(DoubleChestBlock.CHEST_TYPE)) {
                    ChestType.RIGHT -> isDoubleChest = 2
                    ChestType.LEFT -> isDoubleChest = 1
                    else -> isDoubleChest = 0
                }
            } catch (e: Exception) {
                //...
            }
        }

        when (isDoubleChest) {
            2 -> when {
                isOpen >= 2 -> {
                    animationBuilder
                        .addAnimation("double_idle_open_right", true)
                }
                isOpen == 1 -> {
                    animationBuilder
                        .addAnimation("double_open_right", false)
                        .addAnimation("double_idle_open_right", true)
                }
                isOpen == 0 -> {
                    animationBuilder
                        .addAnimation("double_close_right", false)
                        .addAnimation("double_idle_right", true)
                    if (event.controller.isCurrentAnimation("double_idle_right")) {
                        isOpen = -1
                    }
                }
                else -> {
                    animationBuilder.addAnimation("double_idle_right", true)
                }
            }

            1 -> when {
                isOpen >= 2 -> {
                    animationBuilder
                        .addAnimation("double_idle_open_left", true)
                }
                isOpen == 1 -> {
                    animationBuilder
                        .addAnimation("double_open_left", false)
                        .addAnimation("double_idle_open_left", true)
                }
                isOpen == 0 -> {
                    animationBuilder
                        .addAnimation("double_close_left", false)
                        .addAnimation("double_idle_left", true)
                    if (event.controller.isCurrentAnimation("double_idle_left")) {
                        isOpen = -1
                    }
                }
                else -> {
                    animationBuilder.addAnimation("double_idle_left", true)
                }
            }

            else -> when {
                isOpen >= 2 -> {
                    animationBuilder
                        .addAnimation("idle_open", true)
                }
                isOpen == 1 -> {
                    animationBuilder
                        .addAnimation("open", false)
                        .addAnimation("idle_open", true)
                }
                isOpen == 0 -> {
                    animationBuilder
                        .addAnimation("close", false)
                        .addAnimation("idle", true)
                    if (event.controller.isCurrentAnimation("idle")) {
                        isOpen = -1
                    }
                }
                else -> animationBuilder.addAnimation("idle", true)
            }
        }


        //event.controller.clearAnimationCache()
        event.controller.setAnimation(animationBuilder)

        return PlayState.CONTINUE
    }

    /** Gecko sound event listener */
    protected fun <E> soundListener(event: SoundKeyframeEvent<E>) where E : AnimatableChestContainerBlockEntity {
        val player: ClientPlayerEntity = MinecraftClient.getInstance().player!!
        val world = event.entity.world

        if (world != null) {
            val pos = event.entity.pos
            val state = world.getBlockState(pos)
            try {
                if (state.get(DoubleChestBlock.CHEST_TYPE) == ChestType.RIGHT) return
            } catch (e: Exception) {
                //...
            }
        }

        when (isOpen) {
            1 -> {
                if (playedSound != 1) player.playSound(SoundEvents.BLOCK_CHEST_OPEN,
                    0.5F,
                    0.9F + 0.1F * this.world!!.random.nextFloat())
                playedSound = 1
            }
            0 -> {
                if (playedSound != 2) player.playSound(SoundEvents.BLOCK_CHEST_CLOSE,
                    0.5F,
                    0.9F + 0.1F * this.world!!.random.nextFloat())
                playedSound = 2
            }
            else -> {
                playedSound = 0
            }
        }
    }

    /** Gecko animation controller registry */
    override fun registerControllers(data: AnimationData) {
        val animationController = AnimationController(
            this,
            "controller",
            0F,
            AnimationPredicate(this::openPredicate)
        )
        animationController.registerSoundListener(SoundListener(::soundListener))
        data.addAnimationController(animationController)
    }

    /** On Inventory Open*/
    override fun onOpen(player: PlayerEntity?) {
        super.onOpen(player)
        if (isOpen <= 0) isOpen = 1 else isOpen += 1
        onInvOpenOrClose()
    }

    /** On Inventory Close*/
    override fun onClose(player: PlayerEntity?) {
        super.onClose(player)
        isOpen -= 1
        onInvOpenOrClose()
    }

    /** Request data sync when an inventory is open or closed */
    protected fun onInvOpenOrClose() {
        val block = cachedState.block
        if (block is BlockWithEntity) {//todo : change this to a better type
            world!!.addSyncedBlockEvent(pos, block, 1, this.isOpen)
            world!!.updateNeighborsAlways(pos, block)
        }
    }

    /** Sync data between the server, and other client versions of the Block Entity */
    override fun onSyncedBlockEvent(type: Int, data: Int): Boolean {
        return if (type == 1) {
            this.isOpen = data
            true
        } else {
            super.onSyncedBlockEvent(type, data)
        }
    }
}