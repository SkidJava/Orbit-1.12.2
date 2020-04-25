package client.module.combat.antibot.components;

import client.event.EventTarget;
import client.event.events.update.EventUpdate;
import client.module.combat.antibot.AntiBot;
import client.setting.Setting;
import net.minecraft.entity.player.EntityPlayer;

public class Watchdog extends AntiBot {
    public Watchdog() {
        super("Watchdog", false);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.thePlayer.ticksExisted % 100 == 0) {
            bot.clear();
        }
        for (Object object : mc.theWorld.playerEntities)
        {
            EntityPlayer entityPlayer = (EntityPlayer)object;
            if ((entityPlayer != null) && (entityPlayer != mc.thePlayer) &&
                    (entityPlayer.getDisplayName().getFormattedText().equalsIgnoreCase(entityPlayer.getName() + "§r")) &&
                    (!mc.thePlayer.getDisplayName().getFormattedText().equalsIgnoreCase(mc.thePlayer.getName() + "§r"))) {
                bot.add(entityPlayer);
            }
        }
    }
}
