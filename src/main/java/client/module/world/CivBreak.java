package client.module.world;

import client.event.EventTarget;
import client.event.events.misc.EventBlockBreaking;
import client.event.events.move.EventPostMotionUpdates;
import client.event.events.move.EventPreMotionUpdates;
import client.event.events.update.EventUpdate;
import client.event.events.update.PostPacketSendEvent;
import client.event.events.update.PrePacketSendEvent;
import client.module.Module;
import client.utils.BlockHelper;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;

public class CivBreak extends Module {
    public CivBreak() {
        super("CivBreak", Keyboard.KEY_NONE, Category.WORLD);
    }

    private BlockPos pos;
    public boolean isDest = true;
    public boolean sendClick = true;
    private EnumFacing side;

    public void onDisable() {
        this.pos = null;
        this.side = null;
        super.onDisable();
    }

    @EventTarget
    private void onBreakPacketSent(PrePacketSendEvent event) {
        if (event.getPacket() instanceof CPacketPlayerDigging) {
            if (((CPacketPlayerDigging) event.getPacket()).getAction() == CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                BlockPos np = ((CPacketPlayerDigging) event.getPacket()).getPosition();
                if (pos.getX() != np.getX() || pos.getY() != np.getY() || pos.getZ() != np.getZ()) {
                    isDest = false;
                } else {
                    isDest = true;
                }
                //this.pos = ((CPacketPlayerDigging) event.getPacket()).getPosition();
                //this.side = ((CPacketPlayerDigging) event.getPacket()).getFacing();
            }
        }
    }

    @EventTarget
    private void onDigging(EventBlockBreaking event) {
        if (event.getState() == EventBlockBreaking.EnumBlock.CLICK && !this.sendClick) {
            this.pos = event.getPos();
            this.side = event.getSide();
        }
    }

    @EventTarget
    private void onUpdate(EventUpdate event) {

    }

    @EventTarget
    private void onPreUpdate(EventPreMotionUpdates event) {
        if (this.pos != null) {
            Double distance = (double)MathHelper.sqrt(mc.thePlayer.getDistanceSq(this.pos));
            if (distance > 7) {
                if (isDest) {
                    isDest = false;
                    mc.thePlayer.swingArm(mc.thePlayer.swingingHand);
                    mc.getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.pos, this.side));
                }
                return;
            }
            float[] rotations = BlockHelper.getBlockRotations((double) this.pos.getX(), (double) this.pos.getY(), (double) this.pos.getZ());
            event.yaw = rotations[0];
            event.pitch = rotations[1];
        }
    }

    @EventTarget
    private void onPostUpdate(EventPostMotionUpdates event) {
        if (this.pos != null && isDest) {
            //this.mc.getConnection().sendPacket(new CPacketAnimation());
            mc.thePlayer.swingArm(mc.thePlayer.swingingHand);
            this.mc.getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, this.pos, this.side));
            this.mc.getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.pos, this.side));
            mc.playerController.clickBlock(this.pos, this.side);
        } else {
            isDest = false;
        }

        if (this.pos != null && !isDest) {
            this.sendClick = true;
            //mc.getConnection().sendPacket(new CPacketAnimation());
            mc.thePlayer.swingArm(mc.thePlayer.swingingHand);
            mc.playerController.onPlayerDamageBlock(this.pos, this.side);
            this.sendClick = false;
        }
    }
}
