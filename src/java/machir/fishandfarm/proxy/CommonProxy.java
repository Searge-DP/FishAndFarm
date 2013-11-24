package machir.fishandfarm.proxy;

import machir.fishandfarm.FishAndFarm;
import machir.fishandfarm.client.gui.GuiCage;
import machir.fishandfarm.client.gui.GuiSmoker;
import machir.fishandfarm.client.gui.GuiStove;
import machir.fishandfarm.inventory.container.ContainerCage;
import machir.fishandfarm.inventory.container.ContainerSmoker;
import machir.fishandfarm.inventory.container.ContainerStove;
import machir.fishandfarm.tileentity.TileEntityCage;
import machir.fishandfarm.tileentity.TileEntitySmoker;
import machir.fishandfarm.tileentity.TileEntityStove;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy {

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
}
