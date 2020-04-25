package client.module.movement;

import client.event.EventTarget;
import client.event.events.player.EventSlowDown;
import client.event.events.update.ItemSpeedEvent;
import client.module.Module;
import org.lwjgl.input.Keyboard;

public class NoSlowdown extends Module {
    public NoSlowdown() {
        super("NoSlowdown", Keyboard.KEY_NONE, Category.MOVEMENT);
    }
    @EventTarget
    private void onItemUse(ItemSpeedEvent event) {
        event.setCancelled(true);
    }

    @EventTarget
    private void onSlowdown(EventSlowDown event) {
        event.setCancelled(true);
    }
}
