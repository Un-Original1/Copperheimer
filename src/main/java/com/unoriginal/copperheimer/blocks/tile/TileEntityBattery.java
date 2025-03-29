package com.unoriginal.copperheimer.blocks.tile;

import com.unoriginal.copperheimer.Copperheimer;
import com.unoriginal.copperheimer.capability.CapabilityElectric;
import com.unoriginal.copperheimer.capability.ElectricStorage;
import com.unoriginal.copperheimer.capability.IElectricStorage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityBattery extends TileEntity  {
    private int furnaceBurnTime;
    private final ElectricStorage energyStorage = new ElectricStorage(12000, 1000);
    public int electric = energyStorage.getElectricStored();


    public void  setBurning(boolean b){
        if(b) {
           // this.energyStorage.receiveElectric(12000, false);
            this.energyStorage.setEnergy(12000);
            markDirtyClient();
            Copperheimer.logger.debug(this.electric);
        }
    }
    public boolean isBurning()
    {
        Copperheimer.logger.debug(this.electric);
        return this.electric > 0;

    }
    public void outputEnergy(int maxOutput)
    {
        if(energyStorage.getElectricStored() > 0)
        {
            for(EnumFacing facing : EnumFacing.VALUES)
            {
                TileEntity tileEntity = world.getTileEntity(pos.offset(facing));

                if(tileEntity != null && tileEntity.hasCapability(CapabilityElectric.ENERGY, facing.getOpposite()))
                {
                    IElectricStorage handler = tileEntity.getCapability(CapabilityElectric.ENERGY, facing.getOpposite());
                    if(handler != null && handler.canReceive())
                    {
                        int accepted = handler.receiveElectric(maxOutput, false);
                        energyStorage.extractElectric(accepted, false);

                        if(energyStorage.getElectricStored() <= 0)
                        {
                            break;
                        }
                    }
                }
            }
            markDirtyClient();
        }
    }
    public void markDirtyClient()
    {
        markDirty();
        if(getWorld() != null)
        {
            IBlockState state = getWorld().getBlockState(getPos());
            getWorld().notifyBlockUpdate(getPos(), state, state, 3);
        }
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if(capability == CapabilityElectric.ENERGY) return CapabilityElectric.ENERGY.cast(energyStorage);
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        if(capability == CapabilityElectric.ENERGY) return true;
        return super.hasCapability(capability, facing);
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.furnaceBurnTime = compound.getInteger("BurnTime");
        energyStorage.setEnergy(compound.getInteger("Energy"));
    }
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("BurnTime", (short) this.furnaceBurnTime);
        compound.setInteger("Energy", energyStorage.getElectricStored());
        return compound;
    }

    public int getEnergyStored()
    {
        return this.energyStorage.getElectricStored();
    }

    public int getMaxEnergyStored()
    {
        return this.energyStorage.getMaxElectricStored();
    }
    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        super.onDataPacket(net, pkt);
        this.handleUpdateTag(pkt.getNbtCompound());
    }
}
