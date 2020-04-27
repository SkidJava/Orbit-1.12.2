package client.event.events.update;

import client.event.Event;
import net.minecraft.network.Packet;

public class PrePacketSendEvent extends Event {
    public Packet packet;
    public PrePacketSendEvent(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }
}
