package yummyloop.yummy.content.chest

import me.shedaniel.architectury.registry.MenuRegistry
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.ChestBlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World
import java.util.concurrent.TimeUnit

open class ChestBlock(settings: Settings) : BlockWithEntity(settings) {

    init {

    }

    override fun createBlockEntity(world: BlockView?): BlockEntity = ChestEntity()

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
                MenuRegistry.openExtendedMenu(player, screenHandlerFactory) { }
            }

            ActionResult.CONSUME
        }
    }


}