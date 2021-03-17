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
            (world.getBlockEntity(pos) as ChestEntity).isOpen = 1
            ActionResult.SUCCESS
        } else {

            //This will call the createScreenHandlerFactory method from BlockWithEntity, which will return our blockEntity casted to
            //a namedScreenHandlerFactory. If your block class does not extend BlockWithEntity, it needs to implement createScreenHandlerFactory.

            val screenHandlerFactory = state.createScreenHandlerFactory(world, pos)
            //if (screenHandlerFactory != null) {
            //With this call the server will request the client to open the appropriate Screenhandler
            // player.openHandledScreen(screenHandlerFactory)
            // }
            if (player is ServerPlayerEntity) {
                MenuRegistry.openExtendedMenu(player, screenHandlerFactory) { }
            }


            ActionResult.CONSUME
        }
    }


}