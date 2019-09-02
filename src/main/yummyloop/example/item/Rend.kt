package yummyloop.example.item

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.item.ItemStack

interface Rend {
    @Environment(EnvType.CLIENT)
    fun render(stack: ItemStack)
    fun enableRender() {
        ItemDynamicRenderer.list.add(this)
    }
}