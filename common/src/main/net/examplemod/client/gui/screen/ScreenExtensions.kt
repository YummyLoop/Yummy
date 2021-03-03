package net.examplemod.client.gui.screen

import me.shedaniel.architectury.hooks.ScreenHooks
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.AbstractButtonWidget


@Environment(EnvType.CLIENT)
fun Screen.getWidgets(): List<AbstractButtonWidget?>? {
    return ScreenHooks.getButtons(this)
}

@Environment(EnvType.CLIENT)
fun Screen.getButtons(): List<AbstractButtonWidget?>? {
    return getWidgets()
}

@Environment(EnvType.CLIENT)
fun <T : AbstractButtonWidget> Screen.addWidget(buttonWidget: T): T {
    return ScreenHooks.addButton(this, buttonWidget)
}

@Environment(EnvType.CLIENT)
fun <T : AbstractButtonWidget> Screen.addButton(buttonWidget: T): T {
    return addWidget(buttonWidget)
}

@Environment(EnvType.CLIENT)
fun <T : Element?> Screen.addElement(guiElement: T): T {
    return ScreenHooks.addChild(this, guiElement)
}

@Environment(EnvType.CLIENT)
fun <T : Element?> Screen.addChild(guiElement: T): T {
    return addElement(guiElement)
}




