package client;

import client.gui.screen.GuiClientLoading;
import client.manager.Managers;
import net.minecraft.client.Minecraft;

public class Client {
    private static Client theClient;

    public static Client getClient() {
        return theClient;
    }

    public void init() {
        theClient = this;

        Managers managers = new Managers();

        Minecraft.getMinecraft().displayGuiScreen(new GuiClientLoading());

        managers.loadManagers();
    }
}
