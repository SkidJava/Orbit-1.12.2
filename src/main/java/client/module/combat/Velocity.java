package client.module.combat;

import client.event.EventTarget;
import client.event.events.misc.EventPacketReceive;
import client.module.Module;
import client.setting.Setting;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import org.lwjgl.input.Keyboard;

public class Velocity extends Module {
    public Velocity() {
        super("Velocity", Keyboard.KEY_NONE, Category.COMBAT);
    }

    @EventTarget
    private void onPacketReceive(EventPacketReceive event) {
        if (mc.thePlayer.fallDistance > 1) return;
        if (event.packet instanceof SPacketEntityVelocity || event.packet instanceof SPacketEntityTeleport || event.packet instanceof SPacketExplosion) {
            if (event.packet instanceof SPacketEntityTeleport) {
                SPacketEntityTeleport packet;
                if (this.mc.theWorld.getEntityByID((packet = (SPacketEntityTeleport)event.packet).getEntityId()) == this.mc.thePlayer) event.setCancelled(true);
            } else {
                event.setCancelled(true);
            }
        }
    }
}
