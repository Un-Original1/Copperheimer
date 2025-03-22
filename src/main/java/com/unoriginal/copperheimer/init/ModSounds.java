package com.unoriginal.copperheimer.init;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModSounds {
    @Mod.EventBusSubscriber
    public static class RegistrationHandler {
        @SubscribeEvent
        public static void registerSounds(final RegistryEvent.Register<SoundEvent> event) {
                //event.getRegistry.registerAll(SOUND)
        }
    }
}
