package yummyloop.example.block

import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntityType
import yummyloop.example.config.Config
import java.util.function.Supplier
import kotlin.reflect.KFunction0
import net.minecraft.block.entity.BlockEntity as VanillaBlockEntity

abstract class BlockEntity(type : BlockEntityType<*>) : VanillaBlockEntity(type) {
    companion object{
        fun <T : VanillaBlockEntity> createType(entity : KFunction0<T>, blocks : List<Block?>) : BlockEntityType<T> {
            if (blocks.contains(null)) Config.logger.fatal("null block for BlockEntity")
            return BlockEntityType.Builder.create(Supplier(entity), *blocks.toTypedArray()).build(null)
        }
    }
}