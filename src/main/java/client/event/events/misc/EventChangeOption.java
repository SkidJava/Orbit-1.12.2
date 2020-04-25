package client.event.events.misc;

import client.event.Event;
import client.module.Module;

public class EventChangeOption extends Event {

    private Module module;
    private String currentOption;
    private String previousOption;

    public EventChangeOption(Module module, String currentOption, String previousOption) {
        this.module = module;
        this.currentOption = currentOption;
        this.previousOption = previousOption;
    }

    public Module getModule() {
        return module;
    }

    public String getCurrentOption() {
        return currentOption;
    }

    public String getPreviousOption() {
        return previousOption;
    }
}
