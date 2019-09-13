package yummyloop.example.item.models.builtIn

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.model.Box
import net.minecraft.client.model.Cuboid
import net.minecraft.client.model.Model as VanillaModel

@Environment(EnvType.CLIENT)
class Model1() : VanillaModel(){
    private var export = ModelRenderer(this)

    init {
        this.textureWidth = 64
        this.textureHeight = 64

        export.setRotationPoint(0.0711f, 22.6f, -0.2756f)
        export.cubeList.add(ModelBox(export, 13, 18, -48.3481f, -14.8612f, 50.5588f, 34, 30, 21, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, -13.7451f, -14.8612f, 54.8842f, 60, 30, 17, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, 55.4609f, -14.8612f, -48.9248f, 17, 30, 95, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, -61.3242f, -14.8612f, -66.2263f, 25, 30, 21, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, -35.372f, -14.8612f, -74.8771f, 8, 30, 25, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, -69.975f, -14.8612f, 28.932f, 21, 30, 25, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, 42.4848f, -14.8612f, 37.5827f, 12, 30, 17, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, 46.8102f, -14.8612f, 54.8842f, 12, 30, 8, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, 59.7863f, -14.8612f, 46.2335f, 4, 30, 12, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, -48.3481f, -14.8612f, 41.9081f, 12, 30, 8, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, 33.8341f, -14.8612f, -70.5517f, 21, 30, 25, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, 46.8102f, -14.8612f, 28.932f, 8, 30, 8, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, -26.7212f, -14.8612f, -74.8771f, 73, 30, 4, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, -48.3481f, -14.8612f, 37.5827f, 4, 30, 4, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, -48.3481f, -14.8612f, -74.8771f, 12, 30, 8, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, 12.2072f, -14.8612f, -70.5517f, 21, 30, 17, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, -26.7212f, -14.8612f, -70.5517f, 38, 30, 12, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, 33.8341f, -14.8612f, 41.9081f, 8, 30, 12, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, -56.9988f, -14.8612f, -70.5517f, 8, 30, 4, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, 55.4609f, -14.8612f, -61.9009f, 8, 30, 12, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, 55.4609f, -14.8612f, -66.2263f, 4, 30, 4, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, -65.6496f, -14.8612f, -61.9009f, 4, 30, 90, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, 64.1117f, -14.8612f, -57.5756f, 4, 30, 8, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, -61.3242f, -14.8612f, 54.8842f, 12, 30, 8, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, -26.7212f, -14.8612f, -57.5756f, 12, 30, 4, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, -69.975f, -14.8612f, -57.5756f, 4, 30, 86, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, 25.1833f, -14.8612f, -53.2502f, 8, 30, 4, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, 12.2072f, -14.8612f, 50.5588f, 21, 30, 4, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, -74.3003f, -14.8612f, -48.9248f, 4, 30, 95, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, -56.9988f, -14.8612f, 63.535f, 8, 30, 4, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, -61.3242f, -14.8612f, -44.5994f, 8, 30, 25, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, 46.8102f, -14.8612f, -44.5994f, 8, 30, 12, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, 25.1833f, -14.8612f, 46.2335f, 8, 30, 4, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, -35.372f, -14.8612f, 46.2335f, 8, 30, 4, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, -61.3242f, -14.8612f, -18.6472f, 4, 30, 47, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, 42.4848f, -14.8612f, -44.5994f, 4, 30, 4, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, -52.6735f, -14.8612f, -44.5994f, 8, 30, 4, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, -52.6735f, -14.8612f, -40.2741f, 4, 30, 8, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, 51.1356f, -14.8612f, -31.6233f, 4, 30, 12, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, 64.1117f, -14.8612f, 46.2335f, 4, 30, 8, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, 55.4609f, -14.8612f, 46.2335f, 4, 30, 8, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, -65.6496f, -14.8612f, 54.8842f, 4, 30, 4, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, -56.9988f, -14.8612f, 15.9558f, 4, 30, 12, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, 46.8102f, -14.8612f, 63.535f, 8, 30, 4, 0.0f, true))
        export.cubeList.add(ModelBox(export, 13, 18, 51.1356f, -14.8612f, 15.9558f, 4, 30, 12, 0.0f, true))
    }

    fun renderItem() {
        export.render(0.005f)
    }

    /* Stuff to adapt to BlockBench entities*/
    private class ModelBox(cuboid : Cuboid, textureOffsetU : Int, textureOffsetV : Int,
                           xMin : Float, yMin : Float, zMin : Float,
                           xMax : Int, yMax : Int, zMan : Int,
                           scale : Float, mirror : Boolean) :
            Box(cuboid, textureOffsetU, textureOffsetV,
                    xMin, yMin, zMin,
                    xMax, yMax, zMan,
                    scale, mirror)


    class ModelRenderer(model : VanillaModel) : Cuboid(model){
        val cubeList: MutableList<Box> = this.boxes
    }
}

/* Example of how to read resources
var fileText = ""
var line: String? = ""
val input = javaClass.getResourceAsStream("/assets/example/models/item/portal.json")
//val input = javaClass.getResourceAsStream("/Item.kt")
if (input == null) {
    println("File not found")
}else {
    val reader = BufferedReader(InputStreamReader(input))
    while (line != null){
        fileText += line
        line = reader.readLine()
    }
    reader.close()
    println(fileText)
}
* */