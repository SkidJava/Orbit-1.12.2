package client.module.render;

import client.module.Module;
import client.setting.Setting;
import org.lwjgl.input.Keyboard;

public class Scoreboard extends Module {
    public Scoreboard() {
        super("Scoreboard", Keyboard.KEY_NONE, Category.RENDER, false);
        managers.settingManager.addSetting(new Setting(this, "Height", 200F, Integer.MIN_VALUE, Integer.MAX_VALUE, true));
        managers.settingManager.addSetting(new Setting(this, "Draw Int", false));
    }
}
