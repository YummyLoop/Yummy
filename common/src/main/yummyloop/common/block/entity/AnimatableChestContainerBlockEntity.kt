package yummyloop.common.block.entity

import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.enums.ChestType
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
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
import yummyloop.yummy.content.chest.doubleChest.DoubleChestEntity

abstract class AnimatableChestContainerBlockEntity(type: BlockEntityType<*>, size: Int, blockPos: BlockPos?,
                                                   blockState: BlockState?
) : IAnimatable,
    LootableContainerBlockEntityImpl(type, size, blockPos, blockState) {
    var isOpen = -1
    private var playedSound = 0
    protected open val animationFactory: AnimationFactory by lazy { AnimationFactory(this) }

    override fun getFactory(): AnimationFactory = this.animationFactory

    /** Gecko animation predicate */
    private fun <P> openPredicate(event: AnimationEvent<P>): PlayState where P : AnimatableChestContainerBlockEntity {
        val world = event.animatable.world

        if (world != null) {
            val animationBuilder = AnimationBuilder()
            val state = world.getBlockState(event.animatable.pos)

            if (event.animatable is DoubleChestEntity) try {
                if (state.get(DoubleChestBlock.CHEST_TYPE) == ChestType.RIGHT) {
                    event.setLoopingAnimation("idle")
                    return PlayState.STOP
                }
            } catch (e: Exception) {
                //...
            }

            when {
                isOpen >= 1 -> {
                    if (event.isCurrentAnimation("idle_open")) {
                        animationBuilder
                            .addAnimation("idle_open", true)
                    } else {
                        animationBuilder
                            .addAnimation("open", false)
                            .addAnimation("idle_open", true)
                    }
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
        }

        return PlayState.CONTINUE
    }

    /** Gecko sound event listener */
    private fun <E> soundListener(event: SoundKeyframeEvent<E>) where E : AnimatableChestContainerBlockEntity {
        val player = MinecraftClient.getInstance().player
        val world = event.entity.world

        if (world != null && player != null) {
            if (event.sound == "open_sound" && playedSound != 1) {
                player.playSound(SoundEvents.BLOCK_CHEST_OPEN,
                    0.5F,
                    0.9F + 0.1F * world.random.nextFloat())
                playedSound = 1
            } else if (event.sound == "close_sound" && playedSound != 2) {
                player.playSound(SoundEvents.BLOCK_CHEST_CLOSE,
                    0.5F,
                    0.9F + 0.1F * world.random.nextFloat())
                playedSound = 2
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
        //animationController.markNeedsReload()
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