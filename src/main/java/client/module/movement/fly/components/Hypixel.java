package client.module.movement.fly.components;

import client.event.EventTarget;
import client.event.events.move.EventMove;
import client.event.events.move.EventPreMotionUpdates;
import client.module.movement.fly.Fly;
import client.module.movement.speed.Speed;
import client.utils.EntityUtils;
import client.utils.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;

public class Hypixel extends Fly {
    public Hypixel() {
        super("Hypixel", false, true);
    }
    private double moveSpeed;
    private double lastDist;

    @EventTarget
    private void onUpdate(EventPreMotionUpdates event) {
        mc.timer = new Timer(20);
        if (mc.thePlayer.movementInput.jump) {
            mc.thePlayer.motionY = 0.5D;
        } else if (mc.thePlayer.movementInput.sneak) {
            mc.thePlayer.motionY = -0.5D;
        } else {
            mc.thePlayer.motionY = 0.0D;
        }
        if (PlayerUtil.isMoving(mc.thePlayer)) {
            mc.thePlayer.cameraYaw = 0;
            if (mc.thePlayer.isPotionActive(MobEffects.SPEED)) {
                PlayerUtil.setSpeed(PlayerUtil.getSpeed() + 0.005D);
            }
        } else {
            mc.thePlayer.motionX = 0.0D;
            mc.thePlayer.motionZ = 0.0D;
        }
        if ((mc.thePlayer.ticksExisted % 3 == 0) && (!EntityUtils.func2(mc.thePlayer))) {
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.001D, mc.thePlayer.posZ);
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-9D, mc.thePlayer.posZ);
        }
        if (PlayerUtil.isMoving(mc.thePlayer) && (!mc.thePlayer.onGround)) {
            mc.timer = new Timer(20 * (mc.thePlayer.ticksExisted % 2 == 0 ? 0.9F : 1.3F));
            PlayerUtil.setSpeed(mc.thePlayer.ticksExisted % 2 == 0 ? 0.28F : 0.25F);
        } else {
            mc.thePlayer.motionX = 0.0D;
            mc.thePlayer.motionZ = 0.0D;
        }
    }

    @EventTarget
    private void onMove(EventMove event) {
        mc.timer = new Timer(20);
        this.moveSpeed = this.lastDist - this.lastDist / 159.0;

        this.moveSpeed = Math.max(this.moveSpeed, Speed.getBaseMoveSpeed());
        final MovementInput movementInput = this.mc.thePlayer.movementInput;
        float forward = movementInput.moveForward;
        float strafe = movementInput.moveStrafe;
        float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        if (forward == 0.0f && strafe == 0.0f) {
            event.setX(0.0);
            event.setZ(0.0);
        } else if (forward != 0.0f) {
            if (strafe >= 1.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
                strafe = 0.0f;
            } else if (strafe <= -1.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
                strafe = 0.0f;
            }
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        final double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        final double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        final double motionX = forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz;
        final double motionZ = forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx;
        event.setX(forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz);
        event.setZ(forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx);
    }
}
