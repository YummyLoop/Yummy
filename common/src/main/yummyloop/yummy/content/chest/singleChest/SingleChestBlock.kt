package yummyloop.yummy.content.chest.singleChest

import me.shedaniel.architectury.registry.MenuRegistry
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.*
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import yummyloop.common.block.entity.ExtendedLootableContainerBlockEntity
import yummyloop.common.network.packets.add

open class SingleChestBlock(settings: Settings) : BlockWithEntity(settings), Waterloggable {
    open val columns = 9
    open val rows = 3

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
        val sideDirection = ctx.side

        //if Player is sneaking
        if (ctx.shouldCancelInteraction()) {
            //if Player is placing the block while point to the side of a block
            if (sideDirection.axis.isHorizontal) {
                val neighborChestDirection: Direction? = this.getNeighborChestDirection(ctx, sideDirection.opposite)
                // if there a chest in the pointing direction
                if (neighborChestDirection != null) {
                    // if the block is not facing or against the player/pointing direction (is sideways)
                    if (neighborChestDirection.axis !== sideDirection.axis) {
                        facingDirection = neighborChestDirection
                    }
                }
            }
        }

        return this.defaultState
            .with(FACING, facingDirection)
            .with(WATERLOGGED, fluidState.fluid == Fluids.WATER)
    }

    private fun getNeighborChestDirection(ctx: ItemPlacementContext, dir: Direction): Direction? {
        val sideBlockState = ctx.world.getBlockState(ctx.blockPos.offset(dir))

        if (sideBlockState.isOf(this)) {
            return sideBlockState.get(FACING)
        }

        return null
    }

    /** Update this block when the neighbor changes*/
    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos?,
        posFrom: BlockPos?,
    ): BlockState? {
        if (state.get(WATERLOGGED) as Boolean) {
            world.fluidTickScheduler.schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, posFrom)
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState? {
        return state.with(FACING, rotation.rotate(state.get(FACING) as Direction)) as BlockState
    }

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState? {
        return state.rotate(mirror.getRotation(state.get(FACING) as Direction))
    }

    override fun getFluidState(state: BlockState): FluidState? {
        return if (state.get(WATERLOGGED) as Boolean) Fluids.WATER.getStill(false) else super.getFluidState(state)
    }

    override fun getOutlineShape(state: BlockState, view: BlockView?, pos: BlockPos?, ctx: ShapeContext?): VoxelShape {
        return when (state[FACING]) {
            Direction.NORTH -> Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 14.0, 15.0)
            Direction.SOUTH -> Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 14.0, 15.0)
            Direction.EAST -> Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 14.0, 15.0)
            Direction.WEST -> Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 14.0, 15.0)
            else -> VoxelShapes.fullCube()
        }
    }

    override fun createBlockEntity(world: BlockView?): BlockEntity = SingleChestEntity(columns, rows)

    override fun getRenderType(state: BlockState?): BlockRenderType = BlockRenderType.ENTITYBLOCK_ANIMATED

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos?,
        player: PlayerEntity,
        hand: Hand?,
        hit: BlockHitResult?,
    ): ActionResult {
        return if (world.isClient) {
            ActionResult.SUCCESS
        } else {
            // return the blockEntity of this block casted to namedScreenHandlerFactory
            val screenHandlerFactory = state.createScreenHandlerFactory(world, pos)
            if (player is ServerPlayerEntity) {
                MenuRegistry.openExtendedMenu(player, screenHandlerFactory) { it.add(columns, rows) }
            }

            ActionResult.CONSUME
        }
    }

    /** On break, replace, ... */
    override fun onStateReplaced(
        state: BlockState,
        world: World,
        pos: BlockPos?,
        newState: BlockState,
        moved: Boolean,
    ) {
        if (!state.isOf(newState.block)) {
            val blockEntity = world.getBlockEntity(pos)
            if (blockEntity is Inventory) {
                ItemScatterer.spawn(world, pos, blockEntity)
                world.updateComparators(pos, this)
            }
            super.onStateReplaced(state, world, pos, newState, moved)
        }
    }

    override fun hasComparatorOutput(state: BlockState?): Boolean = true

    override fun getComparatorOutput(state: BlockState?, world: World?, pos: BlockPos?): Int {
        val blockEntity = world!!.getBlockEntity(pos)
        return if (blockEntity is Inventory) {
            ScreenHandler.calculateComparatorOutput(blockEntity as Inventory)
        } else ScreenHandler.calculateComparatorOutput(null as Inventory?)
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
            if (blockEntity is ExtendedLootableContainerBlockEntity) blockEntity.customName = itemStack.name
        }
    }
}