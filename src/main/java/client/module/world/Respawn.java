package client.module.world;

import client.event.EventTarget;
import client.event.events.update.EventTick;
import client.module.Module;
import org.lwjgl.input.Keyboard;

public class Respawn extends Module {
    public Respawn() {
        super("Respawn", Keyboard.KEY_NONE, Category.WORLD, false);
    }
    @EventTarget
    private void onTick(EventTick event) {
        if (!this.mc.thePlayer.isEntityAlive()) {
            this.mc.thePlayer.respawnPlayer();
        }
    }
}
