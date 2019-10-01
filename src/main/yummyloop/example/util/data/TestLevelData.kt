package yummyloop.example.util.data

import net.minecraft.nbt.CompoundTag

class TestLevelData(name : String) : LevelData(name) {

    private var testInt=0

    init {
        //println("Hello world from test data")
        this.markDirty()
        //testInt++
    }

    override fun toTag(tag: CompoundTag): CompoundTag {
        tag.putInt("testInt", this.testInt)
        return tag
    }

    override fun fromTag(tag: CompoundTag) {
        this.testInt = tag.getInt("testInt")
    }
}