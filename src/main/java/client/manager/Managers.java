package client.manager;

import client.manager.managers.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Managers {

    private static Managers theManagers;

    private final ArrayList<AbstManager> managerList;
    public boolean loadedManagers = false;

    public final FontManager fontManager;
    public final EventManager eventManager;
    public final CommandManager commandManager;
    public final SettingManager settingManager;
    public final ModuleManager moduleManager;
    public final ScreenManager sceneManager;

    public Managers() {
        theManagers = this;

        fontManager = new FontManager();
        eventManager = new EventManager();
        commandManager = new CommandManager();
        settingManager = new SettingManager();
        moduleManager = new ModuleManager();
        sceneManager = new ScreenManager();

        managerList = new ArrayList<>(Arrays.asList(fontManager, eventManager, commandManager, settingManager, moduleManager));
    }

    public static Managers getManagers() {
        return theManagers;
    }

    public void loadManagers() {
        loadedManagers = true;

        for (AbstManager m : this.managerList) {
            m.load();

            Thread thread = new Thread(() -> {
                m.asyncLoad();
                m.loadEnd();
            });
            thread.start();
        }
    }

    public boolean getLoading() {
        boolean loading = true;
        for (int i = 0; i < managerList.size() && loading; i++)
            loading = loading && managerList.get(i).getLoading();
        return loading;
    }
}
