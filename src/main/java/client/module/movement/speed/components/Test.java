package client.module.movement.speed.components;

import client.event.EventTarget;
import client.event.events.move.EventMove;
import client.event.events.move.EventPreMotionUpdates;
import client.module.movement.speed.Speed;

import client.utils.LiquidUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;


public class Test extends Speed {
    public Test() {
        super("Test", false);
    }
    private int state = 1;
    private double moveSpeed;
    private double lastDist;
    private boolean stopMotionUntilNext;
    private boolean spedUp;
    public static int wait;

    @EventTarget
    private void onMove(EventMove event) {
        if (wait != 0) {
            Speed.canStep = true;
            this.state = 1;
            wait--;
            return;
        }

        Speed.canStep = true;
        final MovementInput movementInput = Minecraft.getMinecraft().thePlayer.movementInput;
        float forward = movementInput.moveForward;
        float strafe = movementInput.moveStrafe;
        float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;

        if (forward == 0.0f && strafe == 0.0f || !mc.thePlayer.onGround || mc.thePlayer.fallDistance != 0.0f) {
            wait = 5;
            Speed.canStep = false;
            // event.x = 0.0;
            // event.z = 0.0;
            return;
        }

        boolean collideCheck = false;
        if (mc.theWorld.getCollisionBoxes(this.mc.thePlayer, this.mc.thePlayer.boundingBox.expand(0.5, 0.0, 0.5))
                .size() > 0) {
            // collideCheck = true;
        }
        if (forward != 0.0f) {
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

        Timer.timerSpeed = 50.0f;
        switch (this.state) {
            case 1:
                double baseSpeed = 0.579;
                if (this.mc.thePlayer.isPotionActive(MobEffects.SPEED)) {
                    final int amplifier = this.mc.thePlayer.getActivePotionEffect(MobEffects.SPEED).getAmplifier();
                    baseSpeed *= 0.999 - 0.029 * (amplifier + 1);
                }
                this.moveSpeed = baseSpeed;
                break;
            case 2:
                this.moveSpeed = 0.66787;
                break;
            default:
                this.moveSpeed = Speed.getBaseMoveSpeed();
                break;
        }

        if (collideCheck || this.mc.thePlayer.isSneaking()
                || (this.mc.thePlayer.moveForward == 0.0f && this.mc.thePlayer.moveStrafing == 0.0f)
                || this.mc.gameSettings.keyBindJump.isPressed() || LiquidUtils.isOnLiquid()
                || LiquidUtils.isInLiquid()) {
            Timer.timerSpeed = 50.0f;
            this.moveSpeed = Speed.getBaseMoveSpeed();
            Speed.canStep = true;
            this.state = 1;
            return;
        }

        this.moveSpeed = Math.max(this.moveSpeed, Speed.getBaseMoveSpeed());
        final double mx = Math.cos(Math.toRadians((double) (yaw + 90.0f)));
        final double mz = Math.sin(Math.toRadians((double) (yaw + 90.0f)));
        event.setX(forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz);
        event.setZ(forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx);
    }

    @EventTarget
    private void onUpdate(EventPreMotionUpdates event) {
            Speed.canStep = true;
            final double xDist = this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX;
            final double zDist = this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ;
            this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);

            if (mc.thePlayer.moveForward == 0.0f && mc.thePlayer.moveStrafing == 0.0f
                    || !mc.thePlayer.onGround && mc.thePlayer.fallDistance != 0.0f) {
                if (!mc.thePlayer.onGround && mc.thePlayer.fallDistance != 0.0f) {
                    Speed.canStep = false;
                }
                return;
            }

            switch (this.state) {
                case 1:
                    Speed.canStep = false;
                        event.y += 0.00001;
                    ++this.state;
                    break;
                case 2:
                    Speed.canStep = false;
                        event.y += 0.00002;
                    ++this.state;
                    break;
                default:
                    this.state = 1;
                    if (!this.mc.thePlayer.isSneaking()
                            && (this.mc.thePlayer.moveForward != 0.0f || this.mc.thePlayer.moveStrafing != 0.0f)
                            && !this.mc.gameSettings.keyBindJump.isPressed() && !LiquidUtils.isOnLiquid()
                            && !LiquidUtils.isInLiquid()) {
                        Speed.canStep = true;
                        this.state = 1;
                        break;
                    }
                    this.moveSpeed = Speed.getBaseMoveSpeed();
                    break;
            }
            Speed.yOffset = event.y - this.mc.thePlayer.posY;
    }
}
