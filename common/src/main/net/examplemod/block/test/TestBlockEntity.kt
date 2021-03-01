package net.examplemod.block.test

import me.shedaniel.architectury.registry.RegistrySupplier
import net.examplemod.ExampleMod
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
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

class TestBlockEntity : BlockEntity(type?.get()), IAnimatable, NamedScreenHandlerFactory, ImplementedInventory {
    companion object {
        var type: RegistrySupplier<BlockEntityType<BlockEntity>>? = null
    }

    private val animationFactory = AnimationFactory(this)
    override fun getFactory(): AnimationFactory = this.animationFactory

    init {
        ExampleMod.log.info("Calling from TestBlockEntity")

    }

    private fun <P : IAnimatable> predicate(event: AnimationEvent<P>): PlayState {
        event.controller.setAnimation(AnimationBuilder().addAnimation("Soaryn_chest_popup", true))
        return PlayState.CONTINUE
    }

    override fun registerControllers(data: AnimationData) {
        val animationPredicate = object : AnimationController.IAnimationPredicate<IAnimatable> {
            override fun <P : IAnimatable> test(event: AnimationEvent<P>): PlayState = predicate(event)
        }
        data.addAnimationController(
            AnimationController(
                this,
                "controller",
                20F,
                animationPredicate
            )
        )
    }

    private val inventory: DefaultedList<ItemStack> = DefaultedList.ofSize(9, ItemStack.EMPTY)

    override val items: DefaultedList<ItemStack>
        get() = this.inventory

    /**
     * Marks the state as dirty.
     * Must be called after changes in the inventory, so that the game can properly save
     * the inventory contents and notify neighboring blocks of inventory changes.
     */
    override fun markDirty() {
        super<BlockEntity>.markDirty()
    }


    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity?): ScreenHandler? {
        //We provide *this* to the screenHandler as our class Implements Inventory
        //Only the Server has the Inventory at the start, this will be synced to the client in the ScreenHandler
        return BoxScreenHandler(syncId, inv, this)
    }

    /**
     * Returns the title of this screen handler; will be a part of the open
     * screen packet sent to the client.
     */
    override fun getDisplayName(): Text = TranslatableText(cachedState.block.translationKey)

    override fun fromTag(state: BlockState?, tag: CompoundTag?) {
        super.fromTag(state, tag)
        Inventories.fromTag(tag, inventory)
    }

    override fun toTag(tag: CompoundTag?): CompoundTag? {
        super.toTag(tag)
        Inventories.toTag(tag, inventory)
        return tag
    }
}