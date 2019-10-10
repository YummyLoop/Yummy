package yummyloop.example.block.storage

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.ChestBlockEntity
import net.minecraft.block.entity.LootableContainerBlockEntity
import net.minecraft.container.Container
import net.minecraft.container.GenericContainer
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.DefaultedList
import net.minecraft.util.math.Direction

open class BBlockEntity (blockEntityType: BlockEntityType<*>) : LootableContainerBlockEntity(blockEntityType) {
    private var inventory: DefaultedList<ItemStack>? = null
    private var viewerCount: Int = 0

    init {
        this.inventory = DefaultedList.ofSize(27, ItemStack.EMPTY)
    }

    override fun toTag(compoundTag_1: CompoundTag): CompoundTag {
        super.toTag(compoundTag_1)
        //if (!this.serializeLootTable(compoundTag_1)) {
            Inventories.toTag(compoundTag_1, this.inventory!!)
        //}

        return compoundTag_1
    }

    override fun fromTag(compoundTag_1: CompoundTag) {
        super.fromTag(compoundTag_1)
        this.inventory = DefaultedList.ofSize(this.invSize, ItemStack.EMPTY)
        //if (!this.deserializeLootTable(compoundTag_1)) {
            Inventories.fromTag(compoundTag_1, this.inventory)
        //}

    }

    override fun getInvSize(): Int {
        return 27
    }

    override fun isInvEmpty(): Boolean {
        val var1 = this.inventory!!.iterator()

        var itemStack_1: ItemStack
        do {
            if (!var1.hasNext()) {
                return true
            }

            itemStack_1 = var1.next() as ItemStack
        } while (itemStack_1.isEmpty)

        return false
    }

    override fun getInvStack(int_1: Int): ItemStack {
        return this.inventory!![int_1]
    }

    override fun takeInvStack(int_1: Int, int_2: Int): ItemStack {
        return Inventories.splitStack(this.inventory, int_1, int_2)
    }

    override fun removeInvStack(int_1: Int): ItemStack {
        return Inventories.removeStack(this.inventory, int_1)
    }

    override fun setInvStack(int_1: Int, itemStack_1: ItemStack) {
        this.inventory!![int_1] = itemStack_1
        if (itemStack_1.count > this.invMaxStackAmount) {
            itemStack_1.count = this.invMaxStackAmount
        }

    }

    override fun clear() {
        this.inventory!!.clear()
    }

    override fun getInvStackList(): DefaultedList<ItemStack>? {
        return this.inventory
    }

    override fun setInvStackList(defaultedList_1: DefaultedList<ItemStack>) {
        this.inventory = defaultedList_1
    }

    override fun getContainerName(): Text {
        return TranslatableText("container.barrel", *arrayOfNulls(0))
    }

    override fun createContainer(int_1: Int, playerInventory_1: PlayerInventory): Container {
        return GenericContainer.createGeneric9x3(int_1, playerInventory_1, this)
    }

    override fun onInvOpen(playerEntity_1: PlayerEntity) {
        if (!playerEntity_1.isSpectator) {
            if (this.viewerCount < 0) {
                this.viewerCount = 0
            }

            ++this.viewerCount
            val blockState_1 = this.cachedState
            val boolean_1 = blockState_1.get(Barrel.OPEN) as Boolean
            if (!boolean_1) {
                this.playSound(blockState_1, SoundEvents.BLOCK_BARREL_OPEN)
                this.setOpen(blockState_1, true)
            }

            this.scheduleUpdate()
        }

    }

    private fun scheduleUpdate() {
        this.world!!.blockTickScheduler.schedule(this.getPos(), this.cachedState.block, 5)
    }

    fun tick() {
        val int_1 = this.pos.x
        val int_2 = this.pos.y
        val int_3 = this.pos.z
        this.viewerCount = ChestBlockEntity.countViewers(this.world!!, this, int_1, int_2, int_3)
        if (this.viewerCount > 0) {
            this.scheduleUpdate()
        } else {
            val blockState_1 = this.cachedState
            /*if (blockState_1.block !== Blocks.BARREL) {
                this.invalidate()
                return
            }*/

            val boolean_1 = blockState_1.get(Barrel.OPEN) as Boolean
            if (boolean_1) {
                this.playSound(blockState_1, SoundEvents.BLOCK_BARREL_CLOSE)
                this.setOpen(blockState_1, false)
            }
        }

    }

    override fun onInvClose(playerEntity_1: PlayerEntity) {
        if (!playerEntity_1.isSpectator) {
            --this.viewerCount
        }
    }

    private fun setOpen(blockState_1: BlockState, boolean_1: Boolean) {
        this.world!!.setBlockState(this.getPos(), blockState_1.with(Barrel.OPEN, boolean_1) as BlockState, 3)
    }

    private fun playSound(blockState_1: BlockState, soundEvent_1: SoundEvent) {
        val vec3i_1 = (blockState_1.get(Barrel.FACING) as Direction).vector
        val double_1 = this.pos.x.toDouble() + 0.5 + vec3i_1.x.toDouble() / 2.0
        val double_2 = this.pos.y.toDouble() + 0.5 + vec3i_1.y.toDouble() / 2.0
        val double_3 = this.pos.z.toDouble() + 0.5 + vec3i_1.z.toDouble() / 2.0
        this.world!!.playSound(null as PlayerEntity?, double_1, double_2, double_3, soundEvent_1, SoundCategory.BLOCKS, 0.5f, this.world!!.random.nextFloat() * 0.1f + 0.9f)
    }
}
