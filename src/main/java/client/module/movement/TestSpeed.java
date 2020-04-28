package client.module.movement;

import client.event.EventTarget;
import client.event.events.move.EventPreMotionUpdates;
import client.module.Module;
import net.minecraft.util.Timer;
import org.lwjgl.input.Keyboard;

public class TestSpeed extends Module {
    public TestSpeed() {
        super("TestSpeed", Keyboard.KEY_NONE, Category.MOVEMENT);
    }

    @EventTarget
    private void onUpdate(EventPreMotionUpdates event) {
        mc.thePlayer.setSprinting(true);
        if (mc.thePlayer.onGround && mc.thePlayer.moveForward > 0.17) {
            mc.thePlayer.jump();
            mc.thePlayer.setPosition(mc.thePlayer.posX + mc.thePlayer.motionX * 0.1, mc.thePlayer.posY, mc.thePlayer.posZ + mc.thePlayer.motionZ * 0.1);
            final Timer timer = this.mc.timer;
            Timer.timerSpeed = 50.11f;
            mc.thePlayer.distanceWalkedOnStepModified = 44.0f;

        }
        else {
            mc.thePlayer.distanceWalkedOnStepModified = 44.0f;
            mc.thePlayer.cameraPitch = 0.0f;
            mc.thePlayer.setPosition(mc.thePlayer.posX + mc.thePlayer.motionX * 0.3, mc.thePlayer.posY, mc.thePlayer.posZ + mc.thePlayer.motionZ * 0.3);
            mc.thePlayer.motionY = -1.0;
            final Timer timer2 = this.mc.timer;
            Timer.timerSpeed = 50.0f;
        }
    }
}