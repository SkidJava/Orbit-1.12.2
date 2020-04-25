package client.module.movement;

import client.event.EventTarget;
import client.event.events.move.EventMove;
import client.module.Module;
import org.lwjgl.input.Keyboard;

public class DepthStrider extends Module {
    public DepthStrider() {
        super("DepthStrider", Keyboard.KEY_NONE, Category.MOVEMENT);
    }
    int waitTicks;

    @EventTarget
    private void onMove(EventMove event) {
        if (mc.thePlayer.isInWater()) {
            ++this.waitTicks;
            if (this.waitTicks == 4) {
                double forward = mc.thePlayer.movementInput.moveForward;
                double strafe = mc.thePlayer.movementInput.moveStrafe;
                float yaw = mc.thePlayer.rotationYaw;
                if (forward == 0.0 && strafe == 0.0) {
                    event.setX(0.0);
                    event.setZ(0.0);
                } else {
                    if (forward != 0.0) {
                        if (strafe > 0.0) {
                            strafe = 1.0;
                            yaw += ((forward > 0.0) ? -45 : 45);
                        } else if (strafe < 0.0) {
                            yaw += ((forward > 0.0) ? 45 : -45);
                        }
                        strafe = 0.0;
                        if (forward > 0.0) {
                            forward = 1.0;
                        } else if (forward < 0.0) {
                            forward = -1.0;
                        }
                    }
                    event.setX(forward * 0.4000000059604645 * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * 0.4000000059604645 * Math.sin(Math.toRadians(yaw + 90.0f)));
                    event.setZ(forward * 0.4000000059604645 * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * 0.4000000059604645 * Math.cos(Math.toRadians(yaw + 90.0f)));
                }
            }
            if (this.waitTicks >= 5) {
                double forward = mc.thePlayer.movementInput.moveForward;
                double strafe = mc.thePlayer.movementInput.moveStrafe;
                float yaw = mc.thePlayer.rotationYaw;
                if (forward == 0.0 && strafe == 0.0) {
                    event.setX(0.0);
                    event.setZ(0.0);
                } else {
                    if (forward != 0.0) {
                        if (strafe > 0.0) {
                            strafe = 1.0;
                            yaw += ((forward > 0.0) ? -45 : 45);
                        } else if (strafe < 0.0) {
                            yaw += ((forward > 0.0) ? 45 : -45);
                        }
                        strafe = 0.0;
                        if (forward > 0.0) {
                            forward = 1.0;
                        } else if (forward < 0.0) {
                            forward = -1.0;
                        }
                    }
                    event.setX(forward * 0.30000001192092896 * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * 0.30000001192092896 * Math.sin(Math.toRadians(yaw + 90.0f)));
                    event.setZ(forward * 0.30000001192092896 * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * 0.30000001192092896 * Math.cos(Math.toRadians(yaw + 90.0f)));
                }
                this.waitTicks = 0;
            }
        }
    }
}
