package yummyloop.common.registry

import me.shedaniel.architectury.event.events.TextureStitchEvent
import me.shedaniel.architectury.platform.Platform
import me.shedaniel.architectury.registry.MenuRegistry
import me.shedaniel.architectury.registry.RegistrySupplier
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import yummyloop.common.client.Texture
import java.util.function.Consumer
import java.util.function.Supplier

/**
 * The register for client side
 *
 * @param modId Id of the mod
 */
class ClientRegisters(private val modId: String) {
    private val isClient: Boolean by lazy { Platform.getEnv() == EnvType.CLIENT }
    private val lateCallList: MutableList<Supplier<Any>> by lazy { mutableListOf() }

    /**
     * Initializes all the client entries
     */
    @Environment(EnvType.CLIENT)
    fun register() {
        lateCallList.forEach { it.get() }
        lateCallList.clear()
    }

    /**
     * Adds entries for later initialization in the Client
     * when not in the client side it does nothing
     *
     * @param s Entry supplier
     */
    operator fun invoke(s: Supplier<Any>) {
        if (isClient) lateCallList.add(s)
    }

    /**
     * Registers a screen, can only be called from the client side
     *
     * @param handlerType the registered type of the screen Handler
     * @param factory The screen factory
     * @see HandledScreen
     * @see screen
     */
    @Environment(EnvType.CLIENT)
    fun <H, S> clientScreen(
        handlerType: RegistrySupplier<ScreenHandlerType<out H>>,
        factory: MenuRegistry.ScreenFactory<H, S>,
    ) where H : ScreenHandler, S : Screen, S : ScreenHandlerProvider<H> {
        MenuRegistry.registerScreenFactory(handlerType.get(), factory)
    }

    /**
     * Registers a screen
     *
     * @param handlerType the registered type of the screen Handler
     * @param factory The screen factory supplier
     * @see HandledScreen
     * @see MenuRegistry.ScreenFactory
     */
    fun <H, S> screen(
        handlerType: RegistrySupplier<ScreenHandlerType<out H>>,
        factory: Supplier<(H, PlayerInventory, Text) -> S>,
    ) where H : ScreenHandler, S : Screen, S : ScreenHandlerProvider<H> {
        this { MenuRegistry.registerScreenFactory(handlerType.get(), factory.get()) }
    }

    /**
     * Registers a texture,
     * the location is of the format: "textures/$path.png"
     *
     * @param path The path to the texture
     * @param xSize texture x axis size
     * @param ySize texture y axis size
     * @return returns a Texture data class
     * @see Texture
     */
    fun texture(path: String, xSize: Int = 256, ySize: Int = 256): Texture {
        this {
            TextureStitchEvent.PRE.register { spriteAtlasTexture: SpriteAtlasTexture, consumer: Consumer<Identifier> ->
                consumer.accept(Identifier(modId, path))
            }
        }
        return Texture(modId, "textures/$path.png", xSize, ySize)
    }
}