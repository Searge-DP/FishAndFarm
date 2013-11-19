package machir.fishandfarm.proxy;

import machir.fishandfarm.FishAndFarm;
import machir.fishandfarm.client.renderer.entity.RenderIronFishingHook;
import machir.fishandfarm.client.renderer.tileentity.TileEntitySmokerRenderer;
import machir.fishandfarm.client.renderer.tileentity.TileEntityStoveRenderer;
import machir.fishandfarm.entity.EntityIronFishHook;
import machir.fishandfarm.tileentity.TileEntitySmoker;
import machir.fishandfarm.tileentity.TileEntityStove;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenders() {
		/** Register TESR and IIR **/
		// Stove
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityStove.class, new TileEntityStoveRenderer());
		MinecraftForgeClient.registerItemRenderer(FishAndFarm.stove.blockID, new TileEntityStoveRenderer());

		// Smoker
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySmoker.class, new TileEntitySmokerRenderer());
		MinecraftForgeClient.registerItemRenderer(FishAndFarm.smoker.blockID, new TileEntitySmokerRenderer());
		
		/** Register Entity Renderers**/
		RenderingRegistry.registerEntityRenderingHandler(EntityIronFishHook.class, new RenderIronFishingHook());
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
