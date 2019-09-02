package yummyloop.example.block.entity

import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import yummyloop.example.item.Items
import java.util.function.Supplier

class TestEntity : BlockEntity(type) {
    companion object Register {
        private val supplier = Supplier { TestEntity() } // Supplier
        private var blocks = listOf(Items.blockA)   // List of blocks to apply the entity to
        private val id = Identifier("tutorial", "example_block_entity")

        private val type = BlockEntityType.Builder.create(supplier, *blocks.toTypedArray()).build(null)!!
        private val register = Registry.register(Registry.BLOCK_ENTITY, id, type)
    }

    var number = 7

    init {
        println("Hello??? init")
        markDirty()
    }

    override fun toTag(tag: CompoundTag): CompoundTag {
        super.toTag(tag)

        // Save the current value of the number to the tag
        number++
        println(number)
        tag.putInt("number", number)

        return tag
    }

    override fun fromTag(tag: CompoundTag) {
        super.fromTag(tag)
        number = tag.getInt("number")
        println(number)
    }
}
