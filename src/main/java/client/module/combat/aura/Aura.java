package client.module.combat.aura;

import client.event.EventTarget;
import client.event.events.render.EventRender2D;
import client.manager.managers.FriendManager;
import client.manager.managers.ModuleManager;
import client.module.Module;
import client.module.combat.antibot.AntiBot;
import client.setting.Setting;
import client.utils.TeamUtils;
import client.utils.TimeHelper;
import client.utils.Timer;
import client.utils.render.RenderUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import optifine.MathUtils;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Aura extends Module {
    protected double blockRange;
    protected TimeHelper timerSwitch, timerAttack;
    protected static Entity currentTarget;
    protected boolean playerAttacked = false;
    protected static EntityLivingBase pseudoTarget;

    public Aura(String name, boolean visible, boolean enabled) {
        super(name, visible, enabled);
    }

    public Aura(String name, boolean visible) {
        super(name, visible);
    }

    public Aura() {
        super("Aura", Keyboard.KEY_R, Category.COMBAT, true, true);

        managers.settingManager.addSetting(new Setting(this, "Draw Info", true));
        managers.settingManager.addSetting(new Setting(this, "Players", true));
        managers.settingManager.addSetting(new Setting(this, "Monsters", false));
        managers.settingManager.addSetting(new Setting(this, "Mobs", false));
        managers.settingManager.addSetting(new Setting(this, "Friend", false));
        managers.settingManager.addSetting(new Setting(this, "Critical", true));
        managers.settingManager.addSetting(new Setting(this, "Teammate", false));

        managers.settingManager.addSetting(new Setting(this, "Delay", 6.0F, 0.0F, 20.0F, false));
        managers.settingManager.addSetting(new Setting(this, "Range", 4.75F, 0.0F, 6.0F, false));
        managers.settingManager.addSetting(new Setting(this, "Block Range", 4F, 0.0F, 6.0F, true));
    }

    public void onEnable() {
        super.onEnable();

        this.timerSwitch = new TimeHelper();
        this.timerAttack = new TimeHelper();
        currentTarget = null;
    }


    @EventTarget
    public void onRender2D(final EventRender2D event) {
        if (managers.settingManager.getSetting(Aura.class, "Draw Info").getBooleanValue() && managers.settingManager.getSetting(Aura.class, "Players").getBooleanValue()) {
            String name = "Name: " + currentTarget.getDisplayName().getFormattedText();
            String hp ="Health: §a"+ ((int) ((EntityLiving) currentTarget).getHealth() ) + "§7/§r" + ((int) ((EntityLiving) currentTarget).getMaxHealth() );
            int baseX = event.width/2 + 15;
            int baseY = event.height/2 + mc.fontRendererObj.FONT_HEIGHT/2;

            RenderUtils.drawBorderedRect(baseX-5, baseY,
                event.width/2 + mc.fontRendererObj.getStringWidth(name) + 45,baseY + mc.fontRendererObj.FONT_HEIGHT*2 + 7 + 25, 0.7F, -1610612736, Color.WHITE.getRGB());
            mc.fontRendererObj.drawStringWithShadow(name, baseX+25, baseY + 5, -1);
            mc.fontRendererObj.drawStringWithShadow(hp, baseX+25, baseY + mc.fontRendererObj.FONT_HEIGHT*2 - 5, -1);

            GuiInventory.drawEntityOnScreen(baseX + 10, baseY + 44, 20, 0, 0, (EntityLivingBase) currentTarget);
            RenderUtils.drawRect(baseX+25, baseY + mc.fontRendererObj.FONT_HEIGHT*3, event.width/2 + mc.fontRendererObj.getStringWidth(name), baseY + mc.fontRendererObj.FONT_HEIGHT*4, 65280);
        }
    }

    protected Entity closeEntity() {
        Entity close = null;
        for (final Object o : getEntityList()) {
            final Entity e = (Entity) o;
            if (e instanceof Entity && isValidEntity(e) && (close == null || mc.thePlayer.getDistanceToEntity(e) < mc.thePlayer.getDistanceToEntity(close))) {
                close = e;
            }
        }
        return close;
    }

    protected List<Entity> getEntityList() {
        List<Entity> entityList = new ArrayList<>();
        for (Object o : mc.theWorld.getLoadedEntityList()) {
            if (o instanceof Entity) {
                Entity e = (Entity) o;
                if (managers.settingManager.getSetting(Aura.class, "Mode").getCurrentOption().equalsIgnoreCase("Switch")) {
                    if (entityList.contains(e) || !this.isValidEntity(e)) continue;
                    entityList.add(e);
                } else {
                    if (entityList.contains(e) || entityList.size() >= 1 || !this.isValidEntity(e)) continue;
                    entityList.add(e);
                }
            }
        }
        return entityList;
    }

    public boolean isValidEntity(Entity target) {
        boolean attackDist = mc.thePlayer.getDistanceToEntity(target) <= managers.settingManager.getSetting(Aura.class, "Range").getCurrentValue();
        if (target.isEntityAlive() && mc.thePlayer.getHealth() > 0.0F) {
            if (managers.moduleManager.getModule(AntiBot.class).isEnabled() && AntiBot.bot.contains(target)) return false;
            if (target instanceof EntityMob) return managers.settingManager.getSetting(Aura.class, "Monsters").getBooleanValue() && attackDist;
            if (target instanceof EntityAnimal || target instanceof EntityBat || target instanceof EntitySquid || target instanceof EntityVillager) return managers.settingManager.getSetting(Aura.class, "Mobs").getBooleanValue() && attackDist;
            if (target instanceof EntityPlayer) {
                if ((!managers.settingManager.getSetting(Aura.class, "Friend").getBooleanValue() && FriendManager.isFriend( ((EntityPlayer)target).getName() )) ||
                    (!managers.settingManager.getSetting(Aura.class, "Teammate").getBooleanValue() && TeamUtils.isTeammate(target) )) return false;
                return managers.settingManager.getSetting(Aura.class, "Players").getBooleanValue() && target != mc.thePlayer && attackDist;
            }
        }
        return false;
    }

    protected void attack(final EntityLivingBase ent, final boolean crit) {
        mc.thePlayer.swingArm(EnumHand.MAIN_HAND);
        Entity hit = mc.objectMouseOver.entityHit;
        mc.getConnection().getNetworkManager().sendPacket(new CPacketUseEntity(ent));
        try {
            if (mc.thePlayer.getHeldItem(EnumHand.MAIN_HAND).isItemEnchanted()) {
                mc.thePlayer.onEnchantmentCritical(ent);
            }
        } catch (Exception localException) {

        }
    }
}
