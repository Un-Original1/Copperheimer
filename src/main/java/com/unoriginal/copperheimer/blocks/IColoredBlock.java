package com.unoriginal.copperheimer.blocks;

import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IColoredBlock {
    @SideOnly(Side.CLIENT)
    IBlockColor getBlockColor();
}
