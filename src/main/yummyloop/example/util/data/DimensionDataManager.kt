package yummyloop.example.util.data

import net.minecraft.server.world.ServerWorld
import yummyloop.example.mixin.server.world.ServerWorldMixin
import kotlin.reflect.KFunction2

object DimensionDataManager {
    private val uninitializedDimensionDataList = HashMap<String, KFunction2<ServerWorld, String, DimensionData>>()
    private val dimensionDataList = HashSet<DimensionData>()

    init {
        //registerDimensionData("test99", ::TestData)
    }

    @JvmStatic
    fun iniDimension(world : ServerWorld) {
        //println("Ini data for dimension" + world.dimension.toString())
        for (i in uninitializedDimensionDataList){
            val name = i.key + world.dimension.type.suffix
            this.dimensionDataList.add(world.persistentStateManager.getOrCreate({ i.value(world, name) }, name))
        }
    }

    @JvmStatic
    fun tick(world : ServerWorld){
        for (i in dimensionDataList){
            if (i.getWorld() == world){
                i.tick()
            }
        }
    }

    // The names must be the same or else it won't load the saved data (should be working automatically)
    fun registerDimensionData(name: String, data: KFunction2<ServerWorld, String, DimensionData>) {
        this.uninitializedDimensionDataList[name] = data
    }


    //-----------------------------------------------------------------------------------------------------------------
    //placeholders to suppress mixin cast error
    @JvmStatic
    fun iniDimension(world : ServerWorldMixin) {}
    @JvmStatic
    fun tick(world : ServerWorldMixin) {}
}