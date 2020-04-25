package client.event.events.misc;

import client.event.Cancellable;
import client.event.Event;
import net.minecraft.network.Packet;

public class EventPacketReceive extends Event implements Cancellable {
    private boolean cancel;
    public Packet packet;

    public EventPacketReceive(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}
