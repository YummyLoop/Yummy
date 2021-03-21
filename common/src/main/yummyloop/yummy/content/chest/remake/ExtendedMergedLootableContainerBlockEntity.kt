package yummyloop.yummy.content.chest.remake

import me.shedaniel.architectury.registry.menu.ExtendedMenuProvider
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.LootableContainerBlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.Direction
import yummyloop.common.inventory.IMergedInventory
import yummyloop.common.inventory.fromTag
import yummyloop.common.inventory.toTag

abstract class ExtendedMergedLootableContainerBlockEntity(blockEntityType: BlockEntityType<*>) :
    LootableContainerBlockEntity(blockEntityType), ExtendedMenuProvider, SidedInventory, IMergedInventory {

    override val inventoryList = mutableListOf<Inventory>()
    abstract val internalInventory : Inventory

    override fun markDirty() {
        super<IMergedInventory>.markDirty()
    }

    override fun clear() {
        super<IMergedInventory>.clear()
    }

    override fun size(): Int {
        return super<IMergedInventory>.size()
    }

    override fun isEmpty(): Boolean {
        checkLootInteraction(null)
        return super<IMergedInventory>.isEmpty()
    }

    override fun getStack(slot: Int): ItemStack {
        checkLootInteraction(null)
        return super<IMergedInventory>.getStack(slot)
    }

    override fun removeStack(slot: Int, amount: Int): ItemStack {
        checkLootInteraction(null)
        return super<IMergedInventory>.removeStack(slot, amount)
    }

    override fun removeStack(slot: Int): ItemStack {
        checkLootInteraction(null)
        return super<IMergedInventory>.removeStack(slot)
    }

    override fun setStack(slot: Int, stack: ItemStack) {
        checkLootInteraction(null)
        return super<IMergedInventory>.setStack(slot, stack)
    }

    override fun canPlayerUse(player: PlayerEntity): Boolean {
        return super<IMergedInventory>.canPlayerUse(player)
    }

    override fun getInvStackList(): DefaultedList<ItemStack> {
        val itemStackList : DefaultedList<ItemStack> = DefaultedList.of()
        inventoryList.forEach {
            for (i in 0 until it.size()){
                itemStackList.add(it.getStack(i))
            }
        }
        return itemStackList
    }

    override fun setInvStackList(list: DefaultedList<ItemStack>) {
        inventoryList.clear()
        inventoryList.add(SimpleInventory(*list.toTypedArray()))
    }

    /** Load blockEntity from tag */
    override fun fromTag(state: BlockState, tag: CompoundTag) {
        super.fromTag(state, tag)
        if (!deserializeLootTable(tag)) internalInventory.fromTag(tag)
    }

    /** Save blockEntity to tag */
    override fun toTag(tag: CompoundTag): CompoundTag {
        super.toTag(tag)
        if (!serializeLootTable(tag)) internalInventory.toTag(tag)
        return tag
    }

    override fun getAvailableSlots(side: Direction): IntArray {
        return IntArray(this.size()) {it}
    }

    /**
     * Determines whether the given stack can be inserted into this inventory at the specified slot position from the given direction.
     */
    override fun canInsert(slot: Int, stack: ItemStack?, dir: Direction?): Boolean = true

    /**
     * Determines whether the given stack can be removed from this inventory at the specified slot position from the given direction.
     */
    override fun canExtract(slot: Int, stack: ItemStack?, dir: Direction?): Boolean = true

}