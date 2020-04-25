package client.event.events.render;

import client.event.Event;
import net.minecraft.block.Block;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class BoundingBoxEvent extends Event {
    public Block block;
    public BlockPos pos;
    public AxisAlignedBB boundingBox;

    public BoundingBoxEvent(Block block, BlockPos pos, AxisAlignedBB boundingBox)
    {
        this.block = block;
        this.pos = pos;
        this.boundingBox = boundingBox;
    }
}
