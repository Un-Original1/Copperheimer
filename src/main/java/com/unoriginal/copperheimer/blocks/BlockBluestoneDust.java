package com.unoriginal.copperheimer.blocks;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.unoriginal.copperheimer.Copperheimer;
import com.unoriginal.copperheimer.blocks.tile.TileEntityBattery;
import com.unoriginal.copperheimer.blocks.tile.TileEntityBluestone;
import com.unoriginal.copperheimer.init.ModBlocks;
import com.unoriginal.copperheimer.init.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class BlockBluestoneDust extends Block implements ITileEntityProvider{
    public static final PropertyEnum<BlockBluestoneDust.EnumAttachPosition> NORTH = PropertyEnum.create("north", BlockBluestoneDust.EnumAttachPosition.class);
    public static final PropertyEnum<BlockBluestoneDust.EnumAttachPosition> EAST = PropertyEnum.create("east", BlockBluestoneDust.EnumAttachPosition.class);
    public static final PropertyEnum<BlockBluestoneDust.EnumAttachPosition> SOUTH = PropertyEnum.create("south", BlockBluestoneDust.EnumAttachPosition.class);
    public static final PropertyEnum<BlockBluestoneDust.EnumAttachPosition> WEST = PropertyEnum.create("west", BlockBluestoneDust.EnumAttachPosition.class);
    public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 1); //that's right folks, no energy loss!
    private boolean canProvidePower = true;
    private final Set<BlockPos> blocksNeedingUpdate = Sets.newHashSet();
    protected static final AxisAlignedBB[] REDSTONE_WIRE_AABB = new AxisAlignedBB[] {new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 0.8125D, 0.0625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.8125D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 1.0D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 1.0D, 0.0625D, 1.0D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D)};

    public BlockBluestoneDust(String name) {
        super(Material.CIRCUITS, MapColor.AIR);
        //this.setHardness(3.0F);
        this.setRegistryName(name);
        this.setUnlocalizedName(name);
        //this.setLightLevel(0.8F);
        this.setCreativeTab(Copperheimer.COPPERTAB);
        this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, BlockBluestoneDust.EnumAttachPosition.NONE).withProperty(EAST, BlockBluestoneDust.EnumAttachPosition.NONE).withProperty(SOUTH, BlockBluestoneDust.EnumAttachPosition.NONE).withProperty(WEST, BlockBluestoneDust.EnumAttachPosition.NONE).withProperty(POWER, 0));
    }


    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return REDSTONE_WIRE_AABB[getAABBIndex(state.getActualState(source, pos))];
    }

    private static int getAABBIndex(IBlockState state)
    {
        int i = 0;
        boolean flag = state.getValue(NORTH) != BlockBluestoneDust.EnumAttachPosition.NONE;
        boolean flag1 = state.getValue(EAST) != BlockBluestoneDust.EnumAttachPosition.NONE;
        boolean flag2 = state.getValue(SOUTH) != BlockBluestoneDust.EnumAttachPosition.NONE;
        boolean flag3 = state.getValue(WEST) != BlockBluestoneDust.EnumAttachPosition.NONE;

        if (flag || flag2 && !flag && !flag1 && !flag3)
        {
            i |= 1 << EnumFacing.NORTH.getHorizontalIndex();
        }

        if (flag1 || flag3 && !flag && !flag1 && !flag2)
        {
            i |= 1 << EnumFacing.EAST.getHorizontalIndex();
        }

        if (flag2 || flag && !flag1 && !flag2 && !flag3)
        {
            i |= 1 << EnumFacing.SOUTH.getHorizontalIndex();
        }

        if (flag3 || flag1 && !flag && !flag2 && !flag3)
        {
            i |= 1 << EnumFacing.WEST.getHorizontalIndex();
        }

        return i;
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        state = state.withProperty(WEST, this.getAttachPosition(worldIn, pos, EnumFacing.WEST));
        state = state.withProperty(EAST, this.getAttachPosition(worldIn, pos, EnumFacing.EAST));
        state = state.withProperty(NORTH, this.getAttachPosition(worldIn, pos, EnumFacing.NORTH));
        state = state.withProperty(SOUTH, this.getAttachPosition(worldIn, pos, EnumFacing.SOUTH));
        return state;
    }

    private BlockBluestoneDust.EnumAttachPosition getAttachPosition(IBlockAccess worldIn, BlockPos pos, EnumFacing direction)
    {
        BlockPos blockpos = pos.offset(direction);
        IBlockState iblockstate = worldIn.getBlockState(pos.offset(direction));

        if (!canConnectTo(worldIn.getBlockState(blockpos), direction, worldIn, blockpos) && (iblockstate.isNormalCube() || (!canConnectUpwardsTo(worldIn, blockpos.down()) || !canConnectUpwardsToVerticalDust(worldIn, blockpos.down(), null))))
        {
            IBlockState iblockstate1 = worldIn.getBlockState(pos.up());

            if (!iblockstate1.isNormalCube())
            {
                boolean flag = worldIn.getBlockState(blockpos).isSideSolid(worldIn, blockpos, EnumFacing.UP) || worldIn.getBlockState(blockpos).getBlock() == Blocks.GLOWSTONE;

                if (flag && (canConnectUpwardsTo(worldIn, blockpos.up()) ||canConnectUpwardsToVerticalDust(worldIn, pos.up(),direction)  ))
                {
                    if (iblockstate.isBlockNormalCube())
                    {
                        return BlockBluestoneDust.EnumAttachPosition.UP;
                    }

                    return BlockBluestoneDust.EnumAttachPosition.SIDE;
                }

            }

            return BlockBluestoneDust.EnumAttachPosition.NONE;
        }
        else
        {
            return BlockBluestoneDust.EnumAttachPosition.SIDE;
        }
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }



    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        IBlockState downState = worldIn.getBlockState(pos.down());
        return downState.isTopSolid() || downState.getBlockFaceShape(worldIn, pos.down(), EnumFacing.UP) == BlockFaceShape.SOLID || worldIn.getBlockState(pos.down()).getBlock() == Blocks.GLOWSTONE;
    }

    private IBlockState updateSurroundingBluestone(World worldIn, BlockPos pos, IBlockState state)
    {
        List<BlockPos> list = Lists.newArrayList(this.blocksNeedingUpdate);
        this.blocksNeedingUpdate.clear();

        for (BlockPos blockpos : list)
        {
            worldIn.notifyNeighborsOfStateChange(blockpos, this, false);
        }

        return state;
    }

    private IBlockState calculateCurrentChanges(World worldIn, BlockPos pos1, BlockPos pos2, IBlockState state)
    {
        IBlockState iblockstate = state;
        int i = ((Integer)state.getValue(POWER)).intValue();
        int j = 0;
        j = this.getMaxCurrentStrength(worldIn, pos2, j);
        this.canProvidePower = false;
        int k = worldIn.isBlockIndirectlyGettingPowered(pos1);
        this.canProvidePower = true;

        if (k > 0 && k > j - 1)
        {
            j = k;
        }

        int l = 0;

        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
        {
            BlockPos blockpos = pos1.offset(enumfacing);
            boolean flag = blockpos.getX() != pos2.getX() || blockpos.getZ() != pos2.getZ();

            if (flag)
            {
                l = this.getMaxCurrentStrength(worldIn, blockpos, l);
            }

            if (worldIn.getBlockState(blockpos).isNormalCube() && !worldIn.getBlockState(pos1.up()).isNormalCube())
            {
                if (flag && pos1.getY() >= pos2.getY())
                {
                    l = this.getMaxCurrentStrength(worldIn, blockpos.up(), l);
                }
            }
            else if (!worldIn.getBlockState(blockpos).isNormalCube() && flag && pos1.getY() <= pos2.getY())
            {
                l = this.getMaxCurrentStrength(worldIn, blockpos.down(), l);
            }
        }

        if (l > j)
        {
            j = l - 1;
        }
        else if (j > 0)
        {
            --j;
        }
        else
        {
            j = 0;
        }

        if (k > j - 1)
        {
            j = k;
        }

        if (i != j)
        {
            state = state.withProperty(POWER, Integer.valueOf(j));

            if (worldIn.getBlockState(pos1) == iblockstate)
            {
                worldIn.setBlockState(pos1, state, 2);
            }

            this.blocksNeedingUpdate.add(pos1);

            for (EnumFacing enumfacing1 : EnumFacing.values())
            {
                this.blocksNeedingUpdate.add(pos1.offset(enumfacing1));
            }
        }

        return state;
    }
    private int getMaxCurrentStrength(World worldIn, BlockPos pos, int strength)
    {
        if (worldIn.getBlockState(pos).getBlock() != this)
        {
            return strength;
        }
        else
        {
            int i = ((Integer)worldIn.getBlockState(pos).getValue(POWER)).intValue();
            return i > strength ? i : strength;
        }
    }

    private void notifyWireNeighborsOfStateChange(World worldIn, BlockPos pos)
    {
        if (worldIn.getBlockState(pos).getBlock() == this)
        {
            worldIn.notifyNeighborsOfStateChange(pos, this, false);

            for (EnumFacing enumfacing : EnumFacing.values())
            {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }
        }
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote)
        {
            this.updateSurroundingBluestone(worldIn, pos, state);

            for (EnumFacing enumfacing : EnumFacing.Plane.VERTICAL)
            {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }

            for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL)
            {
                this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing1));
            }

            for (EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL)
            {
                BlockPos blockpos = pos.offset(enumfacing2);

                if (worldIn.getBlockState(blockpos).isNormalCube())
                {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
                }
                else
                {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
                }
            }
        }
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);

        if (!worldIn.isRemote)
        {
            for (EnumFacing enumfacing : EnumFacing.values())
            {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }

            this.updateSurroundingBluestone(worldIn, pos, state);

               for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL)
            {
                this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing1));
            }

            for (EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL)
            {
                BlockPos blockpos = pos.offset(enumfacing2);

                if (worldIn.getBlockState(blockpos).isNormalCube())
                {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
                }
                else
                {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
                }
            }
        }
    }


    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!worldIn.isRemote)
        {
            TileEntityBluestone bluestone = (TileEntityBluestone) worldIn.getTileEntity(pos);
            if(bluestone != null){
                boolean b = bluestone.isPowered();
                int i = b ?  1 : 0;
                worldIn.setBlockState(pos, state.withProperty(POWER, i));
            }

            if (this.canPlaceBlockAt(worldIn, pos))
            {
                this.updateSurroundingBluestone(worldIn, pos, state);
            }
            else
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
       // worldIn.setBlockToAir(pos);
       TileEntityBluestone bluestone = (TileEntityBluestone) worldIn.getTileEntity(pos);
       if(bluestone != null){
           boolean b = bluestone.isPowered();
           int i = b ?  1 : 0;
           worldIn.setBlockState(pos, state.withProperty(POWER, i));
       }
    }

    private boolean lightUp(World world, BlockPos pos){
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if(block == this){

            state = state.withProperty(POWER, 1);
            world.setBlockState(pos, state);
            //IBlockState state1 = state;
            world.scheduleUpdate(pos, state.getBlock(), 10);
            this.updateSurroundingBluestone(world, pos, state);
            return true;
        } else
        return false;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return ModItems.BLUESTONE;
    }

    protected static boolean canConnectUpwardsTo(IBlockAccess worldIn, BlockPos pos)
    {
        return canConnectTo(worldIn.getBlockState(pos), null, worldIn, pos);
    }

    protected static boolean canConnectUpwardsToVerticalDust(IBlockAccess worldIn, BlockPos pos , @Nullable EnumFacing side)
    {
        return canConnectTo(worldIn.getBlockState(pos), side, worldIn, pos);
    }

    protected static boolean canConnectTo(IBlockState blockState, @Nullable EnumFacing side, IBlockAccess world, BlockPos pos)
    {
        Block block = blockState.getBlock();

        if (block == ModBlocks.BLUESTONE_DUST)
        {
            return true;
        }
        else if(block == ModBlocks.BLUESTONE_VERTICAL){
            if(side!= null) {
                EnumFacing enumFacing = blockState.getValue(BlockBluestoneDustVertical.FACING);
                return enumFacing.getOpposite() == side;
            } else {
                return true;
            }
        }
        else if(block == ModBlocks.BATTERY){
            TileEntityBattery battery = (TileEntityBattery) world.getTileEntity(pos);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean canProvidePower(IBlockState state)
    {
        return this.canProvidePower;
    }

    @SideOnly(Side.CLIENT)
    public static int colorMultiplier(int p_176337_0_)
    {
        float f = (float)p_176337_0_ / 15.0F;
        float f3 = f * 0.4F + 0.4F;

        if (p_176337_0_ == 0)
        {
            f3 = 0.3F;
        }

        float f2 = f * f * 0.7F + 0.1F;
        float f1 = f * f * 0.6F - 0.7F;

        if (f2 < 0.0F)
        {
            f2 = 0.0F;
        }

        if (f1 < 0.0F)
        {
            f1 = 0.0F;
        }

        int i = MathHelper.clamp((int)(f1 * 255.0F), 0, 255); //red
        int j = MathHelper.clamp((int)(f2 * 255.0F), 0, 255); //green
        int k = MathHelper.clamp((int)(f3 * 255.0F), 0, 255); //blue
        return -16777216 | i << 16 | j << 8 | k;
    }

   /* @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        int i = ((Integer)stateIn.getValue(POWER)).intValue();

        if (i != 0)
        {
            double d0 = (double)pos.getX() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
            double d1 = (double)((float)pos.getY() + 0.0625F);
            double d2 = (double)pos.getZ() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
            float f = (float)i / 15.0F;
            float f1 = f * 0.6F + 0.4F;
            float f2 = Math.max(0.0F, f * f * 0.7F - 0.5F);
            float f3 = Math.max(0.0F, f * f * 0.6F - 0.7F);
            worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0, d1, d2, (double)f1, (double)f2, (double)f3);
        }
    }*/

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(ModItems.BLUESTONE);
    }

    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(POWER, meta);
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(POWER);
    }

    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        switch (rot)
        {
            case CLOCKWISE_180:
                return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(EAST, state.getValue(WEST)).withProperty(SOUTH, state.getValue(NORTH)).withProperty(WEST, state.getValue(EAST));
            case COUNTERCLOCKWISE_90:
                return state.withProperty(NORTH, state.getValue(EAST)).withProperty(EAST, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(WEST)).withProperty(WEST, state.getValue(NORTH));
            case CLOCKWISE_90:
                return state.withProperty(NORTH, state.getValue(WEST)).withProperty(EAST, state.getValue(NORTH)).withProperty(SOUTH, state.getValue(EAST)).withProperty(WEST, state.getValue(SOUTH));
            default:
                return state;
        }
    }

    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        switch (mirrorIn)
        {
            case LEFT_RIGHT:
                return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(NORTH));
            case FRONT_BACK:
                return state.withProperty(EAST, state.getValue(WEST)).withProperty(WEST, state.getValue(EAST));
            default:
                return super.withMirror(state, mirrorIn);
        }
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {NORTH, EAST, SOUTH, WEST, POWER});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBluestone();
    }

    enum EnumAttachPosition implements IStringSerializable
    {
        UP("up"),
        SIDE("side"),
        NONE("none");

        private final String name;

        EnumAttachPosition(String name)
        {
            this.name = name;
        }

        public String toString()
        {
            return this.getName();
        }

        public String getName()
        {
            return this.name;
        }
    }
}
