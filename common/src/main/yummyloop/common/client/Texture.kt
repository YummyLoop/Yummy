package yummyloop.common.client

import net.minecraft.util.Identifier

data class Texture(
    val namespace: String,
    val path: String,
    val xSize: Int = 256,
    val ySize: Int = 256,
) {
    constructor(namespace: String, path: String, size: Int) : this(namespace, path, size, size)

    fun get() = Identifier(namespace, path)
}