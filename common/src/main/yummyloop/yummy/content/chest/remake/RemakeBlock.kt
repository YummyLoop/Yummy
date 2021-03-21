package yummyloop.yummy.content.chest.remake

import me.shedaniel.architectury.registry.MenuRegistry
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.LootableContainerBlockEntity
import net.minecraft.block.enums.ChestType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import yummyloop.common.network.packets.add
import yummyloop.yummy.content.chest.doubleChest.DoubleChestBlock

class RemakeBlock(settings: Settings?) : BlockWithEntity(settings), InventoryProvider, Waterloggable{
    companion object {
        val FACING: DirectionProperty = HorizontalFacingBlock.FACING
        val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED
    }

    override fun appendProperties(builder: StateManager.Builder<Block?, BlockState?>) {
        builder.add(FACING, WATERLOGGED)
    }

    init {
        defaultState = this.stateManager.defaultState
            .with(FACING, Direction.NORTH)
            .with(WATERLOGGED, false)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        var facingDirection = ctx.playerFacing.opposite
        val fluidState = ctx.world.getFluidState(ctx.blockPos)


        return this.defaultState
            .with(FACING, facingDirection)
            .with(WATERLOGGED, fluidState.fluid == Fluids.WATER)
    }

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        posFrom: BlockPos,
    ): BlockState? {
        if (state.get(DoubleChestBlock.WATERLOGGED) as Boolean) {
            world.fluidTickScheduler.schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
        }

        val a = world.getBlockEntity(pos.up())
        if ( a is RemakeEntity) {
            (world.getBlockEntity(pos) as RemakeEntity).append(a)
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, posFrom)
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        return state.with(FACING, rotation.rotate(state.get(FACING) as Direction))
    }

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState {
        return state.rotate(mirror.getRotation(state.get(FACING)))
    }

    override fun getFluidState(state: BlockState): FluidState? {
        return if (state.get(WATERLOGGED)) Fluids.WATER.getStill(false) else super.getFluidState(state)
    }


    override fun createBlockEntity(world: BlockView): BlockEntity {
        return RemakeEntity()
    }

    override fun getRenderType(state: BlockState): BlockRenderType = BlockRenderType.ENTITYBLOCK_ANIMATED

    override fun onSyncedBlockEvent(state: BlockState?, world: World, pos: BlockPos?, type: Int, data: Int): Boolean {
        return super.onSyncedBlockEvent(state, world, pos, type, data)
    }

    override fun createScreenHandlerFactory(
        state: BlockState,
        world: World,
        pos: BlockPos,
    ): NamedScreenHandlerFactory? {
        return super.createScreenHandlerFactory(state, world, pos)
    }

    override fun getInventory(state: BlockState, world: WorldAccess, pos: BlockPos): SidedInventory {
        return world.getBlockEntity(pos) as SidedInventory
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult,
    ): ActionResult {
        return if (world.isClient) {
            ActionResult.SUCCESS
        } else {
            // return the blockEntity of this block casted to namedScreenHandlerFactory
            val screenHandlerFactory = state.createScreenHandlerFactory(world, pos)
            if (player is ServerPlayerEntity) {
                MenuRegistry.openExtendedMenu(player, screenHandlerFactory) { it.add(9, (world.getBlockEntity(pos) as RemakeEntity).inventoryList.size) }
            }

            ActionResult.CONSUME
        }
    }

    override fun canPathfindThrough(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        type: NavigationType?,
    ): Boolean {
        return false
    }

    override fun onPlaced(
        world: World,
        pos: BlockPos?,
        state: BlockState?,
        placer: LivingEntity?,
        itemStack: ItemStack,
    ) {
        if (itemStack.hasCustomName()) {
            val blockEntity = world.getBlockEntity(pos)
            if (blockEntity is LootableContainerBlockEntity) blockEntity.customName = itemStack.name
        }
    }
}