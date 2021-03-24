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
import yummyloop.common.integration.gecko.setLoopingAnimation
import yummyloop.yummy.content.chest.doubleChest.DoubleChestBlock

abstract class AnimatableChestContainerBlockEntity(type: BlockEntityType<*>, size: Int) : IAnimatable,
    LootableContainerBlockEntityImpl(type, size) {
    var isOpen = -1
    private var first = true
    protected var playedSound = 0
    private var firstDouble = true
    protected open val animationFactory: AnimationFactory by lazy { AnimationFactory(this) }

    override fun getFactory(): AnimationFactory = this.animationFactory

    /** Gecko animation predicate */
    protected fun <P> openPredicate(event: AnimationEvent<P>): PlayState where P : AnimatableChestContainerBlockEntity {
        val animationBuilder = AnimationBuilder()
        val world = event.animatable.world

        if (world != null) {
            val state = world.getBlockState(event.animatable.pos)
            try {
                if (state.get(DoubleChestBlock.CHEST_TYPE) == ChestType.RIGHT) {
                    /*if (firstDouble) {
                        firstDouble = false
                        //event.controller.clearAnimationCache()

                    }*/
                    event.setLoopingAnimation("idle")
                    return PlayState.STOP
                } else if (state.get(DoubleChestBlock.CHEST_TYPE) == ChestType.LEFT) {
                    /*if (firstDouble) {
                        firstDouble = false
                        //event.controller.clearAnimationCache()
                        event.controller.markNeedsReload()
                    }*/
                } else {
                    /*if (!firstDouble) {
                        firstDouble = true
                        //event.controller.clearAnimationCache()
                        event.controller.markNeedsReload()
                    }*/
                }
            } catch (e: Exception) {
                //...
            }
        }

        if (!first) event.controller.transitionLengthTicks = 2.0 else first = false

        when {
            isOpen >= 1 -> {
                animationBuilder
                    .addAnimation("open", false)
                    .addAnimation("idle_open", true)
            }
            isOpen == 0 -> {
                animationBuilder
                    .addAnimation("close", false)
                    .addAnimation("idle", true)
                if (event.isCurrentAnimation("idle")) {
                    isOpen = -1
                }
            }
            else -> animationBuilder.addAnimation("idle", true)
        }


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
        animationController.markNeedsReload()
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