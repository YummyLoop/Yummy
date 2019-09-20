package yummyloop.example.item.armor

import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.util.ModelIdentifier

interface ArmorWithBody {
    val body : ModelIdentifier?
    //val bodyTransform : ModelTransformation?
}