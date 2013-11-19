package machir.fishandfarm.packet;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import machir.fishandfarm.ModInfo;
import machir.fishandfarm.tileentity.TileEntityStove;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class TileEntityPacket extends FishAndFarmPacket {
    private NBTTagCompound tileEntityNBTTagCompound;
    
    public TileEntityPacket(NBTTagCompound nbttagcompound) {
    	tileEntityNBTTagCompound = nbttagcompound;
    }
    
    public TileEntityPacket() {}
        
    @Override
    public void write(ByteArrayDataOutput out) {
        try {
            writeNBTTagCompound(tileEntityNBTTagCompound, out);
        } catch (IOException e) {
            Logger.getLogger(ModInfo.MODID).log(Level.SEVERE, "TileEntity Packet couldn't be written.");
        } 
    }
        
    @Override
    public void read(ByteArrayDataInput in) throws ProtocolException {
        try {
            this.tileEntityNBTTagCompound = readNBTTagCompound(in);
        } catch (IOException ex) {
            Logger.getLogger(ModInfo.MODID).log(Level.SEVERE, "TileEntity Packet couldn't be read.");
        }
    }
        
    public static NBTTagCompound readNBTTagCompound(DataInput dataInput) throws IOException
    {
        short packetLength = dataInput.readShort();
        
        if (packetLength < 0)
        {
            return null;
        }
        else
        {
            byte[] nbtbyte = new byte[packetLength];
            dataInput.readFully(nbtbyte);
            return CompressedStreamTools.decompress(nbtbyte);
        }
    }
        
    protected static void writeNBTTagCompound(NBTTagCompound nbttagcompound, DataOutput dataOutput) throws IOException
    {
        if (nbttagcompound == null)
        {
            dataOutput.writeShort(-1);
        }
        else
        {
            byte[] nbtbyte = CompressedStreamTools.compress(nbttagcompound);
            dataOutput.writeShort((short)nbtbyte.length);
            dataOutput.write(nbtbyte);
        }
    }

    @Override
    public void execute(EntityPlayer player, Side side) throws ProtocolException {
        if (side.isClient()) {
            TileEntity tileEntity = player.worldObj.getBlockTileEntity(tileEntityNBTTagCompound.getInteger("x"), tileEntityNBTTagCompound.getInteger("y"), tileEntityNBTTagCompound.getInteger("z"));
            if (tileEntity != null) {
            	tileEntity.readFromNBT(tileEntityNBTTagCompound);
            }
        }
                
        if (side.isServer()) {
            PacketDispatcher.sendPacketToAllAround(tileEntityNBTTagCompound.getInteger("x"), tileEntityNBTTagCompound.getInteger("y"), tileEntityNBTTagCompound.getInteger("z"), 64, 0, new TileEntityPacket(tileEntityNBTTagCompound).makePacket());
        }
    }
}