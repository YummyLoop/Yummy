package yummyloop.example.block.storage

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.BlockState
import net.minecraft.container.Container
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.stat.Stats
import net.minecraft.state.StateFactory
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.Hand
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.World
import yummyloop.example.block.BlockEntity
import yummyloop.example.block.BlockWithEntity
import yummyloop.example.block.Blocks
import yummyloop.example.item.Item
import yummyloop.example.item.Items
import yummyloop.example.util.data.DataManager
import yummyloop.example.util.data.LevelChestData
import yummyloop.example.util.registry.RegistryManager
import java.util.*
import net.minecraft.block.Block as VanillaBlock
import net.minecraft.block.entity.BlockEntity as VanillaBlockEntity

class Barrel(blockName: String, blockSettings: FabricBlockSettings) : BlockWithEntity(blockName, blockSettings) {

    companion object {
        val FACING: DirectionProperty = Properties.FACING
        val OPEN: BooleanProperty = Properties.OPEN
    }

    init {
        this.defaultState = ((this.stateFactory.defaultState as BlockState).with(FACING, Direction.NORTH) as BlockState).with(OPEN, false) as BlockState
        IEntity
    }

    override fun createBlockEntity(blockView: BlockView): VanillaBlockEntity = IEntity()

    override fun activate(blockState: BlockState, world: World, blockPos: BlockPos, playerEntity: PlayerEntity, hand: Hand, blockHitResult: BlockHitResult): Boolean {
        if (world.isClient) return true

        val blockEntity = world.getBlockEntity(blockPos)
        if (blockEntity is IEntity) {
            playerEntity.openContainer(blockEntity)
            playerEntity.incrementStat(Stats.OPEN_BARREL)
        }else{
            println("Hello this should not be happening!")
            println(blockEntity.toString())
        }

        return true
    }

    // Replace this
    override fun onBlockRemoved(blockState: BlockState, world: World, blockPos: BlockPos, blockState_2: BlockState, boolean: Boolean) {
        if (blockState.block !== blockState_2.block) {
            val blockEntity = world.getBlockEntity(blockPos)
            if (blockEntity is IEntity) {
                //ItemScatterer.spawn(world, blockPos, (blockEntity as Inventory))
                val self = ItemStack(Items["test_barrel"])
                self.orCreateTag.putLong("storage_id", blockEntity.id())
                ItemScatterer.spawn(world, blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble(), self)
                world.updateHorizontalAdjacent(blockPos, this)
            }

            super.onBlockRemoved(blockState, world, blockPos, blockState_2, boolean)
        }
    }

    override fun onScheduledTick(blockState: BlockState, world: World, blockPos: BlockPos, random: Random) {
        val blockEntity = world.getBlockEntity(blockPos)
        if (blockEntity is IEntity) {
            blockEntity.tick()
        }
    }

    override fun onPlaced(world: World, blockPos: BlockPos, blockState: BlockState, livingEntity: LivingEntity?, itemStack: ItemStack) {
        if (itemStack.hasCustomName()) {
            val blockEntity = world.getBlockEntity(blockPos)
            if (blockEntity is IEntity) {
                blockEntity.customName = itemStack.name
            }
        }

        if (!world.isClient) {
            val tag = itemStack.orCreateTag
            val blockEntity = world.getBlockEntity(blockPos)
            if (blockEntity is IEntity) {
                if (!tag.isEmpty) {
                    blockEntity.setId(tag.getLong("storage_id"))
                } else {
                    blockEntity.newId()
                }
            }
        }
    }

    override fun hasComparatorOutput(blockState: BlockState): Boolean = true
    override fun getComparatorOutput(blockState: BlockState, world: World, blockPos: BlockPos): Int {
        return Container.calculateComparatorOutput(world.getBlockEntity(blockPos))
    }

    override fun rotate(blockState: BlockState, blockRotation: BlockRotation): BlockState {
        return blockState.with(FACING, blockRotation.rotate(blockState.get(FACING) as Direction)) as BlockState
    }

    override fun mirror(blockState: BlockState, blockMirror: BlockMirror): BlockState {
        return blockState.rotate(blockMirror.getRotation(blockState.get(FACING) as Direction))
    }

    override fun appendProperties(factory: StateFactory.Builder<VanillaBlock, BlockState>) {
        factory.add(FACING, OPEN)
    }

    override fun getPlacementState(itemPlacementContext: ItemPlacementContext): BlockState {
        return this.defaultState.with(FACING, itemPlacementContext.playerLookDirection.opposite)
    }

    private class IEntity : BBlockEntity(TYPE) {
        companion object Register {
            private var blocks= listOf(Blocks["test_barrel"])   // List of blocks to apply the entity to
            val TYPE = BlockEntity.createType(::IEntity, blocks)
            init {
                RegistryManager.register(TYPE, "barrel_test_entity")
                //ClientManager.registerBlockEntityRenderer(IEntity::class.java, ::Renderer)
            }
        }
    }
}
