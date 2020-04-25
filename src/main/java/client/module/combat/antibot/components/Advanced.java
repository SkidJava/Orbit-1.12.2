package client.module.combat.antibot.components;

import client.event.EventTarget;
import client.event.events.update.EventUpdate;
import client.event.events.misc.EventPacketReceive;
import client.module.combat.antibot.AntiBot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketSpawnPlayer;

public class Advanced extends AntiBot {
    public Advanced() {
        super("Advanced", false, true);
    }

    @EventTarget
    public void onReceivePacket(EventPacketReceive event) {
        if ((event.getPacket() instanceof SPacketSpawnPlayer)) {
            SPacketSpawnPlayer packet = (SPacketSpawnPlayer)event.getPacket();
            double posX = packet.getX() / 32.0D;
            double posY = packet.getY() / 32.0D;
            double posZ = packet.getZ() / 32.0D;

            double diffX = mc.thePlayer.posX - posX;
            double diffY = mc.thePlayer.posY - posY;
            double diffZ = mc.thePlayer.posZ - posZ;

            double dist = Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);
            if ((dist <= 17.0D) && (posX != mc.thePlayer.posX) && (posY != mc.thePlayer.posY) && (posZ != mc.thePlayer.posZ)) {
                event.setCancelled(true);
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.theWorld.playerEntities != null) {
            for (Object object : mc.theWorld.playerEntities)
            {
                EntityPlayer entityPlayer = (EntityPlayer)object;
                if ((entityPlayer != null) && (entityPlayer != mc.thePlayer) && (!entityPlayer.isSprinting()) && (entityPlayer.isInvisible())) {
                    bot.add(entityPlayer);
                }
            }
        }
    }
}
