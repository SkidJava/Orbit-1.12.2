package client.module.movement.speed.components;

import client.event.EventTarget;
import client.event.events.move.EventPreMotionUpdates;
import client.module.movement.fly.Fly;
import client.module.movement.speed.Speed;
import client.utils.PlayerUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.Timer;

public class AACHop extends Speed {
    public AACHop() {
        super("AACHop", false);
    }

    @EventTarget
    private void onUpdate(EventPreMotionUpdates event) {
        if (managers.moduleManager.getModule(Fly.class).isEnabled()) return;

        if (PlayerUtil.isMoving(mc.thePlayer)) {
            EntityPlayerSP player;
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump();
                player = mc.thePlayer;
                player.motionX *= 1.001D;
                player = mc.thePlayer;
                player.motionZ *= 1.001D;
                mc.thePlayer.motionY = 0.41D;
                mc.timer = new Timer(20 * 1.01F);
            } else {
                player = mc.thePlayer;
                player.motionX *= 1.007D;
                player = mc.thePlayer;
                player.motionZ *= 1.007D;
                mc.timer = new Timer(20 * 1.044F);
            }
        } else {
            mc.timer = new Timer(20);
        }
    }
}
