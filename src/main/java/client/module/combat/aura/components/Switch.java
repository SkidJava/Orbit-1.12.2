package client.module.combat.aura.components;

import client.event.EventTarget;
import client.event.events.move.EventPostMotionUpdates;
import client.event.events.move.EventPreMotionUpdates;
import client.event.events.update.EventUpdate;
import client.module.combat.aura.Aura;
import client.setting.Setting;
import client.utils.RotationUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;

import java.util.List;

public class Switch extends Aura {
    public Switch() {
        super("Switch", false, true);
    }

    @EventTarget
    private void onUpdate(final EventPreMotionUpdates event) {
        List<Entity> entityList = getEntityList();
        if (entityList != null) {
            double delay = managers.settingManager.getSetting(Aura.class, "Delay").getCurrentValue();
            currentTarget = this.closeEntity();
            long timerSwich = 0L;
            if (delay <= 1D) {
                timerSwich = 50L;
            } else if (delay <= 2D) {
                timerSwich = 150L;
            } else if (delay <= 3D) {
                timerSwich = 350L;
            } else if (delay <= 4D) {
                timerSwich = 750L;
            } else if (delay <= 5D) {
                timerSwich = 950L;
            }

            if (currentTarget != null) {
                float[] rotations = RotationUtils.getRotations(currentTarget);
                event.yaw = rotations[0];
                event.pitch = rotations[1];
                playerAttacked = true;
            }
        }
    }

    @EventTarget
    private void onUpdatePost(EventPostMotionUpdates event) {
        long timer = (long) (1000L/managers.settingManager.getSetting(Aura.class, "Delay").getCurrentValue());
        if(currentTarget == null) return;
        if (this.playerAttacked) {
            if (!isValidEntity(currentTarget)) return;
            if (timerSwitch.hasReached(timer)) {
                attack((EntityLivingBase) currentTarget, managers.settingManager.getSetting(Aura.class, "Critical").getBooleanValue());
                timerSwitch.reset();
            }
        }
        if (currentTarget.isDead) this.playerAttacked = true;
    }
}
