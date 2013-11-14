package machir.fishandfarm.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelOcelot;
import net.minecraft.client.renderer.entity.RenderOcelot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenders() {
	}
	
	@Override
	public String getCurrentLanguage() {
		return Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode();
	}

	@Override
	public void addName(Object obj, String s) {
		LanguageRegistry.addName(obj, s);
	}

	@Override
	public void addLocalization(String s1, String string) {
		LanguageRegistry.instance().addStringLocalization(s1, string);
	}

	@Override
	public String getItemDisplayName(ItemStack stack) {
		if (Item.itemsList[stack.itemID] == null)
			return "";

		return Item.itemsList[stack.itemID].getItemDisplayName(stack);
	}
	
}
