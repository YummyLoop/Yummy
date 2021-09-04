package yummyloop.common.block.entity

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.LootableContainerBlockEntity
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos

abstract class LootableContainerBlockEntityImpl(
    type: BlockEntityType<*>, size: Int, blockPos: BlockPos?,
    blockState: BlockState?,
) : LootableContainerBlockEntity(type, blockPos, blockState) {
    protected var items: DefaultedList<ItemStack> = DefaultedList.ofSize(size, ItemStack.EMPTY)

    override fun size(): Int = this.items.size

    /** Load blockEntity from tag */
    override fun readNbt(tag: NbtCompound?) {
        super.readNbt(tag)
        this.items = DefaultedList.ofSize(size(), ItemStack.EMPTY)
        if (!deserializeLootTable(tag)) {
            Inventories.readNbt(tag, this.items)
        }
    }

    /** Save blockEntity to tag */
    override fun writeNbt(tag: NbtCompound?): NbtCompound? {
        super.writeNbt(tag)
        if (!serializeLootTable(tag)) {
            Inventories.readNbt(tag, this.items)
        }

        return tag
    }

    override fun getInvStackList(): DefaultedList<ItemStack> = this.items

    override fun setInvStackList(list: DefaultedList<ItemStack>) {
        this.items = list
    }
}