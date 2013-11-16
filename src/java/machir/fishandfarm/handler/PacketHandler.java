package machir.fishandfarm.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import machir.fishandfarm.ModInfo;
import machir.fishandfarm.tileentity.TileEntityStove;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		if (packet.channel.equals(ModInfo.CHANNEL)) {
			handlePacket(packet, player);
		}
	}

	private void handlePacket(Packet250CustomPayload packet, Player player) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		try {
			NBTTagCompound nbttagcompound = readNBTTagCompound(inputStream);
			EntityPlayerMP entityPlayer = (EntityPlayerMP)player;
			entityPlayer.worldObj.markBlockForUpdate(nbttagcompound.getInteger("x"), nbttagcompound.getInteger("y"), nbttagcompound.getInteger("z"));
		} catch (IOException ex) {
			Logger.getLogger(ModInfo.MODID).log(Level.SEVERE, "AN ERROR HAS OCCURED WHILE READING THE STOVE PACKET");
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
	
	public static Packet250CustomPayload createNewStovePacket(TileEntityStove tileEntityStove) {
		if (tileEntityStove.worldObj != null) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			tileEntityStove.writeToNBT(nbttagcompound);
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
            DataOutputStream outputStream = new DataOutputStream(bos);
            try {
            	writeNBTTagCompound(nbttagcompound, outputStream);                    
            } catch (Exception ex) {
                    ex.printStackTrace();
            }
            
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = ModInfo.CHANNEL;
            packet.data = bos.toByteArray();
            packet.length = bos.size();
			
			return packet;
			
		}
		return null;
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
}
