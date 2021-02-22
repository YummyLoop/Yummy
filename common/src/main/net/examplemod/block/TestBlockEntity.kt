package net.examplemod.block

import me.shedaniel.architectury.registry.RegistrySupplier
import net.examplemod.ExampleMod
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType

class TestBlockEntity : BlockEntity(type?.get()) {
    companion object {
        var type: RegistrySupplier<BlockEntityType<BlockEntity>>? = null
    }

    init {
        ExampleMod.log.info("Calling from TestBlockEntity")
    }
}