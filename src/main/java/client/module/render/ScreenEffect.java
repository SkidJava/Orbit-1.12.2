package client.module.render;

import client.module.Module;
import client.setting.Setting;
import org.lwjgl.input.Keyboard;

public class ScreenEffect extends Module {

    public ScreenEffect() {
        super("ScreenEffect", Keyboard.KEY_NONE, Category.RENDER, false);

        managers.settingManager.addSetting(new Setting(this, "DamageEffect", true));
    }
}
