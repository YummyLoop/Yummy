package yummyloop.yummy.items.baa

import me.shedaniel.architectury.registry.MenuRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import yummyloop.yummy.items.Ytem
import java.util.*

class Ba(settings: Settings = Settings()) : Ytem(settings.maxCount(1)) {

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val itemStack = user.getStackInHand(hand)
        if (world.isClient) return TypedActionResult.success(itemStack, false)

        user.itemCooldownManager[this] = 5

        if (!itemStack.orCreateTag.containsUuid("uuid")) {
            itemStack.orCreateTag.putUuid("uuid", UUID.randomUUID())
        }

        if (user is ServerPlayerEntity) {
            MenuRegistry.openExtendedMenu(
                user,
                BaFactory(itemStack, hand == Hand.OFF_HAND || user.getStackInHand(Hand.MAIN_HAND) == ItemStack.EMPTY)
            )
        }

        return TypedActionResult.consume(itemStack)
    }
}