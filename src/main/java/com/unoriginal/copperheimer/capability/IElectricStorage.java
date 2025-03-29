package com.unoriginal.copperheimer.capability;

import net.minecraftforge.energy.IEnergyStorage;

public interface IElectricStorage {
    //a literal copy of IEnergyStorage, I'd prefer to have this on my own and isolated to prevent tampering with other mods

    int receiveElectric(int maxReceive, boolean simulate);

    /**
     * Removes energy from the storage. Returns quantity of energy that was removed.
     *
     * @param maxExtract
     *            Maximum amount of energy to be extracted.
     * @param simulate
     *            If TRUE, the extraction will only be simulated.
     * @return Amount of energy that was (or would have been, if simulated) extracted from the storage.
     */
    int extractElectric(int maxExtract, boolean simulate);

    /**
     * Returns the amount of energy currently stored.
     */
    int getElectricStored();

    /**
     * Returns the maximum amount of energy that can be stored.
     */
    int getMaxElectricStored();

    /**
     * Returns if this storage can have energy extracted.
     * If this is false, then any calls to extractEnergy will return 0.
     */
    boolean canExtract();

    /**
     * Used to determine if this storage can receive energy.
     * If this is false, then any calls to receiveEnergy will return 0.
     */
    boolean canReceive();
}
