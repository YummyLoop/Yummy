package yummyloop.yummy.content.chest.doubleChest

import me.shedaniel.architectury.registry.RegistrySupplier
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.enums.ChestType
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.math.Direction
import yummyloop.common.block.entity.AnimatableChestContainerBlockEntity
import yummyloop.common.network.packets.PacketBuffer
import yummyloop.common.network.packets.add
import yummyloop.yummy.content.chest.ChestScreenHandler
import yummyloop.common.inventory.MergedInventory


open class DoubleChestEntity(type: BlockEntityType<*>, var columns: Int, var rows: Int) :
    AnimatableChestContainerBlockEntity(type, columns * rows), SidedInventory {
    constructor(columns: Int, rows: Int) : this(rType!!.get(), columns, rows)
    constructor() : this(9, 3)

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
                columns,
                2 * rows,
                mergedInventory
            )
        }

        return ChestScreenHandler(syncId, playerInventory, PacketBuffer(), columns, rows, this)
    }

    override fun getContainerName(): Text = TranslatableText(rType!!.id.toString())

    /**
     * Gets the available slot positions that are reachable from a given side.
     */
    override fun getAvailableSlots(side: Direction?): IntArray {
        return IntArray(this.size()){it}
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