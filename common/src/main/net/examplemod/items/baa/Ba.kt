package net.examplemod.items.baa

import net.examplemod.LOG
import net.examplemod.items.Ytem
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class Ba(settings: Settings = Settings().maxCount(1)) : Ytem(settings) {

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (world.isClient) return TypedActionResult.pass(user.getStackInHand(hand))
        val itemStack = user.getStackInHand(hand)
        user.itemCooldownManager[this] = 5

        if (user is ServerPlayerEntity) {
            LOG.info("at the item:" + itemStack.item.toString())
            user.openHandledScreen(BaFactory(itemStack))
        }

        LOG.info("Stopped using item!")
        return TypedActionResult.success(itemStack)
        //return TypedActionResult.fail(itemStack)
    }
}