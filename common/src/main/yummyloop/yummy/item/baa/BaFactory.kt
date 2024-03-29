package yummyloop.yummy.item.baa

import dev.architectury.registry.menu.ExtendedMenuProvider
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import yummyloop.common.network.packets.PacketBuffer
import yummyloop.common.network.packets.add

class BaFactory(var stack: ItemStack, var isOffHand: Boolean) : ExtendedMenuProvider {
    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity?): ScreenHandler? {
        return BaHandler(syncId, inv, PacketBuffer(stack, isOffHand), stack, isOffHand)
    }

    /**
     * Returns the title of this screen handler; will be a part of the open
     * screen packet sent to the client.
     */
    override fun getDisplayName(): Text {
        return when {
            stack.hasCustomName() -> {
                stack.name
            }
            else -> {
                TranslatableText("backpack")
            }
        }
    }

    override fun saveExtraData(buf: PacketByteBuf) {
        buf.add(stack, isOffHand)
    }
}