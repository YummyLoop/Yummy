package yummyloop.example.util.data

import net.minecraft.nbt.CompoundTag
import net.minecraft.server.world.ServerWorld

class TestData(world : ServerWorld, name : String) : DimensionData(world, name) {

    // note: create companion/static instance list for external access

    override fun tick() {
        //testInt++
        //println(testInt)
        //this.markDirty()
    }

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