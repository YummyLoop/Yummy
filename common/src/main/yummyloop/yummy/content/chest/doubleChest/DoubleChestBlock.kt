package yummyloop.yummy.content.chest.doubleChest

import me.shedaniel.architectury.registry.MenuRegistry
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
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
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.*
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import yummyloop.common.block.entity.LootableContainerBlockEntityImpl
import yummyloop.common.inventory.MergedInventory
import yummyloop.common.network.packets.add

open class DoubleChestBlock(settings: Settings) : BlockWithEntity(settings), Waterloggable, InventoryProvider {
    open val columns = 9
    open val rows = 3

    companion object {
        val FACING: DirectionProperty = HorizontalFacingBlock.FACING
        val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED
        var CHEST_TYPE: EnumProperty<ChestType> = Properties.CHEST_TYPE

        fun getDoubleChestDirection(state: BlockState): Direction {
            val direction = state.get(FACING) as Direction
            return if (state.get(CHEST_TYPE) == ChestType.LEFT) direction.rotateYCounterclockwise() else direction.rotateYClockwise()
        }
    }

    override fun appendProperties(builder: StateManager.Builder<Block?, BlockState?>) {
        builder.add(FACING, WATERLOGGED, CHEST_TYPE)
    }

    init {
        defaultState = this.stateManager.defaultState
            .with(FACING, Direction.NORTH)
            .with(WATERLOGGED, false)
            .with(CHEST_TYPE, ChestType.SINGLE)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        var chestType = ChestType.SINGLE
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
                        if (neighborChestDirection.rotateYCounterclockwise() == sideDirection.opposite) {
                            chestType = ChestType.LEFT
                        } else {
                            chestType = ChestType.RIGHT
                        }
                    }
                }
            }
        } else {
            if (chestType == ChestType.SINGLE) {
                // if the chest at the left is facing the same direction
                if (facingDirection == this.getNeighborChestDirection(ctx, facingDirection.rotateYClockwise())) {
                    chestType = ChestType.RIGHT
                } else {
                    // if the chest at the right is facing the same direction
                    if (facingDirection == this.getNeighborChestDirection(ctx,
                            facingDirection.rotateYCounterclockwise())
                    ) {
                        chestType = ChestType.LEFT
                    }
                }
            }
        }

        return this.defaultState
            .with(FACING, facingDirection)
            .with(WATERLOGGED, fluidState.fluid == Fluids.WATER)
            .with(CHEST_TYPE, chestType)
    }

    private fun getNeighborChestDirection(ctx: ItemPlacementContext, dir: Direction): Direction? {
        val sideBlockState = ctx.world.getBlockState(ctx.blockPos.offset(dir))

        if (sideBlockState.isOf(this)) {
            if (sideBlockState.get(CHEST_TYPE) == ChestType.SINGLE) {
                return sideBlockState.get(FACING)
            }
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

        /** If updated block is a chest block and is in the horizontal axis*/
        if (neighborState.isOf(this) && direction.axis.isHorizontal) {
            val neighborChestType = neighborState.get(CHEST_TYPE) as ChestType

            /** This chest is single type chest */
            if (state.get(CHEST_TYPE) == ChestType.SINGLE) {
                /** This updated chest is not a single type chest */
                if (neighborChestType != ChestType.SINGLE) {
                    /** Both chests are facing the same direction */
                    if (state.get(FACING) == neighborState.get(FACING)) {
                        /** The updated chest corresponds to the corresponding double chest */
                        if (getDoubleChestDirection(neighborState) == direction.opposite) {
                            return state.with(CHEST_TYPE, neighborChestType.opposite)
                        }
                    }
                }
            }
        } else {
            /** The updated block corresponds to the complementary block of the double chest */
            if (getDoubleChestDirection(state) == direction) {
                return state.with(CHEST_TYPE, ChestType.SINGLE)
            }
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

    override fun getOutlineShape(state: BlockState, view: BlockView, pos: BlockPos, ctx: ShapeContext): VoxelShape {
        return when (state[FACING]) {
            Direction.NORTH -> Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 14.0, 15.0)
            Direction.SOUTH -> Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 14.0, 15.0)
            Direction.EAST -> Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 14.0, 15.0)
            Direction.WEST -> Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 14.0, 15.0)
            else -> VoxelShapes.fullCube()
        }
    }

    override fun createBlockEntity(world: BlockView?): BlockEntity = DoubleChestEntity(columns, rows)

    override fun getRenderType(state: BlockState?): BlockRenderType = BlockRenderType.ENTITYBLOCK_ANIMATED


    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos?,
        player: PlayerEntity,
        hand: Hand?,
        hit: BlockHitResult?,
    ): ActionResult {
        if (state.get(CHEST_TYPE) == ChestType.RIGHT) {
            val doubleChestPos = pos!!.offset(getDoubleChestDirection(state))

            world.getBlockState(doubleChestPos).onUse(world, player, hand,
                BlockHitResult(
                    Vec3d(
                        doubleChestPos.x.toDouble(),
                        doubleChestPos.y.toDouble(),
                        doubleChestPos.z.toDouble()
                    ),
                    getDoubleChestDirection(state),
                    doubleChestPos,
                    false
                )
            )
            return ActionResult.CONSUME
        }

        return if (world.isClient) {
            ActionResult.SUCCESS
        } else {
            // return the blockEntity of this block casted to namedScreenHandlerFactory
            val screenHandlerFactory = state.createScreenHandlerFactory(world, pos)
            if (player is ServerPlayerEntity) {
                if (state.get(CHEST_TYPE) == ChestType.LEFT) {
                    MenuRegistry.openExtendedMenu(player, screenHandlerFactory) { it.add(columns, 2 * rows) }
                } else {
                    MenuRegistry.openExtendedMenu(player, screenHandlerFactory) { it.add(columns, rows) }
                }
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

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int {
        val chestType = state.get(CHEST_TYPE)
        if (chestType == ChestType.LEFT || chestType == ChestType.RIGHT) {
            return ScreenHandler.calculateComparatorOutput(MergedInventory(
                world.getBlockEntity(pos) as DoubleChestEntity,
                world.getBlockEntity(pos.offset(getDoubleChestDirection(state))) as DoubleChestEntity)
            )
        } else {
            return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos) as Inventory)
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
            if (blockEntity is LootableContainerBlockEntityImpl) blockEntity.customName = itemStack.name
        }
    }

    override fun getInventory(state: BlockState, world: WorldAccess, pos: BlockPos): SidedInventory {
        val chestType = state.get(CHEST_TYPE)
        if (chestType == ChestType.LEFT || chestType == ChestType.RIGHT) {
            return object : MergedInventory(
                world.getBlockEntity(pos) as DoubleChestEntity,
                world.getBlockEntity(pos.offset(getDoubleChestDirection(state))) as DoubleChestEntity), SidedInventory {
                override fun getAvailableSlots(side: Direction?): IntArray = IntArray(this.size()) { it }
                override fun canInsert(slot: Int, stack: ItemStack?, dir: Direction?): Boolean = true
                override fun canExtract(slot: Int, stack: ItemStack?, dir: Direction?): Boolean = true
            }
        } else {
            return world.getBlockEntity(pos) as DoubleChestEntity
        }
    }
}