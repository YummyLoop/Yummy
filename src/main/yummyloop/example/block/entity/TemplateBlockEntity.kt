package yummyloop.example.block.entity

import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import yummyloop.example.item.Items
import yummyloop.example.item.backpack.HasClient
import java.util.function.Supplier

class TemplateBlockEntity : BlockEntity(type){
    companion object Register : HasClient  {
        var clientIni = false
        private val supplier = Supplier { TemplateBlockEntity() } // Supplier
        private var blocks = listOf(Items.blockA)   // List of blocks to apply the entity to
        private val type = BlockEntityType.Builder.create(supplier, *blocks.toTypedArray()).build(null)!!
        init {
            val id = Identifier("tutorial", "example_block_entity")
            Registry.register(Registry.BLOCK_ENTITY, id, type)
        }

        override fun client () {
            if (!clientIni){
                clientIni=true
                BlockEntityRendererRegistry.INSTANCE.register(TemplateBlockEntity::class.java, TemplateBlockEntityRenderer())
            }
        }
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
