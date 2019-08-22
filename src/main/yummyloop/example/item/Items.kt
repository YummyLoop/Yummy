package yummyloop.example.item

import net.minecraft.block.Material
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import yummyloop.example.block.Block
import yummyloop.example.block.test2
import java.util.function.Supplier
import net.minecraft.item.Items as Vanilla

object Items {
   @JvmField val groupA = ItemGroup("tutorial", "hello1", Vanilla.ANVIL)
   @JvmField val F = Item("tutorial", "fabric_item1", groupA)

   @JvmField val itemX = Item("tutorial", "fabric_item", groupA)

   @JvmField val blockA = Block("tutorial", "example_block", Block.Settings.of(Material.METAL).lightLevel(10))
   @JvmField val blockItemA = BlockItem("tutorial", "example_block", blockA, groupA)


    lateinit var block_entity : BlockEntityType<BlockE>
    class BlockE: BlockEntity(block_entity) {
        init {
            println("Hello???")
        }
    }

    init {
        this.itemX.addTooltip("item.tutorial.fabric_item.tooltip")
        block_entity = BlockEntityType.Builder.create(Supplier<BlockE> { BlockE() }, blockA).build(null)
        val DEMO_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY, "modid:demo" , block_entity);

    }

}