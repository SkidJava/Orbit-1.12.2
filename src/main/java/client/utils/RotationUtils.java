package client.utils;

import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationUtils {
    public static float[] getRotations(Entity ent) {
        double x = ent.posX;
        double z = ent.posZ;
        double y = ent.boundingBox.maxY - 4.0;
        return RotationUtils.getRotationFromPosition(x, z, y);
    }

    public static float[] getBlockRotations(Vec3d vec)
    {
        Entity temp = new EntitySnowball(Minecraft.getMinecraft().theWorld);
        Vec3d eyesPos = getEyesPos();
        temp.posX = (vec.xCoord - eyesPos.xCoord + 0.5D);
        temp.posY = (vec.yCoord - eyesPos.yCoord + 0.5D);
        temp.posZ = (vec.zCoord - eyesPos.zCoord + 0.5D);
        return getAngles(temp);
    }

    public static Vec3d getEyesPos()
    {
        return new Vec3d(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight(), Minecraft.getMinecraft().thePlayer.posZ);
    }
    public static float[] getAngles(Entity e)
    {
        return new float[] { getYawChangeToEntity(e) + Minecraft.getMinecraft().thePlayer.rotationYaw,
                getPitchChangeToEntity(e) + Minecraft.getMinecraft().thePlayer.rotationPitch };
    }

    public static float[] getAverageRotations(List<EntityLivingBase> targetList) {
        double posX = 0.0;
        double posY = 0.0;
        double posZ = 0.0;
        for (Entity ent : targetList) {
            posX += ent.posX;
            posY += ent.boundingBox.maxY - 2.0;
            posZ += ent.posZ;
        }
        return new float[]{RotationUtils.getRotationFromPosition(posX /= (double)targetList.size(), posZ /= (double)targetList.size(), posY /= (double)targetList.size())[0], RotationUtils.getRotationFromPosition(posX, posZ, posY)[1]};
    }

    public static float[] getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
        double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
        double yDiff = y - Minecraft.getMinecraft().thePlayer.posY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight();
        double dist = MathHelper.sqrt((xDiff * xDiff + zDiff * zDiff));
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(- Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793);
        return new float[]{yaw, pitch};
    }

    public static float getTrajAngleSolutionLow(float d3, float d1, float velocity) {
        float g = 0.006f;
        float sqrt = velocity * velocity * velocity * velocity - g * (g * (d3 * d3) + 2.0f * d1 * (velocity * velocity));
        return (float)Math.toDegrees(Math.atan(((double)(velocity * velocity) - Math.sqrt(sqrt)) / (double)(g * d3)));
    }

    public static float getNewAngle(float angle) {
        if ((angle %= 360.0f) >= 180.0f) {
            angle -= 360.0f;
        }
        if (angle < -180.0f) {
            angle += 360.0f;
        }
        return angle;
    }

    public static float getDistanceBetweenAngles(float angle1, float angle2) {
        float angle = Math.abs(angle1 - angle2) % 360.0f;
        if (angle > 180.0f) {
            angle = 360.0f - angle;
        }
        return angle;
    }

    public static float getPitchChangeToEntity(Entity entity) {
        double deltaX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
        double deltaZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double deltaY = entity.posY - 1.6D + entity.getEyeHeight() - Minecraft.getMinecraft().thePlayer.posY;
        double distanceXZ = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
//        return -MathHelper.wrapAngleTo180_float(Minecraft.getMinecraft().thePlayer.rotationPitch - (float) pitchToEntity);
        return -MathHelper.wrapDegrees(Minecraft.getMinecraft().thePlayer.rotationPitch - (float) pitchToEntity);
    }

    public static float getYawChangeToEntity(Entity entity) {
        double deltaX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
        double deltaZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double yawToEntity;
        if ((deltaZ < 0.0D) && (deltaX < 0.0D)) {
            yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
        } else {
            if ((deltaZ < 0.0D) && (deltaX > 0.0D)) {
                yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
            } else {
                yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
            }
        }
//        return MathHelper.wrapAngleTo180_float(-(Minecraft.getMinecraft().thePlayer.rotationYaw - (float) yawToEntity));
        return MathHelper.wrapDegrees(-(Minecraft.getMinecraft().thePlayer.rotationYaw - (float) yawToEntity));
    }

    public static float[] getAnglesToEntity(Entity e) {
        return new float[] { getYawChangeToEntity(e) + Minecraft.getMinecraft().thePlayer.rotationYaw,
                getPitchChangeToEntity(e) + Minecraft.getMinecraft().thePlayer.rotationPitch };
    }

    public static float[] getFacingRotations(int x, int y, int z, EnumFacing facing) {
        EntitySnowball temp = new EntitySnowball(Minecraft.getMinecraft().theWorld);
        temp.posX = x;
        temp.posY = y;
        temp.posZ = z;
        temp.posX += facing.getDirectionVec().getX() * 0.25D;
        temp.posY += facing.getDirectionVec().getY() * 0.25D;
        temp.posZ += facing.getDirectionVec().getZ() * 0.25D;
        return getAnglesToEntity(temp);
    }

    public static void jitterEffect(Random rand)
    {
        if (rand.nextBoolean())
        {
            if (rand.nextBoolean())
            {
                EntityPlayerSP tmp20_17 = Minecraft.getMinecraft().thePlayer;
                tmp20_17.rotationPitch = ((float)(tmp20_17.rotationPitch - rand.nextFloat() * 0.8D));
            }
            else
            {
                EntityPlayerSP tmp48_45 = Minecraft.getMinecraft().thePlayer;
                tmp48_45.rotationPitch = ((float)(tmp48_45.rotationPitch + rand.nextFloat() * 0.8D));
            }
        }
        else if (rand.nextBoolean())
        {
            EntityPlayerSP tmp83_80 = Minecraft.getMinecraft().thePlayer;
            tmp83_80.rotationYaw = ((float)(tmp83_80.rotationYaw - rand.nextFloat() * 0.8D));
        }
        else
        {
            EntityPlayerSP tmp111_108 = Minecraft.getMinecraft().thePlayer;
            tmp111_108.rotationYaw = ((float)(tmp111_108.rotationYaw + rand.nextFloat() * 0.8D));
        }
    }

    public static float[] aimAtBlock(BlockPos pos)
    {
        EnumFacing[] arrenumFacing = EnumFacing.values();
        int n = arrenumFacing.length;
        int n2 = 0;
        float yaw = 1.0F;
        float pitch = 1.0F;
        if (n2 <= n)
        {
            EnumFacing side = arrenumFacing[n2];
            BlockPos neighbor = pos.offset(side);
            EnumFacing side2 = side.getOpposite();
            Vec3d hitVec = new Vec3d(neighbor).addVector(0.5D, 0.5D, 0.5D).add(new Vec3d(side2.getDirectionVec()).scale(0.5D).normalize());

            yaw = getNeededRotations(hitVec)[0];
            pitch = getNeededRotations(hitVec)[1];
            if (Minecraft.getMinecraft().theWorld.getBlockState(neighbor).getBlock().canCollideCheck(Minecraft.getMinecraft().theWorld.getBlockState(pos), false)) {
                return new float[] { yaw, pitch };
            }
            hitVec = new Vec3d(pos).addVector(0.5D, 0.5D, 0.5D).add(new Vec3d(side.getDirectionVec()).scale(0.5D).normalize());
            yaw = getNeededRotations(hitVec)[0];
            pitch = getNeededRotations(hitVec)[1];
            return new float[] { yaw, pitch };
        }
        return new float[] { 1.0F, 1.0F };
    }

    private static float[] getNeededRotations(Vec3d vec) {
        Vec3d eyesPos = getEyesPos();
        double diffX = vec.xCoord - eyesPos.xCoord + 0.5D;
        double diffY = vec.yCoord - eyesPos.yCoord + 0.5D;
        double diffZ = vec.zCoord - eyesPos.zCoord + 0.5D;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
        float pitch = (float) -(Math.atan2(diffY, diffXZ) * 180.0D / 3.141592653589793D);
        return new float[]{MathHelper.wrapDegrees(yaw),
            Minecraft.getMinecraft().gameSettings.keyBindJump.isPressed() ? 90.0F :
                MathHelper.wrapDegrees(pitch)};
    }

    public static float getDirection()
    {
        float var1 = Minecraft.getMinecraft().thePlayer.rotationYaw;
        if (Minecraft.getMinecraft().thePlayer.moveY < 0.0F) {
            var1 += 180.0F;
        }
        float forward = 1.0F;
        if (Minecraft.getMinecraft().thePlayer.moveY < 0.0F) {
            forward = -0.5F;
        } else if (Minecraft.getMinecraft().thePlayer.moveY > 0.0F) {
            forward = 0.5F;
        } else {
            forward = 1.0F;
        }
        if (Minecraft.getMinecraft().thePlayer.moveStrafing > 0.0F) {
            var1 -= 90.0F * forward;
        }
        if (Minecraft.getMinecraft().thePlayer.moveStrafing < 0.0F) {
            var1 += 90.0F * forward;
        }
        var1 *= 0.017453292F;
        return var1;
    }
}
