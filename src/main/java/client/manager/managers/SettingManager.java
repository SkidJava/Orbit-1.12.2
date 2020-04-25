package client.manager.managers;

import client.manager.AbstManager;
import client.module.Module;
import client.setting.Setting;

import java.util.ArrayList;

public class SettingManager extends AbstManager {

    private ArrayList<Setting> settings = new ArrayList<>();

    @Override
    public void load() {
        loadEnd();
    }

    public void addSetting(Setting s) {
        this.settings.add(s);
    }

    public Setting getSetting(Module m, String name) {
        for (Setting s : this.settings) {
            if (s.getModule().equals(m) && s.getName().equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
    }

    public Setting getSetting(Class<? extends Module> m, String name) {
        for (Setting s : this.settings) {
            if (s.getModule().equals(managers.moduleManager.getModule(m)) && s.getName().equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
    }

    public ArrayList<Setting> getSettings() {
        return this.settings;
    }

    public ArrayList<Setting> getSettingsForModule(Module m) {
        ArrayList<Setting> settings = new ArrayList<>();

        for (Setting s : this.settings) {
            if (s.getModule().equals(m)) {
                settings.add(s);
            }
        }

        return settings;
    }

}
