package yummyloop.example.item

import net.minecraft.item.Items as Vanilla
class Items {//extends net.minecraft.item.Items {
    //public int test = net.minecraft.item.Items;
   // Class a = new Class net.minecraft.item.Items

   //private static class vanilla extends net.minecraft.item.Items{

   //}
   companion object {
       @JvmField val groupA = ItemGroup("tutorial", "hello1", Vanilla.ANVIL)
       @JvmField val F = Item("tutorial", "fabric_item1", groupA)
   }


}