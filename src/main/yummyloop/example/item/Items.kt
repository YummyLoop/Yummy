package yummyloop.example.item

import net.minecraft.item.Item as VanillaItem

object Items : HashMap<String, VanillaItem>() {
    fun ini() {

    }
}

/* Reference
    // New Custom ItemStack
    ItemStack b = new ItemStack(a);
    b.getOrCreateTag().putInt("CustomModelData", 1);
    b.setCustomName(new LiteralText("Hello"));
*/