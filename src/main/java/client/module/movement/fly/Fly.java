package client.module.movement.fly;

import client.module.Module;
import org.lwjgl.input.Keyboard;

public class Fly extends Module {
    public Fly() {
        super("Fly", Keyboard.KEY_NONE, Category.MOVEMENT, true, true);
    }

    public Fly(String name, boolean visible, boolean enabled) {
        super(name, visible, enabled);
    }
    public Fly(String name, boolean visible) {
        super(name, visible);
    }
}
