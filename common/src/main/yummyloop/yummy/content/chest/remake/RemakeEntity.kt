package yummyloop.yummy.content.chest.remake

import me.shedaniel.architectury.registry.RegistrySupplier
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import yummyloop.common.network.packets.PacketBuffer
import yummyloop.common.network.packets.add
import yummyloop.yummy.content.chest.ChestScreenHandler

class RemakeEntity(blockEntityType: BlockEntityType<*>) : ExtendedMergedLootableContainerBlockEntity(blockEntityType) {
    constructor() : this(rType!!.get())

    override val internalInventory: Inventory = SimpleInventory(9)

    init {
        this.inventoryList.add(internalInventory)
    }

    companion object {
        var rType: RegistrySupplier<BlockEntityType<RemakeEntity>>? = null
    }

    override fun getContainerName(): Text {
        return TranslatableText(rType!!.id.toString())
    }

    override fun createScreenHandler(syncId: Int, playerInventory: PlayerInventory): ScreenHandler {
        return ChestScreenHandler(syncId, playerInventory, PacketBuffer(9, inventoryList.size), 9, inventoryList.size, this)
    }

    override fun saveExtraData(buf: PacketByteBuf) {
        buf.add(9,inventoryList.size)
    }

}