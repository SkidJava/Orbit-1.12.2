package client.module.combat.antibot.components;

import client.event.EventTarget;
import client.event.events.misc.EventPacketReceive;
import client.event.events.update.EventUpdate;
import client.module.combat.antibot.AntiBot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketSpawnPlayer;

public class Invisible extends AntiBot {
    public Invisible() {
        super("Invisible", false, true);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.thePlayer.ticksExisted % 100 == 0) {
            bot.clear();
        }
        for (Object object : mc.theWorld.playerEntities)
        {
            EntityPlayer entityPlayer = (EntityPlayer)object;
            if ((entityPlayer != null) && (entityPlayer != mc.thePlayer) && entityPlayer.isInvisibleToPlayer(mc.thePlayer)) {
                bot.add(entityPlayer);
            }
        }
    }
}
