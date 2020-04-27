package client.event.events.misc;

import client.event.Event;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class EventBlockBreaking extends Event {
    private EventBlockBreaking.EnumBlock state;
    private BlockPos pos;
    private EnumFacing side;

    public EventBlockBreaking(EventBlockBreaking.EnumBlock state, BlockPos pos, EnumFacing side) {
        this.side = side;
        this.state = state;
        this.pos = pos;
    }

    public void setState(EventBlockBreaking.EnumBlock state) {
        this.state = state;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    public void setSide(EnumFacing side) {
        this.side = side;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public EventBlockBreaking.EnumBlock getState() {
        return this.state;
    }

    public EnumFacing getSide() {
        return this.side;
    }

    public static enum EnumBlock {
        CLICK("CLICK", 0), DAMAGE("DAMAGE", 1), DESTROY("DESTROY", 2);
        private static final EventBlockBreaking.EnumBlock[] ENUM$VALUES = new EventBlockBreaking.EnumBlock[] { CLICK,
                DAMAGE, DESTROY };

        private EnumBlock(String var1, int var2) {
        }
    }
}
