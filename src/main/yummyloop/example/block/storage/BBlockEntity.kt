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
import yummyloop.example.util.data.DataManager
import yummyloop.example.util.data.LevelChestData

open class BBlockEntity (blockEntityType: BlockEntityType<*>) : LootableContainerBlockEntity(blockEntityType) {
    private var inventory: DefaultedList<ItemStack>? = null
    private var viewerCount: Int = 0
    private var data = DataManager.levelDataList["levelChestData"] as LevelChestData
    private var id : Long = 0

    init {
        this.inventory = data[this.id]
    }

    override fun toTag(compoundTag: CompoundTag): CompoundTag {
        super.toTag(compoundTag)
        compoundTag.putLong("storage_id", id)
        data[this.id]=this.inventory

        return compoundTag
    }

    override fun fromTag(compoundTag: CompoundTag) {
        super.fromTag(compoundTag)
        this.id = compoundTag.getLong("storage_id")
        this.inventory=data[this.id]
    }

    fun newId() = this.setId(data.nextId())

    fun setId(id : Long) {
        this.id = id
        this.inventory=data[this.id]
    }
    fun id() = this.id

    override fun getInvSize(): Int = 27

    override fun isInvEmpty(): Boolean {
        return (this.inventory!! == DefaultedList.ofSize(invSize, ItemStack.EMPTY))
    }

    override fun getInvStack(slot: Int): ItemStack = this.inventory!![slot]

    override fun takeInvStack(slot: Int, count: Int): ItemStack = Inventories.splitStack(this.inventory, slot, count)

    override fun removeInvStack(slot: Int): ItemStack = Inventories.removeStack(this.inventory, slot)

    override fun setInvStack(slot: Int, itemStack: ItemStack) {
        this.inventory!![slot] = itemStack
        if (itemStack.count > this.invMaxStackAmount) {
            itemStack.count = this.invMaxStackAmount
        }
    }

    override fun clear() = this.inventory!!.clear()

    override fun getInvStackList(): DefaultedList<ItemStack>? = this.inventory

    override fun setInvStackList(defaultedList: DefaultedList<ItemStack>) {
        this.inventory = defaultedList
    }

    override fun getContainerName(): Text = TranslatableText("container.barrel", null)

    override fun createContainer(syncId: Int, playerInventory: PlayerInventory): Container {
        return GenericContainer.createGeneric9x3(syncId, playerInventory, this)
    }

    override fun onInvOpen(playerEntity: PlayerEntity) {
        if (!playerEntity.isSpectator) {
            if (this.viewerCount < 0) {
                this.viewerCount = 0
            }

            println("chest id:"+this.id)
            ++this.viewerCount
            val cachedBlockState = this.cachedState
            val isOpen = cachedBlockState.get(Barrel.OPEN) as Boolean
            if (!isOpen) {
                this.playSound(cachedBlockState, SoundEvents.BLOCK_BARREL_OPEN)
                this.setOpen(cachedBlockState, true)
            }

            this.scheduleUpdate()
        }

    }

    private fun scheduleUpdate() {
        this.world!!.blockTickScheduler.schedule(this.getPos(), this.cachedState.block, 5)
    }

    fun tick() {
        val x = this.pos.x
        val y = this.pos.y
        val z = this.pos.z
        this.viewerCount = ChestBlockEntity.countViewers(this.world!!, this, x, y, z)
        if (this.viewerCount > 0) {
            this.scheduleUpdate()
        } else {
            val cachedBlockState = this.cachedState
            /*if (blockState_1.block !== Blocks.BARREL) {
                this.invalidate()
                return
            }*/

            val isOpen = cachedBlockState.get(Barrel.OPEN) as Boolean
            if (isOpen) {
                this.playSound(cachedBlockState, SoundEvents.BLOCK_BARREL_CLOSE)
                this.setOpen(cachedBlockState, false)
            }
        }
    }

    override fun onInvClose(playerEntity: PlayerEntity) {
        if (!playerEntity.isSpectator) {
            --this.viewerCount
        }
    }

    private fun setOpen(blockState: BlockState, open: Boolean) {
        this.world!!.setBlockState(this.getPos(), blockState.with(Barrel.OPEN, open) as BlockState, 3)
    }

    private fun playSound(blockState: BlockState, soundEvent: SoundEvent) {
        val facePos = (blockState.get(Barrel.FACING) as Direction).vector
        val x = this.pos.x.toDouble() + 0.5 + facePos.x.toDouble() / 2.0
        val y = this.pos.y.toDouble() + 0.5 + facePos.y.toDouble() / 2.0
        val z = this.pos.z.toDouble() + 0.5 + facePos.z.toDouble() / 2.0
        this.world!!.playSound(null as PlayerEntity?, x, y, z, soundEvent, SoundCategory.BLOCKS, 0.5f, this.world!!.random.nextFloat() * 0.1f + 0.9f)
    }
}
