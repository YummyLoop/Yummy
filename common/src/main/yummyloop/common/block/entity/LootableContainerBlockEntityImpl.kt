package yummyloop.common.block.entity

import me.shedaniel.architectury.registry.menu.ExtendedMenuProvider
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.LootableContainerBlockEntity
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.collection.DefaultedList

abstract class LootableContainerBlockEntityImpl(type: BlockEntityType<*>, size: Int) :
    LootableContainerBlockEntity(type) {
    protected var items: DefaultedList<ItemStack> = DefaultedList.ofSize(size, ItemStack.EMPTY)

    override fun size(): Int = this.items.size

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

    override fun getInvStackList(): DefaultedList<ItemStack> = this.items

    override fun setInvStackList(list: DefaultedList<ItemStack>) {
        this.items = list
    }
}