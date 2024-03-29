package yummyloop.test.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;


public interface Eve {

    Event<PlayerScreenHandler2> PLAYER_SCREEN_HANDLER_POST = EventFactory.createLoop();

    //LightningEvent.STRIKE.invoker().onStrike((LightningEntity) (Object) this, this.world, this.getPos(), list);
    interface PlayerScreenHandler2 {
        void playerScreenHandler3(PlayerInventory inventory, boolean onServer, PlayerEntity owner);
    }
}
