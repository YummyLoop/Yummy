package yummyloop.example.util.data

import net.minecraft.server.world.ServerWorld
import yummyloop.example.mixin.server.world.ServerWorldMixin
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction2

object DimensionDataManager {
    private val uninitializedDimensionDataList = HashMap<String, KFunction2<ServerWorld, String, DimensionData>>()
    private val uninitializedLevelDataList = HashMap<String, KFunction1<String, LevelData>>()
    private val dimensionDataList = HashSet<DimensionData>()
    private val levelDataList = HashSet<LevelData>()
    private var iniGlobal = true

    init {
        //registerDimensionData("test99", ::TestData)
        //registerLevelData("tglobal", ::TestLevelData)
    }

    @JvmStatic
    fun iniDimension(world : ServerWorld) {
        //println(world.saveHandler.worldDir.absolutePath.toString())
        //println("Ini data for dimension" + world.dimension.toString())
        if (iniGlobal) levelDataIni(world) // assumes the first dim initialized is the overworld or equivalent
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

    private fun levelDataIni(world: ServerWorld){
        iniGlobal=false
        for (i in uninitializedLevelDataList){
            val name = "../" + i.key + "_level"
            this.levelDataList.add(world.persistentStateManager.getOrCreate({ i.value(name) }, name))
        }
    }

    // The names must be the same or else it won't load the saved data (should be working automatically)
    fun registerDimensionData(name: String, data: KFunction2<ServerWorld, String, DimensionData>) {
        this.uninitializedDimensionDataList[name] = data
    }

    // Pseudo level data, but its dependent of the overworld or equivalent dimension
    fun registerLevelData(name: String, data: KFunction1<String, LevelData>) {
        this.uninitializedLevelDataList[name] = data
    }


    //-----------------------------------------------------------------------------------------------------------------
    //placeholders to suppress mixin cast error
    @JvmStatic
    fun iniDimension(world : ServerWorldMixin) {}
    @JvmStatic
    fun tick(world : ServerWorldMixin) {}
}