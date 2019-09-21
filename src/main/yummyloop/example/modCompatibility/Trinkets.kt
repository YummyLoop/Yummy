package yummyloop.example.modCompatibility

import dev.emi.trinkets.api.TrinketSlots
import net.minecraft.util.Identifier

object Trinkets {
    init {
        // Default/Standard slots
        addSubSlot("head",      "mask",     "textures/item/empty_trinket_slot_mask.png")
        addSubSlot("chest",     "backpack", "textures/item/empty_trinket_slot_backpack.png")
        addSubSlot("chest",     "necklace", "textures/item/empty_trinket_slot_necklace.png")
        addSubSlot("legs",      "belt",     "textures/item/empty_trinket_slot_belt.png")
        addSubSlot("feet",      "aglet",    "textures/item/empty_trinket_slot_aglet.png")
        addSubSlot("hand",      "gloves",   "textures/item/empty_trinket_slot_gloves.png")
        addSubSlot("hand",      "ring",     "textures/item/empty_trinket_slot_ring.png")
        addSubSlot("offhand",   "ring",     "textures/item/empty_trinket_slot_ring.png")
    }
    private fun addSubSlot(group : String, slot: String, texture : String){
        TrinketSlots.addSubSlot(group, slot, Identifier("trinkets", texture))
    }
}