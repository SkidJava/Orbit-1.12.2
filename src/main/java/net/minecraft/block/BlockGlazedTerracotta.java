package net.minecraft.block;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockGlazedTerracotta extends BlockHorizontal
{
    public BlockGlazedTerracotta(EnumDyeColor p_i47400_1_)
    {
        super(Material.ROCK, MapColor.func_193558_a(p_i47400_1_));
        this.setHardness(1.4F);
        this.setSoundType(SoundType.STONE);
        String s = p_i47400_1_.getUnlocalizedName();

        if (s.length() > 1)
        {
            String s1 = s.substring(0, 1).toUpperCase() + s.substring(1);
            this.setUnlocalizedName("glazedTerracotta" + s1);
        }

        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING);
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the theWorld, to allow for adjustments to the
     * IBlockstate
     */
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | state.getValue(FACING).getHorizontalIndex();
        return i;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

    public EnumPushReaction getMobilityFlag(IBlockState state)
    {
        return EnumPushReaction.PUSH_ONLY;
    }
}
