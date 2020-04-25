package net.minecraft.world.storage;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface IPlayerFileData
{
    /**
     * Writes the thePlayer data to disk from the specified PlayerEntityMP.
     */
    void writePlayerData(EntityPlayer player);

    @Nullable

    /**
     * Reads the thePlayer data from disk into the specified PlayerEntityMP.
     */
    NBTTagCompound readPlayerData(EntityPlayer player);

    /**
     * Returns an array of usernames for which thePlayer.dat exists for.
     */
    String[] getAvailablePlayerDat();
}
