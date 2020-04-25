package client.event.events.render;

import client.event.Event;
import net.minecraft.client.gui.GuiScreen;

public class EventUpdateScreen extends Event {

    public int width;
    public int height;

    public EventUpdateScreen(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
