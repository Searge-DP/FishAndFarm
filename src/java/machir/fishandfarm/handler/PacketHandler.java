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
import machir.fishandfarm.packet.FishAndFarmPacket;
import machir.fishandfarm.packet.FishAndFarmPacket.ProtocolException;
import machir.fishandfarm.tileentity.TileEntityStove;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler implements IPacketHandler {

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		try {
            EntityPlayer entityPlayer = (EntityPlayer)player;
            ByteArrayDataInput in = ByteStreams.newDataInput(packet.data);
            int packetId = in.readUnsignedByte(); // Assuming your packetId is between 0 (inclusive) and 256 (exclusive). If you need more you need to change this
            FishAndFarmPacket fishandfarmPacket = FishAndFarmPacket.constructPacket(packetId);
            fishandfarmPacket.read(in);
            fishandfarmPacket.execute(entityPlayer, entityPlayer.worldObj.isRemote ? Side.CLIENT : Side.SERVER);
	    } catch (ProtocolException e) {
	            if (player instanceof EntityPlayerMP) {
	                    ((EntityPlayerMP) player).playerNetServerHandler.kickPlayerFromServer("Protocol Exception!");
	                    Logger.getLogger(ModInfo.MODID).warning("Player " + ((EntityPlayer)player).username + " caused a Protocol Exception!");
	            }
	    } catch (ReflectiveOperationException e) {
	            throw new RuntimeException("Unexpected Reflection exception during Packet construction!", e);
	    }
	}
}
