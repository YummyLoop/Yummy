package yummyloop.yummy.old.util.data

import net.minecraft.server.world.ServerWorld
import net.minecraft.world.PersistentState

abstract class DimensionData(private val serverWorld : ServerWorld,
                             private val name : String)
    : PersistentState(name){
    init {
        this.markDirty()
    }

    abstract fun tick()

    fun getWorld(): ServerWorld {
        return this.serverWorld
    }

    fun getName(): String {
        return this.name
    }
}