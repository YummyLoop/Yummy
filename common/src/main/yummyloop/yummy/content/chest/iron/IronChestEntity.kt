package yummyloop.yummy.content.chest.iron

import me.shedaniel.architectury.registry.RegistrySupplier
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import yummyloop.yummy.content.chest.doubleChest.DoubleChestEntity

class IronChestEntity(columns: Int, rows: Int) : DoubleChestEntity(rType!!.get(), columns, rows) {
    constructor() : this(9, 6)

    companion object {
        var rType: RegistrySupplier<BlockEntityType<IronChestEntity>>? = null
    }

    override fun getContainerName(): Text = TranslatableText(rType!!.id.toString())
}