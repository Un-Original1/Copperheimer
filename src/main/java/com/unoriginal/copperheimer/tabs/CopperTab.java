package com.unoriginal.copperheimer.tabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class CopperTab extends CreativeTabs {
    public CopperTab(String label){ super(label);}

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(Items.EGG);
    }
}
