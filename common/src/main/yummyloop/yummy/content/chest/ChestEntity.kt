package yummyloop.yummy.content.chest

import me.shedaniel.architectury.registry.RegistrySupplier
import me.shedaniel.architectury.registry.menu.ExtendedMenuProvider
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.collection.DefaultedList
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.core.PlayState
import software.bernie.geckolib3.core.builder.AnimationBuilder
import software.bernie.geckolib3.core.controller.AnimationController
import software.bernie.geckolib3.core.event.predicate.AnimationEvent
import software.bernie.geckolib3.core.manager.AnimationData
import software.bernie.geckolib3.core.manager.AnimationFactory
import yummyloop.common.integration.gecko.AnimatableBlockEntity
import yummyloop.common.network.packets.PacketBuffer
import yummyloop.yummy.LOG

class ChestEntity : AnimatableBlockEntity(type!!.get()), ImplementedInventory, ExtendedMenuProvider {
    override val items: DefaultedList<ItemStack> = DefaultedList.ofSize(9, ItemStack.EMPTY)
    private val animationFactory = AnimationFactory(this)
    override fun getFactory(): AnimationFactory = this.animationFactory

    companion object {
        var type: RegistrySupplier<BlockEntityType<ChestEntity>>? = null
    }

    var isOpen = 0

    init {
        LOG.info("Calling from TestBlockEntity")
    }

    private fun <P> openPredicate(event: AnimationEvent<P>): PlayState where P : ChestEntity {
        if (isOpen == 1) {
            //LOG.info("event")
            event.controller.setAnimation(AnimationBuilder().addAnimation("open_chest", true))
        } else {
            event.controller.setAnimation(AnimationBuilder().addAnimation("close_chest", false))
        }

        return PlayState.CONTINUE
    }


    inner class IAnimationPredicate<I>(private val v: (AnimationEvent<I>) -> PlayState) :
        AnimationController.IAnimationPredicate<I> where I : IAnimatable {
        override fun <P> test(event: AnimationEvent<P>): PlayState where P : IAnimatable {
            return (v as (AnimationEvent<P>) -> PlayState).invoke(event)
        }
    }

    override fun registerControllers(data: AnimationData) {
        data.addAnimationController(
            AnimationController(
                this,
                "controller",
                0F,
                IAnimationPredicate(::openPredicate)
            )
        )
    }

    override fun onOpen(player: PlayerEntity?) {
        super.onOpen(player)
        //isOpen=true
        //LOG.info("event on open ")
    }

    override fun onClose(player: PlayerEntity?) {
        super.onClose(player)
        isOpen=0
        onInvOpenOrClose()
        LOG.info("event on close ${this.pos}")
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    // Sync between server -> client
    ////////////////////////////////////////////////////////////////////////////////////////////
    protected fun onInvOpenOrClose() {
        val block = cachedState.block
        if (block is ChestBlock) {
            world!!.addSyncedBlockEvent(pos, block, 1, this.isOpen)
            world!!.updateNeighborsAlways(pos, block)
        }
    }

    override fun onSyncedBlockEvent(type: Int, data: Int): Boolean {
        return if (type == 1) {
            this.isOpen = data
            true
        } else {
            super.onSyncedBlockEvent(type, data)
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Marks the state as dirty.
     * Must be called after changes in the inventory, so that the game can properly save
     * the inventory contents and notify neighboring blocks of inventory changes.
     */
    override fun markDirty() {
        super<AnimatableBlockEntity>.markDirty()
        super<ImplementedInventory>.markDirty()
    }

    override fun fromTag(state: BlockState?, tag: CompoundTag?) {
        super.fromTag(state, tag)
        Inventories.fromTag(tag, items)
    }

    override fun toTag(tag: CompoundTag?): CompoundTag? {
        super.toTag(tag)
        Inventories.toTag(tag, items)
        return tag
    }


    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity?): ScreenHandler {
        //We provide *this* to the screenHandler as our class Implements Inventory
        //Only the Server has the Inventory at the start, this will be synced to the client in the ScreenHandler
        return ChestScreenHandler(syncId, inv, PacketBuffer(), this)
    }

    /**
     * Returns the title of this screen handler; will be a part of the open
     * screen packet sent to the client.
     */
    override fun getDisplayName(): Text = TranslatableText("a_chest")

    override fun saveExtraData(buf: PacketByteBuf?) {
    }
}