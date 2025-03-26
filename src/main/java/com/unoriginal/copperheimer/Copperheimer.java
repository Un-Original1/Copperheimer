package com.unoriginal.copperheimer;

import com.unoriginal.copperheimer.proxy.CommonProxy;
import com.unoriginal.copperheimer.tabs.CopperTab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = Copperheimer.MODID, name = Copperheimer.NAME, version = Copperheimer.VERSION)
public class Copperheimer
{
    public static final String MODID = "copperheimer";
    public static final String NAME = "Copperheimer";
    public static final String VERSION = "1.0";
    @SidedProxy(serverSide = "com.unoriginal.copperheimer.proxy.CommonProxy", clientSide = "com.unoriginal.copperheimer.proxy.ClientProxy")
    public static CommonProxy commonProxy;

    public static final CreativeTabs COPPERTAB = new CopperTab("coppertab");

    @Mod.Instance
    public static Copperheimer instance;
    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        commonProxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        commonProxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        commonProxy.postInit(e);
    }
}
