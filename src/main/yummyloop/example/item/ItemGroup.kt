package yummyloop.example.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import yummyloop.example.util.registry.RegistryManager
import java.util.function.Consumer
import net.minecraft.item.ItemGroup as VanillaItemGroup

/**
 * ItemGroup defaults to MISC <p>
 * <b>Note:</b> ItemGroup can be set as null for no ItemGroup
 */
class ItemGroup(name : String, itemToIcon: ItemConvertible) {
    companion object{
        private val defaultItemToIcon : Item = Items.APPLE
    }
    private var mcItemGroup : VanillaItemGroup? = null
    private lateinit var fabricItemGroup : FabricItemGroupBuilder

    init {
        register(name, itemToIcon)
    }
    constructor(name : String) : this(name, defaultItemToIcon)
    constructor(name : String, itemToIcon: ItemConvertible, itemStacks: Consumer<List<ItemStack>>): this(name, itemToIcon) {
        this.fabricItemGroup.appendItems(itemStacks);
    }

    private fun register (name : String, itemToIcon: ItemConvertible){
        this.fabricItemGroup = RegistryManager.register(this, name).icon{ItemStack(itemToIcon)}
    }

    fun getGroup() : VanillaItemGroup? {
        if (this.mcItemGroup == null) {
            this.mcItemGroup = this.fabricItemGroup.build()
        }
        return this.mcItemGroup
    }
}

/* Example
    // New Item
    Item a = new Item("tutorial", "fabric_item", ItemGroup.MISC);
    // New Custom ItemStack
    ItemStack b = new ItemStack(a);
    b.getOrCreateTag().putInt("CustomModelData", 1);
    b.setCustomName(new LiteralText("Hello"));

    // New manual/custom Fabric ItemGroup
    ItemGroup helloGroup = new ItemGroup("tutorial", "hello", Items.APPLE, stacks ->
    {
        stacks.add(new ItemStack(Blocks.BONE_BLOCK));
        stacks.add(new ItemStack(Items.APPLE));
        stacks.add(ItemStack.EMPTY);
        stacks.add(new ItemStack(Items.IRON_SHOVEL));
        stacks.add(b);
    });
*/