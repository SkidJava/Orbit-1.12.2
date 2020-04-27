package client.event.events.update;

import client.event.Event;
import net.minecraft.network.Packet;

public class PostPacketSendEvent extends Event {
    public Packet packet;
    public PostPacketSendEvent(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }
}
