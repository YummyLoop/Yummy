package yummyloop.yummy.old.util.data

import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.DefaultedList

class LevelChestData(world : ServerWorld, name : String) : DimensionData(world, name) {

    private var nextId : Long = 0
    private val inventories = HashMap<Long, DefaultedList<ItemStack>>()

    override fun tick() {
        //testInt++
        //println(testInt)
        //this.markDirty()
    }

    init {// Changes made here will only take effect the first time the data is saved/created
        this.markDirty()
    }

    override fun toTag(tag: CompoundTag): CompoundTag {
        tag.putLong("nextId", this.nextId)
        for (i in inventories){
            tag.put(i.key.toString(), Inventories.toTag(CompoundTag(), i.value))
        }
        return tag
    }

    override fun fromTag(tag: CompoundTag) {
        this.nextId = tag.getLong("nextId")
        for (i in 0 until nextId){
            val iTag : CompoundTag = tag.getCompound(i.toString())
            val inventory = DefaultedList.ofSize(27, ItemStack.EMPTY)
            Inventories.fromTag(iTag, inventory)
            inventories[i]=inventory
        }
    }

    fun new(): DefaultedList<ItemStack> {
        val inv = DefaultedList.ofSize(27, ItemStack.EMPTY)
        inventories[nextId]=inv
        nextId++
        this.markDirty()
        return inv
    }

    operator fun get(id: Long) : DefaultedList<ItemStack>? {
        if (inventories[id] == null){
            if (id >= nextId){
                nextId=id
                return new()
            }else{
                set(id, DefaultedList.ofSize(27, ItemStack.EMPTY))
            }
        }
        return inventories[id]
    }

    operator fun set(id: Long, inv : DefaultedList<ItemStack>?) {
        if (inv!=null){
            inventories[id]=inv
            this.markDirty()
        }
    }

    fun currentId() : Long {
        return (nextId-1)
    }

    fun nextId() : Long {
        return nextId
    }

    fun newId() : Long{
        nextId++
        markDirty()
        return nextId
    }
}