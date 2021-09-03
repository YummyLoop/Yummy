package yummyloop.common.client.screen

import me.shedaniel.architectury.hooks.ScreenHooks
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ClickableWidget


@Environment(EnvType.CLIENT)
fun Screen.getWidgets(): List<ClickableWidget?>? {
    return ScreenHooks.getButtons(this)
}

@Environment(EnvType.CLIENT)
fun Screen.getButtons(): List<ClickableWidget?>? {
    return getWidgets()
}

@Environment(EnvType.CLIENT)
fun <T : ClickableWidget> Screen.addWidget(buttonWidget: T): T {
    return ScreenHooks.addButton(this, buttonWidget)
}

@Environment(EnvType.CLIENT)
fun <T : ClickableWidget> Screen.addButton(buttonWidget: T): T {
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




