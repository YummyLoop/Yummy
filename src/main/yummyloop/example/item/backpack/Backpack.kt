package yummyloop.example.item.backpack

import net.minecraft.block.BlockState
import net.minecraft.client.network.ClientDummyContainerProvider
import net.minecraft.container.GenericContainer
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.nbt.CompoundTag
import net.minecraft.text.TranslatableText
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import yummyloop.example.item.Item
import yummyloop.example.item.ItemGroup
import net.minecraft.item.ItemGroup as VanillaItemGroup

class Backpack(modId: String, name: String, settings : Settings) : Item(modId, name, settings){
    constructor(modId : String, itemName : String) :
            this(modId, itemName, Settings().group(VanillaItemGroup.MISC))
    constructor(modId : String, itemName : String, group : ItemGroup) :
            this(modId, itemName, Settings().group(group.getGroup()))

    override fun use(world: World?, player: PlayerEntity?, hand: Hand?): TypedActionResult<ItemStack?> {
        if (world == null || player == null || hand == null) return TypedActionResult(ActionResult.PASS, player?.getStackInHand(hand))
        player.setCurrentHand(hand)
        val itemStack = player.activeItem

        return if (itemStack.count <= 1) {
            //val newItemStack = itemStack.copy()
            //itemStack=ItemStack.EMPTY
            val inventory = DefaultedList.ofSize(54, ItemStack.EMPTY);
            val compoundTag = itemStack.getSubTag("Items")
            if (compoundTag != null){
                Inventories.fromTag(compoundTag, inventory)
            }

            //var chestInventory = player.enderChestInventory
            val chestInventory = Inv(itemStack, inventory)

            val provider = ClientDummyContainerProvider(
                { int_1, playerInventory, playerEntity ->
                    GenericContainer.createGeneric9x6(int_1, playerInventory, chestInventory)
                }, itemStack.name)
            player.openContainer(provider)

            TypedActionResult(ActionResult.SUCCESS, itemStack)
        } else {
            TypedActionResult(ActionResult.FAIL, itemStack)
        }
    }

    override fun useOnBlock(itemUsageContext_1: ItemUsageContext?): ActionResult {
        return ActionResult.PASS
    }

    override fun useOnEntity(itemStack_1: ItemStack?, playerEntity_1: PlayerEntity?, livingEntity_1: LivingEntity?, hand_1: Hand?): Boolean {
        return false
    }

    override fun getUseAction(itemStack_1: ItemStack?): UseAction {
        return UseAction.BLOCK
    }

    override fun getMaxUseTime(itemStack_1: ItemStack?): Int {
        return 5
    }

    override fun onStoppedUsing(itemStack_1: ItemStack?, world_1: World?, livingEntity_1: LivingEntity?, int_1: Int) {
    }

    override fun finishUsing(itemStack_1: ItemStack?, world_1: World?, livingEntity_1: LivingEntity?): ItemStack? {
        //println("is the container closed?")
        return itemStack_1//ItemStack(Items.IRON_BARS)
    }

    override fun isUsedOnRelease(itemStack_1: ItemStack?): Boolean {
        return false
    }

    // End of usage settings

    override fun canMine(blockState_1: BlockState?, world_1: World?, blockPos_1: BlockPos?, playerEntity_1: PlayerEntity?): Boolean {
        return true // gets stuck if false
    }
    // Experimental
    fun serializeInventory(compoundTag_1: CompoundTag, inventory : DefaultedList<ItemStack>): CompoundTag {
        Inventories.toTag(compoundTag_1, inventory, false)
        //println(compoundTag_1.toString())
        return compoundTag_1
    }

    fun deserializeInventory(compoundTag_1: CompoundTag, inventory : DefaultedList<ItemStack>) {
        if (compoundTag_1.containsKey("Items", 9)) {
            Inventories.fromTag(compoundTag_1, inventory)
        }
    }


}