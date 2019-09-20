package yummyloop.example.render.models

import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.ModelBakeSettings
import net.minecraft.client.render.model.ModelLoader
import net.minecraft.client.render.model.json.JsonUnbakedModel
import net.minecraft.client.render.model.json.ModelItemOverride
import net.minecraft.client.render.model.json.ModelItemPropertyOverrideList
import net.minecraft.client.texture.Sprite
import net.minecraft.util.Identifier
import yummyloop.example.ExampleMod
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*
import java.util.function.Function
import net.minecraft.client.render.model.UnbakedModel as VanillaUnbakedModel

class SemiUnbakedModel(private val name : String) : VanillaUnbakedModel {
    override fun bake(modelLoader: ModelLoader?, idSprite: Function<Identifier, Sprite>?, settings: ModelBakeSettings?): BakedModel? {
        val model = getJsonModel()
        return SemiBakedModel(name, model.transformations, compileOverrides(modelLoader, model, model.overrides))
    }

    override fun getModelDependencies(): MutableCollection<Identifier> {
        return Collections.emptyList();
    }

    override fun getTextureDependencies(var1: Function<Identifier, VanillaUnbakedModel>?, var2: MutableSet<String>?): MutableCollection<Identifier> {
        return Collections.emptyList();
    }

    private fun getJsonModel(): JsonUnbakedModel {
        val id = Identifier(ExampleMod.id,"models/item/$name.json")
        val inputStreamReader = InputStreamReader(MinecraftClient.getInstance().resourceManager.getResource(id).inputStream, Charsets.UTF_8) //rawData to char
        val reader = BufferedReader(inputStreamReader) //char to text
        val ret = JsonUnbakedModel.deserialize(reader)//json text to data
        reader.close()
        inputStreamReader.close()
        return ret
    }

    private fun compileOverrides(modelLoader: ModelLoader?, jsonUnbakedModel: JsonUnbakedModel, overrides: List<ModelItemOverride>): ModelItemPropertyOverrideList {
        return if (overrides.isEmpty()) ModelItemPropertyOverrideList.EMPTY else ModelItemPropertyOverrideList(modelLoader, jsonUnbakedModel, Function { modelLoader?.getOrLoadModel(it) }, overrides)
    }
}