package client.module.combat.aura.components;

import client.event.EventTarget;
import client.event.events.move.EventPostMotionUpdates;
import client.event.events.move.EventPreMotionUpdates;
import client.module.combat.aura.Aura;
import client.utils.RotationUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import java.util.List;

public class Single extends Aura {
    public Single() {
        super("Single", false);
    }

    @EventTarget
    private void onUpdate(final EventPreMotionUpdates event) {
        List<Entity> entityList = this.getEntityList();
        if (entityList != null) {
            currentTarget = entityList.get(entityList.size() - 1);
            float[] rotations = RotationUtils.getRotations(currentTarget);
            if (currentTarget != null) {
                event.yaw = rotations[0];
                event.pitch = rotations[1];
                this.playerAttacked = true;
            }
        }
    }

    @EventTarget
    private void onPostUpdate(EventPostMotionUpdates event) {
        long timer = (long) (1000L / managers.settingManager.getSetting(Aura.class, "Delay").getCurrentValue());
        if (this.playerAttacked) {
            if (!isValidEntity(currentTarget)) return;
            if (timerAttack.hasReached(timer)) {
                this.attack((EntityLivingBase) currentTarget, managers.settingManager.getSetting(Aura.class, "Critical").getBooleanValue());
                this.timerAttack.reset();
            }
        }
        if (currentTarget.isDead) this.playerAttacked = true;
    }
}
