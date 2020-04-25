package client.event.events.render;

import client.event.Cancellable;
import client.event.Event;
import net.minecraft.client.gui.GuiScreen;

public class EventChangeScreen extends Event implements Cancellable {

    private GuiScreen currentScreen;

    private GuiScreen previousScreen;

    public EventChangeScreen(GuiScreen currentScreen, GuiScreen previousScreen) {
        this.currentScreen = currentScreen;
        this.previousScreen = previousScreen;
    }

    public GuiScreen getCurrentScreen() {
        return currentScreen;
    }

    public GuiScreen getPreviousScreen() {
        return previousScreen;
    }
}
