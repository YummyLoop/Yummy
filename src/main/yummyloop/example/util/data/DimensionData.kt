package yummyloop.example.util.data

import net.minecraft.server.world.ServerWorld
import net.minecraft.world.PersistentState

abstract class DimensionData(protected val serverWorld : ServerWorld,
                             protected val name : String)
    : PersistentState(name){
    init {
        this.markDirty()
    }

    abstract fun tick()
}