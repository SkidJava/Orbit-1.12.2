package client.module;

import client.setting.Setting;
import org.lwjgl.input.Keyboard;

public class Empty extends Module {

    public Empty() {
        super("Empty", Keyboard.KEY_NONE, null, false);
        managers.settingManager.addSetting(new Setting(this, "Empty", true));
    }
}
