package client.module.render;

import client.module.Module;
import org.lwjgl.input.Keyboard;

public class AntiFC extends Module {
    public AntiFC() {
        super("AntiFC", Keyboard.KEY_NONE, Category.RENDER);
    }
}
