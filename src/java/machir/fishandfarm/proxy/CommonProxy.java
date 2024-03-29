package machir.fishandfarm.proxy;

import machir.fishandfarm.client.gui.GuiStove;
import machir.fishandfarm.inventory.container.ContainerStove;
import machir.fishandfarm.tileentity.TileEntityStove;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler {

	@SidedProxy(clientSide = "machir.fishandfarm.proxy.ClientProxy", serverSide = "machir.fishandfarm.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	public void registerRenders() {
	}
	
	// localization	
	public String getCurrentLanguage() {
		return null;
	}
	
	public void addName(Object obj, String s) {
	}

	public void addLocalization(String s1, String string) {
	}

	public String getItemDisplayName(ItemStack newStack) {
		return "";
	}
	
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if(tileEntity instanceof TileEntityStove)
            return new ContainerStove(player.inventory, (TileEntityStove)tileEntity);
        return null;
    }

	@Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) 
    {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
    	if(tileEntity instanceof TileEntityStove)
        	return new GuiStove(player.inventory, (TileEntityStove)tileEntity);
        return null;
    }	
}
