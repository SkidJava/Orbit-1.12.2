package client.module.movement;

import client.event.EventTarget;
import client.event.events.move.EventMove;
import client.event.events.move.EventPreMotionUpdates;
import client.module.Module;
import client.module.movement.speed.Speed;
import net.minecraft.util.MovementInput;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class LongJump extends Module {
    public LongJump() {
        super("LongJump", Keyboard.KEY_NONE, Category.MOVEMENT, true);
    }
    private static final double SPEED_BASE = 0.2873D;
    private double moveSpeed;
    private double lastDist;
    private int stage;
    private int settingUpTicks;

    @EventTarget
    public void onEnable() {
        super.onEnable();
        stage = 0;
        settingUpTicks = 2;
    }

    @EventTarget
    private void onMove(EventMove event) {
        if (!this.mc.thePlayer.isCollidedHorizontally && (this.mc.thePlayer.moveForward != 0.0F || this.mc.thePlayer.moveStrafing == 0.0F)) {
            if (settingUpTicks > 0 && (this.mc.thePlayer.moveForward != 0.0F || this.mc.thePlayer.moveStrafing != 0.0F)) {
                this.moveSpeed = 0.09D;
                --settingUpTicks;
            } else if (stage != 1 || !this.mc.thePlayer.isCollidedVertically || this.mc.thePlayer.moveForward == 0.0F && this.mc.thePlayer.moveStrafing == 0.0F) {
                if (stage != 2 || !this.mc.thePlayer.isCollidedVertically || this.mc.thePlayer.moveForward == 0.0F && this.mc.thePlayer.moveStrafing == 0.0F) {
                    if (stage == 3) {
                        double difference = 0.66D * (this.lastDist - Speed.getBaseMoveSpeed());
                        this.moveSpeed = this.lastDist - difference;
                    } else {
                        this.moveSpeed = this.lastDist - this.lastDist / 159.0D;
                    }
                } else {
                    event.setY(this.mc.thePlayer.motionY = 0.415D);
                    this.moveSpeed *= 2.13D;
                }
            } else {
                this.moveSpeed = 0.6D + Speed.getBaseMoveSpeed() - 0.05D;
            }

            setMoveSpeed(event, this.moveSpeed);

            List collidingList = this.mc.theWorld.getCollisionBoxes(this.mc.thePlayer, this.mc.thePlayer.boundingBox.offset(0.0D, this.mc.thePlayer.motionY, 0.0D));
            List collidingList2 = this.mc.theWorld.getCollisionBoxes(this.mc.thePlayer, this.mc.thePlayer.boundingBox.offset(0.0D, -0.4D, 0.0D));
            if (!this.mc.thePlayer.isCollidedVertically && (collidingList.size() > 0 || collidingList2.size() > 0) && stage > 10) {
                if (stage >= 38) {
                    event.setY(this.mc.thePlayer.motionY = -0.4D);
                    stage = 0;
                    settingUpTicks = 5;
                    this.onDisable();
                } else {
                    event.setY(this.mc.thePlayer.motionY = -0.001D);
                }
            }

            if (settingUpTicks <= 0 && (this.mc.thePlayer.moveForward != 0.0F || this.mc.thePlayer.moveStrafing != 0.0F)) {
                ++stage;
            }
        } else {
            stage = 0;
            settingUpTicks = 5;
            this.onDisable();
        }
    }

    @EventTarget
    private void onUpdate(EventPreMotionUpdates event) {
        double xDist = this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX;
        double zDist = this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ;
        this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
    }

    private void setMoveSpeed(EventMove event, double speed) {
        double forward = (double) MovementInput.moveForward;
        double strafe = (double)MovementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if (forward == 0.0D && strafe == 0.0D) {
            event.setX(0.0D);
            event.setZ(0.0D);
        } else {
            if (forward != 0.0D) {
                if (strafe > 0.0D) {
                    yaw += (float)(forward > 0.0D ? -45 : 45);
                } else if (strafe < 0.0D) {
                    yaw += (float)(forward > 0.0D ? 45 : -45);
                }

                strafe = 0.0D;
                if (forward > 0.0D) {
                    forward = 1.0D;
                } else if (forward < 0.0D) {
                    forward = -1.0D;
                }
            }

            event.setX(forward * speed * Math.cos(Math.toRadians((double)(yaw + 90.0F))) + strafe * speed * Math.sin(Math.toRadians((double)(yaw + 90.0F))));
            event.setZ(forward * speed * Math.sin(Math.toRadians((double)(yaw + 90.0F))) - strafe * speed * Math.cos(Math.toRadians((double)(yaw + 90.0F))));
        }

    }
}
