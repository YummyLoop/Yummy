package yummyloop.yummy.content.chest.doubleChest

import dev.architectury.registry.menu.MenuRegistry
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.InventoryProvider
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.enums.ChestType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluids
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.state.StateManager
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import yummyloop.common.inventory.MergedInventory
import yummyloop.common.network.packets.add
import yummyloop.yummy.content.chest.singleChest.SingleChestBlock

open class DoubleChestBlock(settings: Settings) : SingleChestBlock(settings), InventoryProvider {
    override val size = 27

    companion object {
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
                val neighborChestFacingDirection: Direction? =
                    this.getNeighborChestDirection(ctx, sideDirection.opposite)
                // if there a chest in the pointing direction
                if (neighborChestFacingDirection != null) {
                    // if the block is not facing or against the player/pointing direction (is sideways)
                    if (neighborChestFacingDirection.axis !== sideDirection.axis) {
                        facingDirection = neighborChestFacingDirection
                        val neighborState = ctx.world.getBlockState(ctx.blockPos.offset(sideDirection.opposite))
                        // if the neighbor chest is of type single
                        if (neighborState.get(CHEST_TYPE) == ChestType.SINGLE) {
                            if (neighborChestFacingDirection.rotateYCounterclockwise() == sideDirection.opposite) {
                                chestType = ChestType.LEFT
                            } else {
                                chestType = ChestType.RIGHT
                            }
                        }
                    }
                }
            }
        } else {
            if (chestType == ChestType.SINGLE) {
                // if the chest at the left is facing the same direction
                var neighborFacingDirection = this.getNeighborChestDirection(ctx, facingDirection.rotateYClockwise())
                if (facingDirection == neighborFacingDirection) {
                    val neighborState = ctx.world.getBlockState(ctx.blockPos.offset(facingDirection.rotateYClockwise()))
                    // if the neighbor chest is of type single
                    if (neighborState.get(CHEST_TYPE) == ChestType.SINGLE) {
                        chestType = ChestType.RIGHT
                    }
                }
                // if the chest at the right is facing the same direction
                neighborFacingDirection = this.getNeighborChestDirection(ctx, facingDirection.rotateYCounterclockwise())
                if (facingDirection == neighborFacingDirection) {
                    val neighborState =
                        ctx.world.getBlockState(ctx.blockPos.offset(facingDirection.rotateYCounterclockwise()))
                    // if the neighbor chest is of type single
                    if (neighborState.get(CHEST_TYPE) == ChestType.SINGLE) {
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

    override fun getOutlineShape(state: BlockState, view: BlockView, pos: BlockPos, ctx: ShapeContext): VoxelShape {
        return when (state[FACING]) {
            Direction.NORTH -> Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 14.0, 15.0)
            Direction.SOUTH -> Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 14.0, 15.0)
            Direction.EAST -> Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 14.0, 15.0)
            Direction.WEST -> Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 14.0, 15.0)
            else -> VoxelShapes.fullCube()
        }
    }

    override fun createBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity? = DoubleChestEntity(size, pos, state)

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
                    MenuRegistry.openExtendedMenu(player, screenHandlerFactory) { it.add(2 * size) }
                } else {
                    MenuRegistry.openExtendedMenu(player, screenHandlerFactory) { it.add(size) }
                }
            }

            ActionResult.CONSUME
        }
    }

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