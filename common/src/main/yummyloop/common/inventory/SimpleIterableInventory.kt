package yummyloop.common.inventory

import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack

class SimpleIterableInventory : SimpleInventory, IterableInventory {

    constructor(vararg itemStacks: ItemStack) : super(*itemStacks)
    constructor(size: Int) : super(size)

    override val size: Int
        get() = this.size()
}