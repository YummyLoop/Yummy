package yummyloop.yummy.integration.modmenu

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import me.shedaniel.autoconfig.AutoConfig
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import yummyloop.yummy.config.ModConfig

@Environment(EnvType.CLIENT)
internal class ModMenuIntegration : ModMenuApi {
    override fun getModConfigScreenFactory() = ConfigScreenFactory {
        AutoConfig.getConfigScreen(ModConfig::class.java, it).get()
    }
}
