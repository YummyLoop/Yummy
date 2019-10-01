package yummyloop.example.util.data

import net.minecraft.world.PersistentState

abstract class LevelData (private val name : String) : PersistentState(name) {
    init {
        this.markDirty()
    }

    fun getName(): String {
        return this.name
    }
}
