package com.unoriginal.copperheimer.blocks;

import com.google.common.base.Predicate;
import com.unoriginal.copperheimer.Copperheimer;

import com.unoriginal.copperheimer.init.ModBlocks;
import com.unoriginal.copperheimer.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockBluestoneDustVertical extends Block {
    public static final PropertyEnum<BlockBluestoneDustVertical.EnumAttachPosition> UP = PropertyEnum.<BlockBluestoneDustVertical.EnumAttachPosition>create("north", BlockBluestoneDustVertical.EnumAttachPosition.class);
    public static final PropertyEnum<BlockBluestoneDustVertical.EnumAttachPosition> LEFT = PropertyEnum.<BlockBluestoneDustVertical.EnumAttachPosition>create("east", BlockBluestoneDustVertical.EnumAttachPosition.class);
    public static final PropertyEnum<BlockBluestoneDustVertical.EnumAttachPosition> DOWN = PropertyEnum.<BlockBluestoneDustVertical.EnumAttachPosition>create("south", BlockBluestoneDustVertical.EnumAttachPosition.class);
    public static final PropertyEnum<BlockBluestoneDustVertical.EnumAttachPosition> RIGHT = PropertyEnum.<BlockBluestoneDustVertical.EnumAttachPosition>create("west", BlockBluestoneDustVertical.EnumAttachPosition.class);
    //above are the connection enums, below is the facing stuff;
    public static final PropertyDirection FACING = PropertyDirection.create("facing", new Predicate<EnumFacing>()
    {
        public boolean apply(@Nullable EnumFacing p_apply_1_)
        {
            return p_apply_1_ != EnumFacing.DOWN && p_apply_1_ != EnumFacing.UP;
        }
    });
    public BlockBluestoneDustVertical(String name) {
        super(Material.CIRCUITS, MapColor.BLUE);
        //this.setHardness(3.0F);
        this.setRegistryName(name);
        this.setUnlocalizedName(name);
        //this.setLightLevel(0.8F);
        this.setCreativeTab(Copperheimer.COPPERTAB);

        this.setDefaultState(this.blockState.getBaseState().withProperty(UP, BlockBluestoneDustVertical.EnumAttachPosition.NONE).withProperty(LEFT, BlockBluestoneDustVertical.EnumAttachPosition.NONE).withProperty(DOWN, BlockBluestoneDustVertical.EnumAttachPosition.NONE).withProperty(RIGHT, BlockBluestoneDustVertical.EnumAttachPosition.NONE).withProperty(FACING, EnumFacing.NORTH));
    }


    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        return side != EnumFacing.DOWN && side != EnumFacing.UP;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        for (EnumFacing enumfacing : FACING.getAllowedValues())
        {
            if (this.canPlaceAt(worldIn, pos, enumfacing) && !worldIn.getBlockState(pos.down()).isTopSolid())
            {
                return true;
            }
        }

        return false;
    }

    private boolean canPlaceAt(World worldIn, BlockPos pos, EnumFacing facing)
    {
        BlockPos blockpos = pos.offset(facing.getOpposite());
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        BlockFaceShape blockfaceshape = iblockstate.getBlockFaceShape(worldIn, blockpos, facing);

        if (facing != EnumFacing.UP && facing != EnumFacing.DOWN)
        {
            return !isExceptBlockForAttachWithPiston(block) && blockfaceshape == BlockFaceShape.SOLID;
        }
        else
        {
            return false;
        }
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        if (this.canPlaceAt(worldIn, pos, facing))
        {
            return this.getDefaultState().withProperty(FACING, facing);
        }
        else
        {
            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
            {
                if (this.canPlaceAt(worldIn, pos, enumfacing))
                {
                    return this.getDefaultState().withProperty(FACING, enumfacing);
                }
            }

            return this.getDefaultState();
        }
    }

    /*public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        this.checkForDrop(worldIn, pos, state);
    }*/

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        this.onNeighborChangeInternal(worldIn, pos, state);
    }

    protected boolean onNeighborChangeInternal(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!this.checkForDrop(worldIn, pos, state))
        {
            return true;
        }
        else
        {
            EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
            EnumFacing.Axis enumfacing$axis = enumfacing.getAxis();
            EnumFacing enumfacing1 = enumfacing.getOpposite();
            BlockPos blockpos = pos.offset(enumfacing1);
            boolean flag = false;

            if (enumfacing$axis.isHorizontal() && worldIn.getBlockState(blockpos).getBlockFaceShape(worldIn, blockpos, enumfacing) != BlockFaceShape.SOLID)
            {
                flag = true;
            }
            else if (enumfacing$axis.isVertical())
            {
                flag = true;
            }

            if (flag)
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    protected boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state)
    {
        if (state.getBlock() == this && this.canPlaceAt(worldIn, pos, (EnumFacing)state.getValue(FACING)))
        {
            return true;
        }
        else
        {
            if (worldIn.getBlockState(pos).getBlock() == this)
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }

            return false;
        }
    }
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState iblockstate = this.getDefaultState();

        switch (meta)
        {
            case 1:
                iblockstate = iblockstate.withProperty(FACING, EnumFacing.EAST);
                break;
            case 2:
                iblockstate = iblockstate.withProperty(FACING, EnumFacing.WEST);
                break;
            case 3:
                iblockstate = iblockstate.withProperty(FACING, EnumFacing.SOUTH);
                break;
            case 4:
                iblockstate = iblockstate.withProperty(FACING, EnumFacing.NORTH);
                break;
           /* case 5:
            default:
                iblockstate = iblockstate.withProperty(FACING, EnumFacing.UP);*/
        }

        return iblockstate;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    public int getMetaFromState(IBlockState state)
    {
        int i = 0;

        switch ((EnumFacing)state.getValue(FACING))
        {
            case EAST:
                i = i | 1;
                break;
            case WEST:
                i = i | 2;
                break;
            case SOUTH:
                i = i | 3;
                break;
            case NORTH:
                i = i | 4;
                break;
           /* case DOWN:
            case UP:
            default:
                i = i | 5;*/
        }

        return i;
    }



    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        state = state.withProperty(RIGHT, this.getAttachPosition(state, worldIn, pos, CalculateOffset(EnumFacing.WEST, worldIn.getBlockState(pos))));
        state = state.withProperty(LEFT, this.getAttachPosition(state, worldIn, pos,CalculateOffset(EnumFacing.EAST,  worldIn.getBlockState(pos))));
        state = state.withProperty(UP, this.getAttachPosition(state, worldIn, pos,CalculateOffset(EnumFacing.NORTH,  worldIn.getBlockState(pos))));
        state = state.withProperty(DOWN, this.getAttachPosition(state, worldIn, pos, CalculateOffset(EnumFacing.SOUTH, worldIn.getBlockState(pos))));
        return state;
    }

    public EnumFacing CalculateOffset(EnumFacing basic, IBlockState state){
        EnumFacing facing = state.getValue(FACING);
        EnumFacing result = null;
        switch (facing){
            case WEST:
                switch (basic){
                    case SOUTH:
                        result = EnumFacing.NORTH;
                        break;
                    case EAST:
                        result =  EnumFacing.DOWN;
                       break;
                    case NORTH:
                        result =  EnumFacing.SOUTH;
                       break;
                    case WEST:
                        result =  EnumFacing.UP;
                       break;
                }
                break;
            case EAST:
                switch (basic){
                    case SOUTH:
                        result = EnumFacing.SOUTH;
                    break;
                    case EAST:
                        result = EnumFacing.DOWN;
                    break;
                    case NORTH:
                        result = EnumFacing.NORTH;
                    break;
                    case WEST:
                        result =  EnumFacing.UP;
                    break;
                }
                break;
            case NORTH:
                switch (basic){
                    case SOUTH:
                        result =  EnumFacing.DOWN;
                    break;
                    case EAST:
                        result =  EnumFacing.WEST;
                    break;
                    case NORTH:
                        result =  EnumFacing.UP;
                    break;
                    case WEST:
                        result =  EnumFacing.EAST;
                    break;
                }
                break;
            case SOUTH:
                switch (basic){
                    case SOUTH:
                        result =  EnumFacing.DOWN;
                    break;
                    case EAST:
                        result =  EnumFacing.EAST;
                    break;
                    case NORTH:
                        result =  EnumFacing.UP;
                    break;
                    case WEST:
                        result =  EnumFacing.WEST;
                    break;
                }
                break;
        }
        return result;
    }
    //this is a mess, it detects one as every bit connected. why?
    private BlockBluestoneDustVertical.EnumAttachPosition getAttachPosition(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing direction)
    {
        BlockPos blockpos = pos.offset(direction);
        IBlockState iblockstate = worldIn.getBlockState(pos.offset(direction));
        EnumFacing facing = worldIn.getBlockState(pos).getValue(FACING);

        if (!canConnectTo(worldIn.getBlockState(blockpos), direction, worldIn, blockpos) && (iblockstate.isNormalCube() || (!canConnectUpwardsTo(worldIn, blockpos.offset(facing.getOpposite())) || !canConnectUpwardsToVerticalDust(worldIn, blockpos.offset(facing.getOpposite()), facing))))

        {


            IBlockState iblockstate1 = worldIn.getBlockState(pos.offset(direction).up());

            if (!iblockstate1.isNormalCube())
           {
                boolean flag = worldIn.getBlockState(blockpos).isSideSolid(worldIn, blockpos, direction) || worldIn.getBlockState(blockpos).getBlock() == Blocks.GLOWSTONE;



               if (flag && canConnectUpwardsTo(worldIn, blockpos.up()))
               {
                    if (iblockstate.isBlockNormalCube()) {
                        return BlockBluestoneDustVertical.EnumAttachPosition.UP;
                    }

                    return BlockBluestoneDustVertical.EnumAttachPosition.SIDE;

                }

               if (flag && canConnectUpwardsToVerticalDust(worldIn, blockpos.offset(facing), facing)) {

                    return BlockBluestoneDustVertical.EnumAttachPosition.UP;
               }
               else {
                    return BlockBluestoneDustVertical.EnumAttachPosition.NONE;
               }
            }
            return EnumAttachPosition.NONE;

        }

        else
        {

                return BlockBluestoneDustVertical.EnumAttachPosition.SIDE;
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
        this.checkForDrop(worldIn, pos, state);
        if (!worldIn.isRemote)
        {
            // this.updateSurroundingRedstone(worldIn, pos, state);

            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
            {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }

            for (EnumFacing enumfacing1 : EnumFacing.Plane.VERTICAL)
            {
                this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing1));
            }

            for (EnumFacing enumfacing2 : EnumFacing.Plane.VERTICAL)
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

            //  this.updateSurroundingRedstone(worldIn, pos, state);

           /*    for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL)
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
            }*/
        }
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
    //whi is it only detecting up????
    //TODO we should try to first get it working on a single direction (north), I believe this can be somehow "forced" to work, need to log every single detection and what it detects so I can find how & why it is detecting incorrectly
    protected static boolean canConnectTo(IBlockState blockState, @Nullable EnumFacing side, IBlockAccess world, BlockPos pos)
    {
        Block block = blockState.getBlock();

        if (block == ModBlocks.BLUESTONE_DUST)
        {
            return true;
        }
        else if (block == ModBlocks.BLUESTONE_VERTICAL){
            EnumFacing facing = blockState.getValue(BlockBluestoneDustVertical.FACING);
            IBlockState us = world.getBlockState(pos);
            EnumFacing usfacing = us.getValue(FACING);
            return facing == usfacing/* || facing.getOpposite() == side*/;
        }
     /*   else if (Blocks.UNPOWERED_REPEATER.isSameDiode(blockState))
        {
            EnumFacing enumfacing = (EnumFacing)blockState.getValue(BlockRedstoneRepeater.FACING);
            return enumfacing == side || enumfacing.getOpposite() == side;
        }
        else if (Blocks.OBSERVER == blockState.getBlock())
        {
            return side == blockState.getValue(BlockObserver.FACING);
        }*/
        else
        {
            return false /*blockState.getBlock().canConnectRedstone(blockState, world, pos, side)*/;
        }
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(ModItems.BLUESTONE);
    }

    /*public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState()/*.withProperty(POWER, Integer.valueOf(meta));
    }*/

  /*  public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(POWER)).intValue();
    }*/

    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        switch (rot)
        {
            case CLOCKWISE_180:
                return state.withProperty(UP, state.getValue(DOWN)).withProperty(LEFT, state.getValue(RIGHT)).withProperty(DOWN, state.getValue(UP)).withProperty(RIGHT, state.getValue(LEFT));
            case COUNTERCLOCKWISE_90:
                return state.withProperty(UP, state.getValue(LEFT)).withProperty(LEFT, state.getValue(DOWN)).withProperty(DOWN, state.getValue(RIGHT)).withProperty(RIGHT, state.getValue(UP));
            case CLOCKWISE_90:
                return state.withProperty(UP, state.getValue(RIGHT)).withProperty(LEFT, state.getValue(UP)).withProperty(DOWN, state.getValue(LEFT)).withProperty(RIGHT, state.getValue(DOWN));
            default:
                return state;
        }
    }

    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        switch (mirrorIn)
        {
            case LEFT_RIGHT:
                return state.withProperty(UP, state.getValue(DOWN)).withProperty(DOWN, state.getValue(UP));
            case FRONT_BACK:
                return state.withProperty(LEFT, state.getValue(RIGHT)).withProperty(RIGHT, state.getValue(LEFT));
            default:
                return super.withMirror(state, mirrorIn);
        }
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {UP, LEFT, DOWN, RIGHT, FACING});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    static enum EnumAttachPosition implements IStringSerializable
    {
        UP("up"),
        SIDE("side"),
        NONE("none");

        private final String name;

        private EnumAttachPosition(String name)
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
