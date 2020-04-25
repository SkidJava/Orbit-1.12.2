package client.module.render;

import client.module.Module;
import org.lwjgl.input.Keyboard;

public class ESP extends Module {
    public ESP() {
        super("ESP", Keyboard.KEY_NONE, Category.RENDER);
    }
}
