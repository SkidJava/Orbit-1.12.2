package client.event.events.render;

import client.event.Event;
import net.minecraft.client.gui.GuiScreen;

public class EventOpenScreen extends Event {

    private GuiScreen screen;

    public EventOpenScreen(GuiScreen screen) {
        this.screen = screen;
    }

    public GuiScreen getScreen() {
        return screen;
    }
}
