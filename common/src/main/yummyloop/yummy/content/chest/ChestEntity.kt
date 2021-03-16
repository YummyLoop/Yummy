package yummyloop.yummy.content.chest

import me.shedaniel.architectury.registry.RegistrySupplier
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.screen.NamedScreenHandlerFactory
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
import yummyloop.yummy.LOG

class ChestEntity : AnimatableBlockEntity(type!!.get()), NamedScreenHandlerFactory, ImplementedInventory {
    companion object {
        var type: RegistrySupplier<BlockEntityType<ChestEntity>>? = null
        private var isOpen = false
    }

    private val animationFactory = AnimationFactory(this)
    override fun getFactory(): AnimationFactory = this.animationFactory

    init {
        LOG.info("Calling from TestBlockEntity")
    }

    private var playedAnimation = true

    override fun onOpen(player: PlayerEntity?) {
        isOpen = true
        super.onOpen(player)

        playedAnimation = false
        LOG.info("event on open")
    }

    override fun onClose(player: PlayerEntity?) {
        super.onClose(player)
       // isOpen = false
        playedAnimation = false
        LOG.info("event on close")
    }

    private fun <P : IAnimatable> openPredicate(event: AnimationEvent<P>): PlayState {
        if (isOpen){
            LOG.info("event")
            event.controller.setAnimation(AnimationBuilder().addAnimation("open_chest", true))
        }else {
            LOG.info("event false")
            event.controller.setAnimation(AnimationBuilder().addAnimation("close_chest", false))
        }

        return PlayState.CONTINUE
    }

    override fun registerControllers(data: AnimationData) {
        val animationPredicate = object : AnimationController.IAnimationPredicate<IAnimatable> {
            override fun <P : IAnimatable> test(event: AnimationEvent<P>): PlayState = openPredicate(event)
        }

        data.addAnimationController(
            AnimationController(
                this,
                "controller",
                0F,
                animationPredicate
            )
        )
    }

    override val items: DefaultedList<ItemStack> = DefaultedList.ofSize(9, ItemStack.EMPTY)

    /**
     * Marks the state as dirty.
     * Must be called after changes in the inventory, so that the game can properly save
     * the inventory contents and notify neighboring blocks of inventory changes.
     */
    override fun markDirty() {
        super<AnimatableBlockEntity>.markDirty()
        super<ImplementedInventory>.markDirty()
    }


    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity?): ScreenHandler? {
        //We provide *this* to the screenHandler as our class Implements Inventory
        //Only the Server has the Inventory at the start, this will be synced to the client in the ScreenHandler
        return ChestScreenHandler(syncId, inv, this)
    }

    /**
     * Returns the title of this screen handler; will be a part of the open
     * screen packet sent to the client.
     */
    override fun getDisplayName(): Text = TranslatableText(cachedState.block.translationKey)

    override fun fromTag(state: BlockState?, tag: CompoundTag?) {
        super.fromTag(state, tag)
        Inventories.fromTag(tag, items)
    }

    override fun toTag(tag: CompoundTag?): CompoundTag? {
        super.toTag(tag)
        Inventories.toTag(tag, items)
        return tag
    }
}