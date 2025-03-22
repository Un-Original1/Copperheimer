package com.unoriginal.copperheimer.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy{
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
     //   ModEntities.initModels();
    }
    @Override
    public void registerParticles() {
      //  Minecraft.getMinecraft().effectRenderer.registerParticle(ModParticles.SAND_SPIT.getParticleID(), new ParticleSandSpit.Factory());
    }
    @Override
    public Object getArmorModel(Item item, EntityLivingBase entity) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void openGui(ItemStack bestiary) {
    //    Minecraft.getMinecraft().displayGuiScreen(new GuiWiki(bestiary));
    }
}
