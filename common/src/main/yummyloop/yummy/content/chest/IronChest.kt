package yummyloop.yummy.content.chest

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World

open class IronChest(settings: Settings) : ChestBlock(settings) {
    override val columns = 9
    override val rows = 6

    override fun createBlockEntity(world: BlockView?): BlockEntity = IronChestEntity(columns, rows)

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos?,
        player: PlayerEntity,
        hand: Hand?,
        hit: BlockHitResult?
    ): ActionResult {
        if (world.isClient){
            val b = world.getBlockEntity(pos!!.add(1,0,0))

            if (b is IronChestEntity){ b.isOpen = 1 }
        }

        return super.onUse(state, world, pos, player, hand, hit)
    }
}