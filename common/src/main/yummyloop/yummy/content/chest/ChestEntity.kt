package yummyloop.yummy.content.chest

import me.shedaniel.architectury.registry.RegistrySupplier
import me.shedaniel.architectury.registry.menu.ExtendedMenuProvider
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.LootableContainerBlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandler
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.collection.DefaultedList
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
import yummyloop.common.network.packets.PacketBuffer

class ChestEntity : LootableContainerBlockEntity(type!!.get()), IAnimatable, ExtendedMenuProvider {
    private var items: DefaultedList<ItemStack> = DefaultedList.ofSize(9, ItemStack.EMPTY)
    private var isOpen = -1
    private var playedSound = 0
    private val animationFactory = AnimationFactory(this)
    private val animationController = AnimationController(this, "controller", 0F, AnimationPredicate(::openPredicate))
    override fun getFactory(): AnimationFactory = this.animationFactory

    companion object {
        var type: RegistrySupplier<BlockEntityType<ChestEntity>>? = null
    }

    init {
        //LOG.info("Calling from TestBlockEntity")
    }

    /** Gecko animation predicate */
    private fun <P> openPredicate(event: AnimationEvent<P>): PlayState where P : ChestEntity {
        when (isOpen) {
            1 -> event.controller.setAnimation(AnimationBuilder()
                .addAnimation("open_chest", false)
                .addAnimation("open_chest_idle", true))
            0 -> event.controller.setAnimation(AnimationBuilder()
                .addAnimation("close_chest", false))
            else -> {
            }
        }
        return PlayState.CONTINUE
    }

    /** Gecko sound event listener */
    private fun <E> soundListener(event: SoundKeyframeEvent<E>) where E : ChestEntity {
        val player: ClientPlayerEntity = MinecraftClient.getInstance().player!!
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
        animationController.registerSoundListener(SoundListener(::soundListener))
        data.addAnimationController(animationController)
    }

    /** On Inventory Open*/
    override fun onOpen(player: PlayerEntity?) {
        super.onOpen(player)
        if (isOpen <= 0) isOpen = 1 else isOpen += 1
        onInvOpenOrClose()
        //LOG.info("event on open ")
    }

    /** On Inventory Close*/
    override fun onClose(player: PlayerEntity?) {
        super.onClose(player)
        isOpen -= 1
        onInvOpenOrClose()
        //LOG.info("event on close ${this.pos}")
    }

    /** Request data sync when an inventory is open or closed */
    private fun onInvOpenOrClose() {
        val block = cachedState.block
        if (block is ChestBlock) {
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

    override fun size(): Int = items.size

    /** Load blockEntity from tag */
    override fun fromTag(state: BlockState?, tag: CompoundTag?) {
        super.fromTag(state, tag)
        this.items = DefaultedList.ofSize(size(), ItemStack.EMPTY)
        if (!deserializeLootTable(tag)) {
            Inventories.fromTag(tag, this.items)
        }
    }

    /** Save blockEntity to tag */
    override fun toTag(tag: CompoundTag?): CompoundTag? {
        super.toTag(tag)
        if (!serializeLootTable(tag)) {
            Inventories.toTag(tag, this.items)
        }

        return tag
    }

    /** Screen provider, create menu */
    override fun createScreenHandler(syncId: Int, playerInventory: PlayerInventory): ScreenHandler =
        ChestScreenHandler(syncId, playerInventory, PacketBuffer(), this)

    /** Screen provider, packet extra data */
    override fun saveExtraData(buf: PacketByteBuf?) {
    }

    override fun getContainerName(): Text = TranslatableText("a_chest")

    override fun getInvStackList(): DefaultedList<ItemStack> = this.items

    override fun setInvStackList(list: DefaultedList<ItemStack>) {
        this.items = list
    }
}