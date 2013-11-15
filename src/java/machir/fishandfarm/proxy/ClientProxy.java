package machir.fishandfarm.proxy;

import machir.fishandfarm.client.gui.GuiStove;
import machir.fishandfarm.client.renderer.tileentity.TileEntityStoveRenderer;
import machir.fishandfarm.handler.StoveRenderer;
import machir.fishandfarm.tileentity.TileEntityStove;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenders() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityStove.class, new TileEntityStoveRenderer());
		RenderingRegistry.registerBlockHandler(new StoveRenderer());
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
