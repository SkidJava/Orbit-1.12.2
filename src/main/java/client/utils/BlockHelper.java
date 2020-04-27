package client.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public final class BlockHelper {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static Block getBlockAtPos(BlockPos inBlockPos) {
        IBlockState s = mc.theWorld.getBlockState(inBlockPos);
        return s.getBlock();
    }

    public static float[] rotations(BlockPos block) {
        double x = (double) block.getX() + 0.5D - mc.thePlayer.posX;
        double y = (double) block.getZ() + 0.5D - mc.thePlayer.posZ;
        double z = (double) block.getY() + 0.5D - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());
        double var14 = (double) MathHelper.sqrt(x * x + y * y);
        float yaw = (float) (Math.atan2(y, x) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) (-(Math.atan2(z, var14) * 180.0D / Math.PI));
        return new float[] { yaw, pitch };
    }

    public static boolean isInLiquid() {
        boolean inLiquid = false;
        int y = (int) mc.thePlayer.boundingBox.minY;

        for (int x = MathHelper.floor(mc.thePlayer.boundingBox.minX); x < MathHelper
                .floor(mc.thePlayer.boundingBox.maxX) + 1; ++x) {
            for (int z = MathHelper.floor(mc.thePlayer.boundingBox.minZ); z < MathHelper
                    .floor(mc.thePlayer.boundingBox.maxZ) + 1; ++z) {
                Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();

                if (block != null && !(block instanceof BlockAir)) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }

                    inLiquid = true;
                }
            }
        }

        return inLiquid;
    }

    public static boolean isInLiquid(Entity entity) {
        if (entity == null)
            return false;
        boolean inLiquid = false;
        final int y = (int) entity.getEntityBoundingBox().minY;
        for (int x = MathHelper.floor(entity.getEntityBoundingBox().minX); x < MathHelper
                .floor(mc.thePlayer.getEntityBoundingBox().maxX) + 1; x++) {
            for (int z = MathHelper.floor(entity.getEntityBoundingBox().minZ); z < MathHelper
                    .floor(entity.getEntityBoundingBox().maxZ) + 1; z++) {
                final Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != null && !(block instanceof BlockAir)) {
                    if (!(block instanceof BlockLiquid))
                        return false;
                    inLiquid = true;
                }
            }
        }
        return inLiquid || mc.thePlayer.isInWater();
    }

    public static boolean isOnIce() {
        boolean onIce = false;
        int y = (int) (mc.thePlayer.boundingBox.minY - 1.0D);

        for (int x = MathHelper.floor(mc.thePlayer.boundingBox.minX); x < MathHelper
                .floor(mc.thePlayer.boundingBox.maxX) + 1; ++x) {
            for (int z = MathHelper.floor(mc.thePlayer.boundingBox.minZ); z < MathHelper
                    .floor(mc.thePlayer.boundingBox.maxZ) + 1; ++z) {
                Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();

                if (block != null && !(block instanceof BlockAir)
                        && (block instanceof BlockPackedIce || block instanceof BlockIce)) {
                    onIce = true;
                }
            }
        }

        return onIce;
    }

    public static boolean isOnLadder() {
        boolean onLadder = false;
        int y = (int) (mc.thePlayer.boundingBox.minY - 1.0D);

        for (int x = MathHelper.floor(mc.thePlayer.boundingBox.minX); x < MathHelper
                .floor(mc.thePlayer.boundingBox.maxX) + 1; ++x) {
            for (int z = MathHelper.floor(mc.thePlayer.boundingBox.minZ); z < MathHelper
                    .floor(mc.thePlayer.boundingBox.maxZ) + 1; ++z) {
                Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();

                if (block != null && !(block instanceof BlockAir)) {
                    if (!(block instanceof BlockLadder) && !(block instanceof BlockLadder)) {
                        return false;
                    }

                    onLadder = true;
                }
            }
        }

        if (!onLadder && !mc.thePlayer.isOnLadder()) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isOnLiquid() {
        boolean onLiquid = false;
        int y = (int) (mc.thePlayer.boundingBox.minY - 0.01D);

        for (int x = MathHelper.floor(mc.thePlayer.boundingBox.minX); x < MathHelper
                .floor(mc.thePlayer.boundingBox.maxX) + 1; ++x) {
            for (int z = MathHelper.floor(mc.thePlayer.boundingBox.minZ); z < MathHelper
                    .floor(mc.thePlayer.boundingBox.maxZ) + 1; ++z) {
                Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();

                if (block != null && !(block instanceof BlockAir)) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }

                    onLiquid = true;
                }
            }
        }

        return onLiquid;
    }

    public static float[] getBlockRotations(double x, double y, double z) {
        double var4 = x - mc.thePlayer.posX + 0.5D;
        double var6 = z - mc.thePlayer.posZ + 0.5D;

        double var8 = y - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - 1.0D);
        double var14 = MathHelper.sqrt(var4 * var4 + var6 * var6);
        float var12 = (float) (Math.atan2(var6, var4) * 180.0D / 3.141592653589793D) - 90.0F;

        return new float[] { var12, (float) -(Math.atan2(var8, var14) * 180.0D / 3.141592653589793D) };
    }

}