package yummyloop.test.gui

import me.shedaniel.architectury.registry.RegistrySupplier
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.CraftingScreenHandler
import net.minecraft.screen.ScreenHandlerType
import yummyloop.yummy.LOG


class ScreenHandler1(id: Int, playerInventory: PlayerInventory) : CraftingScreenHandler(id, playerInventory) {
    companion object {
        var rType: RegistrySupplier<ScreenHandlerType<out ScreenHandler1>>? = null
    }

    init {
        LOG.info(this.javaClass.toGenericString())
        slots.clear()
    }

    override fun getType(): ScreenHandlerType<*> {
        return rType!!.get()!!
    }

}

