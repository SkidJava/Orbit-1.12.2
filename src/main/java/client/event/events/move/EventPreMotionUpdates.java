package client.event.events.move;

import client.event.Event;

public class EventPreMotionUpdates extends Event {

    public float yaw, pitch;
    public double x, y, z;
    private boolean cancel;

    public EventPreMotionUpdates(float yaw, float pitch, double x, double y, double z) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.x = x;
        this.y = y;
        this.z = z;
    }

}
