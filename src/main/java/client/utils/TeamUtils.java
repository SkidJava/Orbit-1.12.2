package client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class TeamUtils {
    public static boolean isTeammate(final Entity entity) {
        boolean team = false;
        if (entity instanceof EntityPlayer) {
            final String n = entity.getDisplayName().getFormattedText();
            if (n.startsWith("§f") && !n.equalsIgnoreCase(entity.getName())) {
                team = n.substring(0, 6).equalsIgnoreCase(Minecraft.getMinecraft().thePlayer.getDisplayName().getFormattedText().substring(0, 6));
            }
            else {
                team = n.substring(0, 2).equalsIgnoreCase(Minecraft.getMinecraft().thePlayer.getDisplayName().getFormattedText().substring(0, 2));
            }
        }
        return team;
    }
}
