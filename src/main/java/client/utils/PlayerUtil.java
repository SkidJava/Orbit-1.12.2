package client.utils;

import client.ClientInfo;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.TextComponentString;

public class PlayerUtil {

    public static void tellPlayer(String text) {
        GuiIngame.getChatGUI().printChatMessage(new TextComponentString(
                ChatFormatting.WHITE + "[" + ChatFormatting.RED + ClientInfo.getClientName() + ChatFormatting.WHITE + "] " + text));
    }

    public static boolean isMoving(Entity e)
    {
        return (e.motionX != 0.0D) && (e.motionZ != 0.0D) && ((e.motionY != 0.0D) || (e.motionY > 0.0D));
    }

    // TODO: EntityPlayerSPからなぜ消えたこのメゾット
    public static float getSpeed()
    {
        float vel = (float)Math.sqrt(Minecraft.getMinecraft().thePlayer.motionX * Minecraft.getMinecraft().thePlayer.motionX + Minecraft.getMinecraft().thePlayer.motionZ * Minecraft.getMinecraft().thePlayer.motionZ);
        return vel;
    }

    // TODO: え！？
    public static void setSpeed(double speed)
    {
        Minecraft.getMinecraft().thePlayer.motionX = (-Math.sin(RotationUtils.getDirection()) * speed);
        Minecraft.getMinecraft().thePlayer.motionZ = (Math.cos(RotationUtils.getDirection()) * speed);
    }
}
