package client.module.render;

import client.module.Module;
import org.lwjgl.input.Keyboard;

public class ViewClip extends Module {
    public ViewClip() {
        super("ViewClip", Keyboard.KEY_NONE, Category.RENDER);
    }
}
