package net.examplemod.mixin.plugin

import me.shedaniel.architectury.platform.Platform
import net.examplemod.ExampleMod
import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo

class YummyMixinPlugin : IMixinConfigPlugin {
    companion object {
        private const val mixinPath = "net.examplemod.mixin"
    }

    /**
     * Called after the plugin is instantiated, do any setup here.
     *
     * @param mixinPackage The mixin root package from the config
     */
    override fun onLoad(mixinPackage: String?) {
        // ...
    }

    /**
     * Called only if the "referenceMap" key in the config is **not** set.
     * This allows the refmap file name to be supplied by the plugin
     * programmatically if desired. Returning `null` will revert to
     * the default behaviour of using the default refmap json file.
     *
     * @return Path to the refmap resource or null to revert to the default
     */
    override fun getRefMapperConfig(): String? {
        return null
    }

    /**
     * Called during mixin initialisation, allows this plugin to control whether
     * a specific will be applied to the specified target. Returning false will
     * remove the target from the mixin's target set, and if all targets are
     * removed then the mixin will not be applied at all.
     *
     * @param targetClassName Fully qualified class name of the target class
     * @param mixinClassName Fully qualified class name of the mixin
     * @return True to allow the mixin to be applied, or false to remove it from
     * target's mixin set
     */
    override fun shouldApplyMixin(targetClassName: String?, mixinClassName: String?): Boolean {
        fun containsMixinClassName(vararg mixin: String): Boolean = mixin.contains(mixinClassName)

        if (!ExampleMod.modConfig.dev
            && !Platform.isDevelopmentEnvironment()
            && containsMixinClassName(
                "$mixinPath.client.ExampleMixin"
            )
        ) {
            return false
        }
        if (!ExampleMod.modConfig.mixinSideScreen
            && containsMixinClassName(
                "$mixinPath.client.InventoryScreenMixin",
                "$mixinPath.common.PlayerScreenHandlerMixin",
                "$mixinPath.mixin.common.SlotMixin"
            )
        ) {
            return false
        }
        return true
    }

    /**
     * Called after all configurations are initialised, this allows this plugin
     * to observe classes targeted by other mixin configs and optionally remove
     * targets from its own set. The set myTargets is a direct view of the
     * targets collection in this companion config and keys may be removed from
     * this set to suppress mixins in this config which target the specified
     * class. Adding keys to the set will have no effect.
     *
     * @param myTargets Target class set from the companion config
     * @param otherTargets Target class set incorporating targets from all other
     * configs, read-only
     */
    override fun acceptTargets(myTargets: MutableSet<String>?, otherTargets: MutableSet<String>?) {
        // ...
    }

    /**
     * After mixins specified in the configuration have been processed, this
     * method is called to allow the plugin to add any additional mixins to
     * load. It should return a list of mixin class names or return null if the
     * plugin does not wish to append any mixins of its own.
     *
     * @return additional mixins to apply
     */
    override fun getMixins(): MutableList<String>? {
        return null
    }

    /**
     * Called immediately **before** a mixin is applied to a target class,
     * allows any pre-application transformations to be applied.
     *
     * @param targetClassName Transformed name of the target class
     * @param targetClass Target class tree
     * @param mixinClassName Name of the mixin class
     * @param mixinInfo Information about this mixin
     */
    override fun preApply(
        targetClassName: String?,
        targetClass: ClassNode?,
        mixinClassName: String?,
        mixinInfo: IMixinInfo?,
    ) {
        // ...
    }

    /**
     * Called immediately **after** a mixin is applied to a target class,
     * allows any post-application transformations to be applied.
     *
     * @param targetClassName Transformed name of the target class
     * @param targetClass Target class tree
     * @param mixinClassName Name of the mixin class
     * @param mixinInfo Information about this mixin
     */
    override fun postApply(
        targetClassName: String?,
        targetClass: ClassNode?,
        mixinClassName: String?,
        mixinInfo: IMixinInfo?,
    ) {
        // ...
    }
}