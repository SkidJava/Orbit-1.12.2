package client.module.render;

import client.gui.ClickGui;
import client.module.Module;
import client.setting.Setting;
import org.lwjgl.input.Keyboard;

public class GUI extends Module {
    public GUI() {
        super("GUI", Keyboard.KEY_RSHIFT, Category.RENDER, false);
        managers.settingManager.addSetting(new Setting(this, "X", 30F, 0F, Integer.MAX_VALUE, true));
        managers.settingManager.addSetting(new Setting(this, "Y", 50F, 0F, Integer.MAX_VALUE, true));
    }
    @Override
    public void onEnable() {
        this.mc.displayGuiScreen(new ClickGui());
    }
}
