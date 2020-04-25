package client.module.movement.speed;

import client.event.events.move.EventMove;
import client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.init.MobEffects;
import net.minecraft.util.MovementInput;
import org.lwjgl.input.Keyboard;

public class Speed extends Module {
    public Speed() {
        super("Speed", Keyboard.KEY_NONE, Category.MOVEMENT, true, true);
    }

    public Speed(String name, boolean visible, boolean enabled) {
        super(name, visible, enabled);
    }
    public Speed(String name, boolean visible) {
        super(name, visible);
    }
    public double speed;
    public double lastDist;

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(MobEffects.SPEED)) {
            final int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(MobEffects.SPEED).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }

    public double defaultSpeed() {
        double baseSpeed = 0.2873;
        if (mc.thePlayer.isPotionActive(MobEffects.SPEED)) {
            final int amplifier = mc.thePlayer.getActivePotionEffect(MobEffects.SPEED).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }

    public void setMotion(final EventMove me, final double speed) {
        double forward = MovementInput.moveForward;
        double strafe = MovementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            me.setX(0.0);
            me.setZ(0.0);
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            me.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            me.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
        }
    }
}
