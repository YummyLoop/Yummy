package net.examplemod.test.gui

import me.shedaniel.architectury.registry.RegistrySupplier
import net.examplemod.LOG
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.CraftingScreenHandler
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.Slot
import net.minecraft.text.Text


class ScreenHandler1(id : Int, playerInventory: PlayerInventory) : CraftingScreenHandler(id, playerInventory) {
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

