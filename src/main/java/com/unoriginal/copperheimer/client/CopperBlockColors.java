package com.unoriginal.copperheimer.client;

import com.unoriginal.copperheimer.blocks.BlockBluestoneDust;
import com.unoriginal.copperheimer.init.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import static com.unoriginal.copperheimer.blocks.BlockBluestoneDust.POWER;

@SideOnly(Side.CLIENT)
public class CopperBlockColors implements IBlockColor {

    @Override
    public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {

            if (worldIn.getBlockState(pos).getBlock() == ModBlocks.BLUESTONE_DUST) {
                return BlockBluestoneDust.colorMultiplier(worldIn.getBlockState(pos).getValue(POWER) * 15);
            } else {
                return BlockBluestoneDust.colorMultiplier(15);
            }

    }
}
