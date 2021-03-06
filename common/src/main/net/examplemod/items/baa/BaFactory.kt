package net.examplemod.items.baa

import me.shedaniel.architectury.registry.menu.ExtendedMenuProvider
import net.examplemod.network.packets.packetBuffer
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText

class BaFactory(var stack: ItemStack, var isOffHand: Boolean) : ExtendedMenuProvider {
    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity?): ScreenHandler? {
        return BaHandler(syncId, inv, packetBuffer(stack, isOffHand), stack)
    }

    /**
     * Returns the title of this screen handler; will be a part of the open
     * screen packet sent to the client.
     */
    override fun getDisplayName(): Text {
        return TranslatableText("backpack")
    }

    override fun saveExtraData(buf: PacketByteBuf) {
        buf.writeItemStack(stack)
        buf.writeBoolean(isOffHand)
    }
}