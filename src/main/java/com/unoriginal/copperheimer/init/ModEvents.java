package com.unoriginal.copperheimer.init;

import com.unoriginal.copperheimer.client.CopperBlockColors;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModEvents {
    CopperBlockColors copperBlockColors = new CopperBlockColors();
    @SubscribeEvent
    public void Color(ColorHandlerEvent.Block event){
        event.getBlockColors().registerBlockColorHandler(copperBlockColors, ModBlocks.BLUESTONE_DUST);
        event.getBlockColors().registerBlockColorHandler(copperBlockColors, ModBlocks.BLUESTONE_VERTICAL);
    }
}
