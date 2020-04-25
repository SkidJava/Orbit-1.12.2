package net.minecraft.tileentity;

import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;

public interface IHopper extends IInventory
{
    /**
     * Returns the worldObj for this tileEntity.
     */
    World getWorld();

    /**
     * Gets the theWorld X position for this hopper entity.
     */
    double getXPos();

    /**
     * Gets the theWorld Y position for this hopper entity.
     */
    double getYPos();

    /**
     * Gets the theWorld Z position for this hopper entity.
     */
    double getZPos();
}
