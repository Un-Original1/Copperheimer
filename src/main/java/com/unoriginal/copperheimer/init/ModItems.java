package com.unoriginal.copperheimer.init;

import com.unoriginal.copperheimer.Copperheimer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ModItems {
    //public static Item EXAMPLE
    public static void init() {
        //EXAMPLE = new Item()
    }
    //TODO CREATE A CUSTOM CREATIVE TAB
    public static Item quickItemRegistry(String name){
        return new Item().setRegistryName(name).setUnlocalizedName(name).setCreativeTab(Copperheimer.COPPERTAB).setMaxStackSize(64);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        //event.getRegistry().registerAll(Example)
    }
    @SubscribeEvent
    public static void registerRenders(ModelRegistryEvent event)
    {
        //register Render(EXAMPLE)
    }

    public static void registerRender(Item item)
    {
        if(!item.getHasSubtypes()) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
        } else {
            NonNullList<ItemStack> list = NonNullList.create();
            item.getSubItems(item.getCreativeTab(), list);
            for(ItemStack stack : list) {
                ModelLoader.setCustomModelResourceLocation(item, stack.getMetadata(), new ModelResourceLocation(item.getRegistryName(), "inventory"));
            }
        }
    }
}
