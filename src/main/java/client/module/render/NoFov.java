package client.module.render;

import client.module.Module;
import org.lwjgl.input.Keyboard;

public class NoFov extends Module {
    public NoFov() {
        super("NoFov", Keyboard.KEY_NONE, Category.RENDER);
    }
}
