package machir.fishandfarm.handler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import machir.fishandfarm.ModInfo;
import machir.fishandfarm.item.crafting.EnumStoveToolType;
import machir.fishandfarm.item.crafting.EnumStoveToolType.StoveToolType;
import machir.fishandfarm.tileentity.TileEntityStove;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		if (packet.channel.equals("FishAndFarm")) {
			handlePacket(packet, player);
		}
	}

	private void handlePacket(Packet250CustomPayload packet, Player player) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		
		try {
			int x = inputStream.readInt();
			int y = inputStream.readInt();
			int z = inputStream.readInt();
			StoveToolType stoveToolType = EnumStoveToolType.getToolFromInt(inputStream.readInt());
			
			EntityPlayerMP playerMP = (EntityPlayerMP)player;
			
			TileEntity tileEntity = playerMP.worldObj.getBlockTileEntity(x, y, z);
			
			if (tileEntity != null && tileEntity instanceof TileEntityStove) {
				TileEntityStove tileEntityStove = (TileEntityStove) tileEntity;
				tileEntityStove.tool = stoveToolType;
				playerMP.worldObj.markBlockForUpdate(x, y, z);
			}
		} catch (IOException ex) {
			Logger.getLogger(ModInfo.MODID).log(Level.SEVERE, "AN ERROR HAS OCCURED WHILE READING THE STOVE PACKET");
		} finally {
			
		}
	}
}
