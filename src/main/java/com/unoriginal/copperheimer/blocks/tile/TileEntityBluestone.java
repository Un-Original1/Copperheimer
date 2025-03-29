package com.unoriginal.copperheimer.blocks.tile;

import com.unoriginal.copperheimer.Copperheimer;
import com.unoriginal.copperheimer.blocks.BlockBluestoneDust;
import com.unoriginal.copperheimer.capability.IElectricStorage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityBluestone extends TileEntity implements ITickable {
    private TileEntity master;
    private boolean isPowered;

    @Override
    public void update() {
        IBlockState blockAt = world.getBlockState(pos);
        if (blockAt.getBlock() instanceof BlockBluestoneDust) {
            for (EnumFacing facing : EnumFacing.VALUES){
                BlockPos otherPos1 = pos.offset(facing);

                if(!world.isRemote && this.world.getTileEntity(otherPos1) != null && this.world.getTileEntity(otherPos1) instanceof TileEntityBattery) {

                    TileEntityBattery battery = (TileEntityBattery) this.world.getTileEntity(otherPos1);
                    if(battery != null){
                        this.isPowered = battery.getEnergyStored() > 0;
                        if(this.master == null){
                            this.master = battery;
                        }
                    }
                   /* else {
                        if(this.master == null)
                        this.isPowered = false;
                    }*/
                    markDirty();
                }

                if(!world.isRemote && this.world.getTileEntity(otherPos1) != null && this.world.getTileEntity(otherPos1) instanceof TileEntityBluestone) {
                    TileEntityBluestone bluestone = (TileEntityBluestone) this.world.getTileEntity(otherPos1);
                    if(bluestone != null && bluestone.isPowered){
                        this.isPowered = bluestone.isPowered;
                        if(this.master == null){
                            this.master = bluestone;
                        }
                    }
                   /* else {
                        if(this.master == null)
                        this.isPowered = false;
                    }*/
                    markDirty();
                }
            }
        }
        /*if(this.master == null){
            this.isPowered = false;
            markDirty();
        }*/
    }

    public boolean isPowered(){
        Copperheimer.logger.debug(this.isPowered);
        return this.isPowered;

    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.isPowered = compound.getBoolean("Power");
        if(this.master != null) {
            this.master = this.world.getTileEntity(new BlockPos(compound.getInteger("MasterX"), compound.getInteger("MasterY"), compound.getInteger("MasterZ")));
        }
      //  energyStorage.setEnergy(compound.getInteger("Energy"));
    }
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
       // compound.setInteger("BurnTime", (short) this.furnaceBurnTime);
        compound.setBoolean("Power", this.isPowered);
        if(this.master != null) {
            compound.setInteger("MasterX", this.master.getPos().getX());
            compound.setInteger("MasterY", this.master.getPos().getY());
            compound.setInteger("MasterZ", this.master.getPos().getZ());
        }

        return compound;
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
