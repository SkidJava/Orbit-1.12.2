package client.module.player.phase.components;

import client.event.Event;
import client.event.EventTarget;
import client.event.events.move.EventPostMotionUpdates;
import client.event.events.render.BoundingBoxEvent;
import client.module.player.phase.Phase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;

public class Hypixel extends Phase {
    public Hypixel() {
        super("Hypixel", false, true);
    }
    private double dist = 1.0D;
    private int reset, resetNext;

    @EventTarget
    public void onUpdate(EventPostMotionUpdates event) {
        this.reset -= 1;
        double xOff = 0.0D;
        double zOff = 0.0D;
        double mx = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
        double mz = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));

        double multiplier = 0.3D;
        double ux = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
        double uz = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
        MovementInput movementInput = mc.thePlayer.movementInput;
        double n = MovementInput.moveForward * 0.3D * ux;
        MovementInput movementInput2 = mc.thePlayer.movementInput;
        double x = n + MovementInput.moveStrafe * 0.3D * uz;
        MovementInput movementInput3 = mc.thePlayer.movementInput;
        double n2 = MovementInput.moveForward * 0.3D * uz;
        MovementInput movementInput4 = mc.thePlayer.movementInput;
        double z = n2 - MovementInput.moveStrafe * 0.3D * ux;
        if ((mc.thePlayer.isCollidedHorizontally) && (!mc.thePlayer.isOnLadder()) && (!isInsideBlock())) {
            mc.thePlayer.connection.sendPacket(new CPacketPlayer.Position(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z, false));
            for (int i = 1; i < 10; i++) {
                mc.thePlayer.connection.sendPacket((new CPacketPlayer.Position(mc.thePlayer.posX, 8.988465674311579E307D, mc.thePlayer.posZ, false)));
            }
            mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
        }
    }

    @EventTarget
    public void onCollision(BoundingBoxEvent event) {
        if ((event.boundingBox != null) && (event.boundingBox.maxY > mc.thePlayer.boundingBox.minY) && (Keyboard.isKeyDown(29))) {
            event.setCancelled(true);
        }
    }

    public boolean isInsideBlock() {
        for (int x = MathHelper.floor(mc.thePlayer.boundingBox.minX); x < MathHelper.floor(mc.thePlayer.boundingBox.maxX) + 1; x++) {
            for (int y = MathHelper.floor(mc.thePlayer.boundingBox.minY); y < MathHelper.floor(mc.thePlayer.boundingBox.maxY) + 1; y++) {
                for (int z = MathHelper.floor(mc.thePlayer.boundingBox.minZ); z < MathHelper.floor(mc.thePlayer.boundingBox.maxZ) + 1; z++) {
                    Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if ((block != null) && (!(block instanceof BlockAir))) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(mc.theWorld.getBlockState(new BlockPos(x, y, z)), mc.theWorld, new BlockPos(x, y, z));
                        if ((block instanceof BlockHopper)) {
                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                        }
                        if ((boundingBox != null) && (mc.thePlayer.boundingBox.intersectsWith(boundingBox))) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
