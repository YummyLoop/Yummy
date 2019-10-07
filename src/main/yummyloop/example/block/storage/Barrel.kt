package yummyloop.example.block.storage

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.Block as VanillaBlock
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BarrelBlockEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.container.Container
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
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
import yummyloop.example.block.BlockWithEntity
import java.util.*

class Barrel(blockName: String, blockSettings: FabricBlockSettings) : BlockWithEntity(blockName, blockSettings) {

    companion object {
        val FACING: DirectionProperty = Properties.FACING
        val OPEN: BooleanProperty = Properties.OPEN
    }

    init {
        this.defaultState = ((this.stateFactory.defaultState as BlockState).with(FACING, Direction.NORTH) as BlockState).with(OPEN, false) as BlockState
    }

    override fun createBlockEntity(blockView: BlockView): BlockEntity = BarrelBlockEntity()

    override fun activate(blockState: BlockState, world: World, blockPos: BlockPos, playerEntity: PlayerEntity, hand: Hand, blockHitResult: BlockHitResult): Boolean {
        if (world.isClient) return true

        val blockEntity = world.getBlockEntity(blockPos)
        if (blockEntity is BarrelBlockEntity) {
            playerEntity.openContainer(blockEntity)
            playerEntity.incrementStat(Stats.OPEN_BARREL)
        }

        return true
    }

    // Replace this
    override fun onBlockRemoved(blockState: BlockState, world: World, blockPos: BlockPos, blockState_2: BlockState, boolean: Boolean) {
        if (blockState.block !== blockState_2.block) {
            val blockEntity = world.getBlockEntity(blockPos)
            if (blockEntity is Inventory) {
                ItemScatterer.spawn(world, blockPos, (blockEntity as Inventory))
                world.updateHorizontalAdjacent(blockPos, this)
            }

            super.onBlockRemoved(blockState, world, blockPos, blockState_2, boolean)
        }
    }

    override fun onScheduledTick(blockState: BlockState, world: World, blockPos: BlockPos, random: Random) {
        val blockEntity = world.getBlockEntity(blockPos)
        if (blockEntity is BarrelBlockEntity) {
            blockEntity.tick()
        }
    }

    override fun onPlaced(world: World, blockPos: BlockPos, blockState: BlockState, livingEntity: LivingEntity?, itemStack: ItemStack) {
        if (itemStack.hasCustomName()) {
            val blockEntity = world.getBlockEntity(blockPos)
            if (blockEntity is BarrelBlockEntity) {
                blockEntity.customName = itemStack.name
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
}
