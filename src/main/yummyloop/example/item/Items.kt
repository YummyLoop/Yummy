package yummyloop.example.item

import net.fabricmc.fabric.api.client.render.ColorProviderRegistry
import net.minecraft.block.Material
import net.minecraft.client.color.item.ItemColorProvider
import net.minecraft.container.Container
import net.minecraft.item.DyeableItem
import net.minecraft.item.ItemConvertible
import yummyloop.example.ExampleMod
import yummyloop.example.block.Block
import yummyloop.example.block.entity.TemplateBlockEntity
import yummyloop.example.block.entity.TemplateBlockWithEntity
import yummyloop.example.item.backpack.BContainer
import yummyloop.example.item.backpack.Backpack
import net.minecraft.item.Items as Vanilla
import net.minecraft.item.Item as VanillaItem

object Items {
    @JvmField val itemList = mutableMapOf<String, ItemConvertible>()
    @JvmField val groupList = mutableMapOf<String, ItemGroup>()
    @JvmField val containerList = mutableMapOf<String, Any>()
    @JvmField val blockEntityList = mutableMapOf<String, Any>()

   @JvmField val blockA = TemplateBlockWithEntity(ExampleMod.id, "example_blockrender", Block.of(Material.METAL).lightLevel(10))

    init {
        groupList["groupA"] = ItemGroup(ExampleMod.id, "hello1", Vanilla.ANVIL)

        itemList["itemX"] = Item(ExampleMod.id, "fabric_item", groupList["groupA"]!!)
        itemList["backpack"] = Backpack(ExampleMod.id, "ring", 6, groupList["groupA"]!!)
        itemList["backpack2"] = Backpack(ExampleMod.id, "ring2", 5, groupList["groupA"]!!)
        itemList["blockItemA"] = BlockItem(ExampleMod.id, "example_blockrender", blockA)

        (itemList["itemX"] as Item).addTooltip("item.example.fabric_item.tooltip")


        containerList["backpack"] = BContainer
        blockEntityList["backpack"] = TemplateBlockEntity
    }

    private fun registerFlatDyableItem (items : List<ItemConvertible>){
        ColorProviderRegistry.ITEM.register(// Only works for "parent": "item/generated" / that is flat textures ???
                ItemColorProvider { itemStack, layer ->
                    if(layer != 0){
                        -1
                    }else{
                        (itemStack.item as DyeableItem).getColor(itemStack)
                    }
                },
                *items.toTypedArray()
        )
    }

}