package client.event.events.move;

import client.event.Event;
import net.minecraft.entity.MoverType;

public class EventMove extends Event {

    private MoverType moverType;
    private double x, y, z;

    public EventMove(MoverType moverType, double x, double y, double z) {
        this.moverType = moverType;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX()
    {
        return this.x;
    }

    public double getY()
    {
        return this.y;
    }

    public double getZ()
    {
        return this.z;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public void setZ(double z)
    {
        this.z = z;
    }
}
