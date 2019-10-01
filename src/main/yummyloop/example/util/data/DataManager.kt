package yummyloop.example.util.data

import net.minecraft.server.world.ServerWorld
import net.minecraft.world.dimension.DimensionType
import yummyloop.example.mixin.server.world.ServerWorldMixin
import kotlin.reflect.KFunction2

object DataManager {
    private val uninitializedDimensionDataList = HashMap<String, KFunction2<ServerWorld, String, DimensionData>>()
    private val uninitializedLevelDataList = HashMap<String, KFunction2<ServerWorld, String, DimensionData>>()
    private val dimensionDataList = HashSet<DimensionData>()
    private val levelDataList = HashSet<DimensionData>()
    private var iniGlobal = true

    init {
        //registerDimensionData("test91", ::TestData)
        //registerLevelData("tglobal1", ::TestData)
    }

    @JvmStatic
    fun iniDimension(world : ServerWorld) {
        //println(world.saveHandler.worldDir.absolutePath.toString())
        //println("Ini data for dimension" + world.dimension.toString())
        if (iniGlobal && world.dimension.type == DimensionType.OVERWORLD) levelDataIni(world)
        for (i in uninitializedDimensionDataList){
            val name = i.key + world.dimension.type.suffix
            this.dimensionDataList.add(world.persistentStateManager.getOrCreate({ i.value(world, name) }, name))
        }
    }

    @JvmStatic
    fun tick(world : ServerWorld){
        for (dim in dimensionDataList){
            if (dim.getWorld() == world){
                dim.tick()
            }
        }
        if (world.dimension.type == DimensionType.OVERWORLD){ // might be out of sync with multiple overworlds
            for (level in levelDataList){
                level.tick()
            }
        }
    }

    private fun levelDataIni(world: ServerWorld){
        iniGlobal=false
        for (i in uninitializedLevelDataList){
            val name = "../" + i.key + "_level"
            this.levelDataList.add(world.persistentStateManager.getOrCreate({ i.value(world, name) }, name))
        }
    }

    // The names must be the same or else it won't load the saved data (should be working automatically)
    fun registerDimensionData(name: String, data: KFunction2<ServerWorld, String, DimensionData>) {
        this.uninitializedDimensionDataList[name] = data
    }

    // Pseudo level data, but it is dependent of the overworld, by default minecraft has the same behaviour
    fun registerLevelData(name: String, data: KFunction2<ServerWorld, String, DimensionData>) {
        this.uninitializedLevelDataList[name] = data
    }


    //-----------------------------------------------------------------------------------------------------------------
    //placeholders to suppress mixin cast error
    @JvmStatic fun iniDimension(world : ServerWorldMixin) {}
    @JvmStatic fun tick(world : ServerWorldMixin) {}
}