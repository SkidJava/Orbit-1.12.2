package client.module.render;

import client.event.EventTarget;
import client.event.events.render.EventRender2D;
import client.event.events.render.EventRender3D;
import client.manager.managers.FriendManager;
import client.module.Module;
import client.module.combat.antibot.AntiBot;
import client.setting.Setting;
import client.utils.ColorUtils;
import client.utils.render.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MiniMap extends Module {
    public MiniMap() {
        super("MiniMap", Keyboard.KEY_NONE, Category.RENDER);
        managers.settingManager.addSetting(new Setting(this, "Size", 150F, 0F, 200F, true));
//        managers.settingManager.addSetting(new Setting(this, "X", 1F, 0F, Integer.MAX_VALUE, true));
        managers.settingManager.addSetting(new Setting(this, "Y", 90F, 0F, Integer.MAX_VALUE, true));
    }

    @EventTarget
    private void onRender(EventRender2D event) {
        int posY = (int) managers.settingManager.getSetting(this, "Y").getCurrentValue();
        int size = (int) managers.settingManager.getSetting(this, "Size").getCurrentValue();
        int scale = 1;
        // TODO: background
        RenderUtils.drawBorderedRect(2, posY, 122, posY + 120, new Color(0, 0, 0, 100).getRGB(), Color.BLACK.getRGB());

        // TODO: cross
        RenderUtils.drawRect(59F, posY, 60F, posY + 120, Color.lightGray.getRGB());
        RenderUtils.drawRect(2.5F, posY + 59.5F, 122F, posY + 60.5F, Color.lightGray.getRGB());

        // TODO: entities
        for (final EntityPlayer ent : list) {
//			float x = (float) (mc.thePlayer.posX - ent.posX);
//			float z = (float) (mc.thePlayer.posZ - ent.posZ);
            final float playerOffsetX = (float) mc.thePlayer.posX;
            final float playerOffSetZ = (float) mc.thePlayer.posZ;
            final float pTicks = mc.timer.elapsedTicks;
            final float posX = (float) ((ent.posX + (ent.posX - ent.lastTickPosX) * pTicks - playerOffsetX) * scale);
            final float posZ = (float) ((ent.posZ + (ent.posZ - ent.lastTickPosZ) * pTicks - playerOffSetZ) * scale);
            final float cos = (float) Math.cos(mc.thePlayer.rotationYaw * 0.017453292519943295);
            final float sin = (float) Math.sin(mc.thePlayer.rotationYaw * 0.017453292519943295);
            float rotX = -(posX * cos + posZ * sin);
            float rotZ = -(posZ * cos - posX * sin);
            if (rotZ > (float) size / 2 - 5) {
                rotZ = (float) size / 2 - 5.0f;
            } else if (rotZ < -((float) size / 2 - 5)) {
                rotZ = -((float) size / 2 - 5);
            }
            if (rotX > (float) size / 2 - 5.0f) {
                rotX = (float) size / 2 - 5;
            } else if (rotX < -((float) size / 2 - 5)) {
                rotX = -((float) size / 2 - 5.0f);
            }

            RenderUtils.drawRect(58.5F + rotX, (posY + 59F)+rotZ, 60.5F + rotX, (posY + 61F)+rotZ, FriendManager.isFriend(ent.getName()) ? ColorUtils.selectedColor : Color.WHITE.getRGB());
        }
    }

    @EventTarget
    private void onRender(EventRender3D event) {
        list.clear();
        for (Entity ent : mc.theWorld.loadedEntityList) {
            if (ent instanceof EntityPlayer && mc.thePlayer.getDistanceToEntity(ent) <= 60) {
                if (managers.moduleManager.getModule(AntiBot.class).isEnabled() && !AntiBot.bot.contains(ent)) {
                    list.add((EntityPlayer)ent);
                } else {
                    list.add((EntityPlayer)ent);
                }
            }
        }
    }

    List<EntityPlayer> list = new ArrayList<>();
}
