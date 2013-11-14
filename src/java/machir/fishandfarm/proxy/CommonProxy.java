package machir.fishandfarm.proxy;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.SidedProxy;

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
