package client.module.render;

import client.module.Module;
import org.lwjgl.input.Keyboard;

public class NameProtect extends Module {
    public NameProtect() {
        super("NameProtect", Keyboard.KEY_NONE, Category.RENDER);
    }
}
