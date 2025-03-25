package com.unoriginal.copperheimer.proxy;

import com.unoriginal.copperheimer.Copperheimer;
import com.unoriginal.copperheimer.init.ModBlocks;
import com.unoriginal.copperheimer.init.ModEntities;
import com.unoriginal.copperheimer.init.ModEvents;
import com.unoriginal.copperheimer.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod.EventBusSubscriber
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent e)
    {
        ModBlocks.init();
        ModEntities.init();
        ModItems.init();

        MinecraftForge.EVENT_BUS.register(new ModEvents());
      // GameRegistry.registerWorldGenerator(new ModWorldGen(), 0);
     //   ModTriggers.init();
     //   BeastSlayerPacketHandler.initMessages();
    }
    public void registerParticles() {

    }

    public void openGui(ItemStack bestiary) {
    }

    public Object getArmorModel(Item item, EntityLivingBase entity) {
        return null;
    }


    public void init(FMLInitializationEvent e) {
    //    ModParticles.init();
     //   ABGuiHandler.registerGuiHandler();
        ConfigManager.sync(Copperheimer.MODID, Config.Type.INSTANCE);
     //   OreDictionary.registerOre("treeLeaves", ModBlocks.CURSED_LEAVES);

     //   NetworkRegistry.INSTANCE.registerGuiHandler(BeastSlayer.instance, new ABGuiHandler());
    }
    public void postInit(FMLPostInitializationEvent e) {
     //   ModEntities.registerSpawns();
    }


}
