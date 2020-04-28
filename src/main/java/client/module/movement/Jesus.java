package client.module.movement;

import client.event.EventTarget;
import client.event.events.move.EventPreMotionUpdates;
import client.event.events.render.BoundingBoxEvent;
import client.event.events.render.EventRender2D;
import client.event.events.update.PrePacketSendEvent;
import client.module.Module;
import client.utils.LiquidUtils;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.input.Keyboard;

public class Jesus extends Module {
    public Jesus() {
        super("Jesus", Keyboard.KEY_NONE, Category.MOVEMENT);
    }
    public static boolean shouldOffsetPacket;

    @EventTarget
    private void onUpdate(EventPreMotionUpdates event) {
        if (LiquidUtils.isInLiquid() && this.mc.thePlayer.isInsideOfMaterial(Material.AIR) && !this.mc.thePlayer.isSneaking()) {
            this.mc.thePlayer.motionY = 0.085;
        }
        if (!LiquidUtils.isOnLiquid() || LiquidUtils.isInLiquid() || !this.shouldSetBoundingBox()) {
            shouldOffsetPacket = true;
        }
    }

    @EventTarget
    private void onBoundingBox(BoundingBoxEvent event) {
        if (!LiquidUtils.isInLiquid() && event.block instanceof BlockLiquid && this.mc.theWorld.getBlockState(event.pos).getBlock() instanceof BlockLiquid && (Integer)this.mc.theWorld.getBlockState(event.pos).getValue((IProperty)BlockLiquid.LEVEL) == 0 && this.shouldSetBoundingBox() && (double)(event.pos.getY() + 1) <= this.mc.thePlayer.boundingBox.minY) {
            event.boundingBox = new AxisAlignedBB((double)event.pos.getX(), (double)event.pos.getY(), (double)event.pos.getZ(), (double)(event.pos.getX() + 1), (double)(event.pos.getY() + 1), (double)(event.pos.getZ() + 1));
        }
    }

    @EventTarget
    private void onPacketSend(PrePacketSendEvent event) {
        if (event.packet instanceof CPacketPlayer && LiquidUtils.isOnLiquid()) {
            CPacketPlayer packet = (CPacketPlayer)event.packet;
            boolean bl = Jesus.shouldOffsetPacket = !shouldOffsetPacket;
            if (shouldOffsetPacket) {
                packet.y -= 1.0E-6;
            }
        }
    }

    private boolean shouldSetBoundingBox() {
        if (!this.mc.thePlayer.isSneaking() && this.mc.thePlayer.fallDistance < 4.0f) {
            return true;
        }
        return false;
    }

    @Override
    public void onDisable() {
        shouldOffsetPacket = false;
        super.onDisable();
    }
}
