package client.module.movement;

import client.event.EventTarget;
import client.event.events.move.EventPostMotionUpdates;
import client.event.events.move.EventPreMotionUpdates;
import client.event.events.render.EventRender2D;
import client.module.Module;
import client.utils.PlayerUtil;
import client.utils.RotationUtils;
import client.utils.Timer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.List;

public class ScaffoldWalk extends Module {
    public ScaffoldWalk() {
        super("ScaffoldWalk", Keyboard.KEY_NONE, Category.MOVEMENT);
    }

    @EventTarget
    public void onEnable() {
        super.onEnable();
        this.invalid = Arrays.asList(Blocks.AIR, Blocks.WATER, Blocks.FIRE, Blocks.FLOWING_WATER, Blocks.LAVA,
            Blocks.FLOWING_LAVA, Blocks.ICE, Blocks.PACKED_ICE, Blocks.NOTEBLOCK, Blocks.CHEST, Blocks.ANVIL, Blocks.ENCHANTING_TABLE);
    }

    @EventTarget
    private void onRender2D(EventRender2D event) {
        mc.fontRendererObj.drawStringWithShadow("§r" + getTotalBlocks(), event.width/2 + 7, event.height/2 - mc.fontRendererObj.FONT_HEIGHT/2, -1);
    }

    @EventTarget
    private void onPreUpdate(EventPreMotionUpdates event) {
        int block = blockInHotbar();
        if (block == 0) {
            if (grabBlock()) {
                block = blockInHotbar();
                if (block != 0) {}
            } else {
                return;
            }
        }
        int tempSlot = getBlockSlot();
        this.blockData = null;
        this.slot = -1;
        if (tempSlot == -1) {
            return;
        }
        if (!Minecraft.getMinecraft().gameSettings.keyBindJump.isPressed()) {
            this.timer.reset();
        }
        if (tempSlot != -1) {
            double forward = MovementInput.moveForward;
            double strafe = MovementInput.moveStrafe;

            double x2 = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
            double z2 = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));

            boolean hypixel = true;
            double xOffset = MovementInput.moveForward * (hypixel ? 0.0D : mc.gameSettings.keyBindJump.isPressed() ? 1.0D : this.offsetToUse * x2);
            double zOffset = MovementInput.moveForward * (hypixel ? 0.0D : mc.gameSettings.keyBindJump.isPressed() ? 1.0D : this.offsetToUse * z2);

            double x = mc.thePlayer.posX + (hypixel ? 0.0D : mc.gameSettings.keyBindJump.isPressed() ? 0.1D : xOffset);
            double y = mc.thePlayer.posY - 0.1D;
            double z = mc.thePlayer.posZ + (hypixel ? 0.0D : mc.gameSettings.keyBindJump.isPressed() ? 0.1D : zOffset);
            float yaww = mc.thePlayer.rotationYaw;
            x += forward * 0.45D * Math.cos(Math.toRadians(yaww + 90.0F)) + strafe * 0.45D * Math.sin(Math.toRadians(yaww + 90.0F));
            z += forward * 0.45D * Math.sin(Math.toRadians(yaww + 90.0F)) - strafe * 0.45D * Math.cos(Math.toRadians(yaww + 90.0F));

            BlockPos blockBelow1 = new BlockPos(x, y, z);
            if (mc.theWorld.getBlockState(blockBelow1).getBlock() == Blocks.AIR) {
                this.offsetToUse = 0.0D;
                this.blockData = getBlockData(blockBelow1);
                this.slot = tempSlot;
                if (this.blockData != null) {
                    float yaw = RotationUtils.aimAtBlock(this.blockData.position)[0];
                    float pitch = RotationUtils.aimAtBlock(this.blockData.position)[1];

                    event.yaw = yaw;
                    event.pitch = pitch;
                    this.oldYaw = yaw;
                    this.oldPitch = pitch;
                }
            }
        }
    }

    @EventTarget
    private void onPostUpdate(EventPostMotionUpdates event) {
        if (this.blockData != null) {
            boolean possible;
            boolean bl = possible = mc.thePlayer.inventory.currentItem != this.slot ? true : false;
            boolean wasSprintng = mc.thePlayer.isSprinting();
            if (wasSprintng) {
                mc.thePlayer.setSprinting(true);
            }
            int oldSlot = mc.thePlayer.inventory.currentItem;
            if (possible) {
                mc.thePlayer.connection.sendPacket(new CPacketHeldItemChange(this.slot));
            }
            // mc.thePlayer.inventoryContainer.getSlot(36 + this.slot).getStack(),
            if (mc.playerController.processRightClickBlock(mc.thePlayer, mc.theWorld, this.blockData.position, this.blockData.face,
                new Vec3d(this.blockData.position.getX(), this.blockData.position.getY(), this.blockData.position.getZ()), EnumHand.MAIN_HAND) == EnumActionResult.SUCCESS) {
                if (!PlayerUtil.isMoving(mc.thePlayer)) {
                    if (mc.thePlayer.movementInput.jump) {
                        mc.thePlayer.motionY = 0.4199382043D;
                        mc.thePlayer.motionX = 0.0D;
                        mc.thePlayer.motionZ = 0.0D;
                        if (Timer.hasReached(1500L)) {
                            mc.thePlayer.motionY = -0.279929103D;
                            this.towertimer.reset();
                            if (Timer.hasReached(3L)) {
                                mc.thePlayer.motionY = 0.4199382043D;
                            }
                        }
                    }
                }
//                    mc.thePlayer.swingItem();
                mc.getConnection().getNetworkManager().sendPacket(new CPacketAnimation());
                return;
            }
            if (possible) {
                mc.thePlayer.connection.sendPacket(new CPacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            }
            return;
        }
    }

    private boolean grabBlock() {
        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if ((stack != null) && ((stack.getItem() instanceof ItemBlock))) {
                for (int x = 36; x < 45; x++) {
                    try {
                        Item localItem = mc.thePlayer.inventoryContainer.getSlot(x).getStack().getItem();
                    } catch (NullPointerException ex) {
                        swap(i, x - 36);
                        return true;
                    }
                }
                swap(i, 1);
                return true;
            }
        }
        return false;
    }

    private int getBlockSlot() {
        int i = 36;
        while (i < 45) {
            ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if ((itemStack != null) && ((itemStack.getItem() instanceof ItemBlock)) && (itemStack.stackSize >= 1)) {
                return i - 36;
            }
            i++;
        }
        return -1;
    }

    private void swap(int slot, int hotbarNum) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, ClickType.QUICK_MOVE, mc.thePlayer);
    }

    private int blockInHotbar() {
        for (int i = 36; i < 45; i++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if ((stack != null) && ((stack.getItem() instanceof ItemBlock))) {
                return i;
            }
        }
        return 0;
    }

    private int getTotalBlocks() {
        int totalCount = 0;
        int i = 9;
        while (i < 45) {
            ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if ((itemStack != null) && ((itemStack.getItem() instanceof ItemBlock)) && (itemStack.stackSize >= 1)) {
                totalCount += itemStack.stackSize;
            }
            i++;
        }
        return totalCount;
    }

    private class BlockData {
        public BlockPos position;
        public EnumFacing face;

        public BlockData(BlockPos position, EnumFacing face) {
            this.position = position;
            this.face = face;
        }
    }
    private BlockData getBlockData(BlockPos pos) {
        if (!this.invalid.contains(mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains(mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains(mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains(mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!this.invalid.contains(mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        BlockPos add = pos.add(-1, 0, 0);
        if (!this.invalid.contains(mc.theWorld.getBlockState(add.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains(mc.theWorld.getBlockState(add.add(1, 0, 0)).getBlock())) {
            return new BlockData(add.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains(mc.theWorld.getBlockState(add.add(0, 0, -1)).getBlock())) {
            return new BlockData(add.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!this.invalid.contains(mc.theWorld.getBlockState(add.add(0, 0, 1)).getBlock())) {
            return new BlockData(add.add(0, 0, 1), EnumFacing.NORTH);
        }
        BlockPos add2 = pos.add(1, 0, 0);
        if (!this.invalid.contains(mc.theWorld.getBlockState(add2.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains(mc.theWorld.getBlockState(add2.add(1, 0, 0)).getBlock())) {
            return new BlockData(add2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains(mc.theWorld.getBlockState(add2.add(0, 0, -1)).getBlock())) {
            return new BlockData(add2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!this.invalid.contains(mc.theWorld.getBlockState(add2.add(0, 0, 1)).getBlock())) {
            return new BlockData(add2.add(0, 0, 1), EnumFacing.NORTH);
        }
        BlockPos add3 = pos.add(0, 0, -1);
        if (!this.invalid.contains(mc.theWorld.getBlockState(add3.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains(mc.theWorld.getBlockState(add3.add(1, 0, 0)).getBlock())) {
            return new BlockData(add3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains(mc.theWorld.getBlockState(add3.add(0, 0, -1)).getBlock())) {
            return new BlockData(add3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!this.invalid.contains(mc.theWorld.getBlockState(add3.add(0, 0, 1)).getBlock())) {
            return new BlockData(add3.add(0, 0, 1), EnumFacing.NORTH);
        }
        BlockPos add4 = pos.add(0, 0, 1);
        if (!this.invalid.contains(mc.theWorld.getBlockState(add4.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains(mc.theWorld.getBlockState(add4.add(1, 0, 0)).getBlock())) {
            return new BlockData(add4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains(mc.theWorld.getBlockState(add4.add(0, 0, -1)).getBlock())) {
            return new BlockData(add4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!this.invalid.contains(mc.theWorld.getBlockState(add4.add(0, 0, 1)).getBlock())) {
            return new BlockData(add4.add(0, 0, 1), EnumFacing.NORTH);
        }
        return null;
    }

    private int slot;
    public final Timer timer = new Timer();
    public final Timer towertimer = new Timer();
    private BlockData blockData;
    private List<Block> invalid;
    private double offsetToUse = 0.0D;
    private float oldYaw, oldPitch;
}
