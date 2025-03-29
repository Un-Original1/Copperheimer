package com.unoriginal.copperheimer.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class CapabilityElectric {
    @CapabilityInject(IElectricStorage.class)
    public static Capability<IElectricStorage> ENERGY = null;

    public static void register()
    {
        CapabilityManager.INSTANCE.register(IElectricStorage.class, new Capability.IStorage<IElectricStorage>()
                {
                    @Override
                    public NBTBase writeNBT(Capability<IElectricStorage> capability, IElectricStorage instance, EnumFacing side)
                    {
                        return new NBTTagInt(instance.getElectricStored());
                    }

                    @Override
                    public void readNBT(Capability<IElectricStorage> capability, IElectricStorage instance, EnumFacing side, NBTBase nbt)
                    {
                        if (!(instance instanceof ElectricStorage))
                            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
                        ((ElectricStorage)instance).energy = ((NBTTagInt)nbt).getInt();
                    }
                },
                () -> new ElectricStorage(12000));
    }
}
