package yummyloop.example.util.data

import net.minecraft.nbt.CompoundTag
import net.minecraft.server.world.ServerWorld

class TestData(world : ServerWorld, name : String) : DimensionData(world, name) {

    override fun tick() {
        //testInt++
        //println(testInt)
        //this.markDirty()
    }

    private var testInt=0

    init {// Changes made here will only take effect the first time the data is saved/created
        this.markDirty()
    }

    override fun toTag(tag: CompoundTag): CompoundTag {
        tag.putInt("testInt", this.testInt)
        return tag
    }

    override fun fromTag(tag: CompoundTag) {
        this.testInt = tag.getInt("testInt")
    }
}