package client.event.events.player;

import client.event.Event;

public class EventPlayerVelocity extends Event {

    public double motionX, motionY, motionZ;

    public EventPlayerVelocity(double x, double y, double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
    }

}
