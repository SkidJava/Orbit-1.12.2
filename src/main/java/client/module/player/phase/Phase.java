package client.module.player.phase;

import client.module.Module;
import org.lwjgl.input.Keyboard;

public class Phase extends Module {
    public Phase() {
        super("Phase", Keyboard.KEY_NONE, Category.PLAYER, true, true);
    }

    public Phase(String name, boolean visible, boolean enabled) {
        super(name, visible, enabled);
    }
    public Phase(String name, boolean visible) {
        super(name, visible);
    }
}
