package yummyloop.example.item.models

import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.ModelBakeSettings
import net.minecraft.client.render.model.ModelLoader
import net.minecraft.client.texture.Sprite
import net.minecraft.util.Identifier
import java.util.*
import java.util.function.Function
import net.minecraft.client.render.model.UnbakedModel as VanillaUnbakedModel

class UnbakedModel(private val bakedModel : BakedModel) : VanillaUnbakedModel {
    override fun bake(var1: ModelLoader?, var2: Function<Identifier, Sprite>?, var3: ModelBakeSettings?): BakedModel? {
        return bakedModel
    }

    override fun getModelDependencies(): MutableCollection<Identifier> {
        return Collections.emptyList();
    }

    override fun getTextureDependencies(var1: Function<Identifier, VanillaUnbakedModel>?, var2: MutableSet<String>?): MutableCollection<Identifier> {
        return Collections.emptyList();
    }
}