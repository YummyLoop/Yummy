package yummyloop.yummy.content.chest.iron

import me.shedaniel.architectury.registry.RegistrySupplier
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import yummyloop.yummy.content.chest.singleChest.SingleChestEntity

class IronChestEntity(size: Int) : SingleChestEntity(rType!!.get(), size) {
    constructor() : this(54)

    companion object {
        var rType: RegistrySupplier<BlockEntityType<IronChestEntity>>? = null
    }

    override fun getContainerName(): Text = TranslatableText(rType!!.id.toString())
}