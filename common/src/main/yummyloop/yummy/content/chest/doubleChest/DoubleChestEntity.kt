package yummyloop.yummy.content.chest.doubleChest

import me.shedaniel.architectury.registry.RegistrySupplier
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.enums.ChestType
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.math.Direction
import yummyloop.common.inventory.MergedInventory
import yummyloop.common.network.packets.PacketBuffer
import yummyloop.yummy.content.chest.ChestScreenHandler
import yummyloop.yummy.content.chest.singleChest.SingleChestEntity


open class DoubleChestEntity(type: BlockEntityType<*>, size: Int) : SingleChestEntity(type, size), SidedInventory {
    constructor(size: Int) : this(rType!!.get(), size)
    constructor() : this(27)

    companion object {
        var rType: RegistrySupplier<BlockEntityType<DoubleChestEntity>>? = null
    }

    init {
        //
    }

    /** Screen provider, create menu */
    override fun createScreenHandler(syncId: Int, playerInventory: PlayerInventory): ScreenHandler {
        val state = world?.getBlockState(pos)
        if (state?.get(DoubleChestBlock.CHEST_TYPE) == ChestType.LEFT) {
            val doubleChestPos = pos!!.offset(DoubleChestBlock.getDoubleChestDirection(state))
            val mergedInventory = MergedInventory(this, world?.getBlockEntity(doubleChestPos) as Inventory)
            return ChestScreenHandler(syncId, playerInventory, PacketBuffer(),
                size * 2,
                mergedInventory
            )
        }

        return super.createScreenHandler(syncId, playerInventory)
    }

    override fun getContainerName(): Text = TranslatableText(rType!!.id.toString())

    /**
     * Gets the available slot positions that are reachable from a given side.
     */
    override fun getAvailableSlots(side: Direction?): IntArray {
        return IntArray(this.size()) { it }
    }

    /**
     * Determines whether the given stack can be inserted into this inventory at the specified slot position from the given direction.
     */
    override fun canInsert(slot: Int, stack: ItemStack?, dir: Direction?): Boolean {
        return true
    }

    /**
     * Determines whether the given stack can be removed from this inventory at the specified slot position from the given direction.
     */
    override fun canExtract(slot: Int, stack: ItemStack?, dir: Direction?): Boolean {
        return true
    }

}