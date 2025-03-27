package com.unoriginal.copperheimer.init;

import com.unoriginal.copperheimer.blocks.BlockBluestoneDust;
import com.unoriginal.copperheimer.blocks.BlockBluestoneDustVertical;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class ModBlocks {
    //public static Block EXAMPLE;
    public static Block BLUESTONE_DUST;
    public static Block BLUESTONE_VERTICAL;
    public static void init(){
// EXAMPLE = new Block()
        BLUESTONE_DUST = new BlockBluestoneDust("bluestone_dust");
        BLUESTONE_VERTICAL = new BlockBluestoneDustVertical("bluestone_vertical");
    }
    @SubscribeEvent
    public static void registerBlock(RegistryEvent.Register<Block> event) {
        //event.getRegistry.registerAll(EXAMPLE)
        event.getRegistry().registerAll(BLUESTONE_DUST);
        event.getRegistry().registerAll(BLUESTONE_VERTICAL);
    }
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
     //   event.getRegistry().registerAll(new ItemBlock(BLUESTONE_DUST).setRegistryName(BLUESTONE_DUST.getRegistryName()));
     //   event.getRegistry().registerAll(new ItemBlock(BLUESTONE_VERTICAL).setRegistryName(BLUESTONE_VERTICAL.getRegistryName()));
        //event.getRegistry().registerAll(new ItemBlock(EXAMPLE).setRegistryName(EXAMPLE.getRegistryName()));
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerRenders(ModelRegistryEvent event) {
       // registerRender(Item.getItemFromBlock(EXAMPLE));
       // registerRender(Item.getItemFromBlock(BLUESTONE_DUST));
       // registerRender(Item.getItemFromBlock(BLUESTONE_VERTICAL));
    }

    public static void registerRender(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation( item.getRegistryName(), "normal"));
    }
    //for special inventory renders
    public static void registerRenderInv(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation( item.getRegistryName(), "inventory"));
    }

}