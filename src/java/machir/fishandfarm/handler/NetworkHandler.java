package machir.fishandfarm.handler;

import java.util.logging.Logger;

import machir.fishandfarm.FishAndFarm;
import machir.fishandfarm.ModInfo;
import machir.fishandfarm.client.gui.GuiCage;
import machir.fishandfarm.client.gui.GuiSmoker;
import machir.fishandfarm.client.gui.GuiStove;
import machir.fishandfarm.inventory.container.ContainerCage;
import machir.fishandfarm.inventory.container.ContainerSmoker;
import machir.fishandfarm.inventory.container.ContainerStove;
import machir.fishandfarm.packet.FishAndFarmPacket;
import machir.fishandfarm.packet.FishAndFarmPacket.ProtocolException;
import machir.fishandfarm.tileentity.TileEntityCage;
import machir.fishandfarm.tileentity.TileEntitySmoker;
import machir.fishandfarm.tileentity.TileEntityStove;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class NetworkHandler implements IPacketHandler, IGuiHandler {
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
        
        @Override
        public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
        {
            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
            if(tileEntity instanceof TileEntityStove && id == FishAndFarm.GUI_STOVE_ID)
                return new ContainerStove(player.inventory, (TileEntityStove)tileEntity);
            if(tileEntity instanceof TileEntitySmoker && id == FishAndFarm.GUI_SMOKER_ID)
                return new ContainerSmoker(player.inventory, (TileEntitySmoker)tileEntity);
              if(tileEntity instanceof TileEntityCage && id == FishAndFarm.GUI_CAGE_ID)
                    return new ContainerCage(player.inventory, (TileEntityCage)tileEntity);
            return null;
        }

        @Override
        public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) 
        {
            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
            if(tileEntity instanceof TileEntityStove && id == FishAndFarm.GUI_STOVE_ID)
                return new GuiStove(player.inventory, (TileEntityStove)tileEntity);
            if(tileEntity instanceof TileEntitySmoker && id == FishAndFarm.GUI_SMOKER_ID)
                return new GuiSmoker(player.inventory, (TileEntitySmoker)tileEntity);
            if(tileEntity instanceof TileEntityCage && id == FishAndFarm.GUI_CAGE_ID)
                return new GuiCage(player.inventory, (TileEntityCage)tileEntity);
            return null;
        } 
}