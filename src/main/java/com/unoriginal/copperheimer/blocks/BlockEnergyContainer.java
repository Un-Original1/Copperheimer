package com.unoriginal.copperheimer.blocks;


import com.deeperdepths.common.blocks.DeeperDepthsBlocks;
import com.unoriginal.copperheimer.Copperheimer;
import com.unoriginal.copperheimer.blocks.tile.TileEntityBattery;
import com.unoriginal.copperheimer.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockEnergyContainer extends Block implements ITileEntityProvider {

    public static final PropertyInteger CHARGED = PropertyInteger.create("charged", 0, 5);

    public BlockEnergyContainer(String name) {

        super(Material.IRON, MapColor.BLACK);
        this.setUnlocalizedName(name);
        this.setRegistryName(name);
        this.setCreativeTab(Copperheimer.COPPERTAB);
        this.setDefaultState(this.blockState.getBaseState().withProperty(CHARGED, 0));
    }

    @Override
    public int getLightValue(IBlockState state)
    { return state.getValue(CHARGED) > 0 ? 4 + ((state.getValue(CHARGED) - 1) * 5): 0; }

    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(ModBlocks.BATTERY);
    }

    public IBlockState getStateFromMeta(int meta) { return this.getDefaultState().withProperty(CHARGED, meta); }

    public int getMetaFromState(IBlockState state)
    { return  state.getValue(CHARGED); }

    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    { return Math.max(0, 5 * blockState.getValue(CHARGED) - 1); }

    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }


    public boolean isCharged(World world, BlockPos pos){
        return world.getBlockState(pos).getValue(CHARGED) > 0;
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {CHARGED});
    }
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!worldIn.isRemote) {

            if(worldIn.getBlockState(pos.up()).getBlock() == DeeperDepthsBlocks.LIGHTNING_ROD && worldIn.isBlockPowered(pos.up())){
                worldIn.setBlockState(pos, ModBlocks.BATTERY.getDefaultState().withProperty(CHARGED, 4), 2);
                TileEntityBattery battery = (TileEntityBattery) worldIn.getTileEntity(pos);
                if(battery != null){
                    battery.setBurning(true);
                    Copperheimer.logger.debug("battery CHARGED!");
                }


                worldIn.notifyNeighborsOfStateChange(pos, this, false);
                worldIn.scheduleUpdate(pos, this, 2400 );
            }
        }
        //12000 / 5 =?
    }
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            if (worldIn.getBlockState(pos).getValue(CHARGED) > 0)
            {
                int i = worldIn.getBlockState(pos).getValue(CHARGED);
                worldIn.setBlockState(pos, ModBlocks.BATTERY.getDefaultState().withProperty(CHARGED, i - 1), 2);
                worldIn.scheduleUpdate(pos, this, 2400);
            }
        }
    }
    public static void setState(boolean active, World worldIn, BlockPos pos)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity != null)
        {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBattery();
    }
    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

}
