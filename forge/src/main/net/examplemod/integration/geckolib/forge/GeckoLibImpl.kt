package net.examplemod.integration.geckolib.forge

import software.bernie.geckolib3.GeckoLib

object GeckoLibImpl {
    @JvmStatic
    fun initialize(): Unit = GeckoLib.initialize()
}