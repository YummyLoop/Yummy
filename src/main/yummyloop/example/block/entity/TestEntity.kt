package yummyloop.example.block.entity

import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.registry.Registry
import yummyloop.example.block.Block
import java.util.function.Supplier
import net.minecraft.nbt.CompoundTag

lateinit var block_entity : BlockEntityType<TestEntity>
class TestEntity(block : Block) : BlockEntity(block_entity) {

    var number = 7

    init {
        println("Hello???")
        block_entity = Registry.register(Registry.BLOCK_ENTITY, "modid:demo" ,  BlockEntityType.Builder.create(Supplier<TestEntity> { TestEntity(block) }, block).build(null));
    }

    override fun toTag(tag: CompoundTag): CompoundTag {
        super.toTag(tag)

        // Save the current value of the number to the tag
        number++
        tag.putInt("number", number)

        return tag
    }

    override fun fromTag(tag: CompoundTag) {
        super.fromTag(tag)
        number = tag.getInt("number")
    }
}
