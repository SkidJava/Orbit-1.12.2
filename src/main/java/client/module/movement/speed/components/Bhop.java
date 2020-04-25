package client.module.movement.speed.components;

import client.event.EventTarget;
import client.event.events.move.EventMove;
import client.event.events.move.EventPreMotionUpdates;
import client.module.movement.fly.Fly;
import client.module.movement.speed.Speed;
import client.utils.PlayerUtil;

import java.util.List;

public class Bhop extends Speed {
    public Bhop() {
        super("Bhop", false, true);
    }
    private int stage;

    @EventTarget
    private void onMove(EventMove event) {
        if (managers.moduleManager.getModule(Fly.class).isEnabled()) return;
        if (mc.thePlayer.moveForward == 0.0f && mc.thePlayer.moveStrafing == 0.0f) {
            this.speed = defaultSpeed();
        }
        if (this.stage == 1 && mc.thePlayer.isCollidedVertically && (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f)) {
            this.speed = 2.14 * defaultSpeed() - 0.01;
        }
        if (this.stage == 2 && mc.thePlayer.isCollidedVertically && (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f)) {
            event.setY(mc.thePlayer.motionY = 0.4);
            this.speed *= 1.5563;
        } else if (this.stage == 3) {
            final double difference = 0.66 * (this.lastDist - defaultSpeed());
            this.speed = this.lastDist - difference;
        } else {
            final List collidingList = mc.theWorld.getCollisionBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0, mc.thePlayer.motionY, 0.0));
            if ((collidingList.size() > 0 || mc.thePlayer.isCollidedVertically) && this.stage > 0) {
                this.stage = ((mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) ? 1 : 0);
            }
            this.speed = this.lastDist - this.lastDist / 159.0;
        }
        this.speed = Math.max(this.speed, defaultSpeed());
        if (this.stage > 0) {
            this.setMotion(event, this.speed);
        }
        if (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) {
            ++this.stage;
        }
    }

    @EventTarget
    private void onUpdate(EventPreMotionUpdates event) {
        if (managers.moduleManager.getModule(Fly.class).isEnabled()) return;
        final double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
        final double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
        this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
    }
}
