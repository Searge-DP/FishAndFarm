package machir.fishandfarm;

import java.io.File;
import java.util.logging.Logger;

import machir.fishandfarm.block.BlockLettuceCrop;
import machir.fishandfarm.block.BlockStove;
import machir.fishandfarm.block.BlockStrawberryCrop;
import machir.fishandfarm.block.BlockTomatoCrop;
import machir.fishandfarm.handler.BonemealHandler;
import machir.fishandfarm.handler.CraftingHandler;
import machir.fishandfarm.handler.PacketHandler;
import machir.fishandfarm.item.ItemCropSeeds;
import machir.fishandfarm.item.ItemFood;
import machir.fishandfarm.item.ItemFryingPan;
import machir.fishandfarm.item.ItemKnife;
import machir.fishandfarm.proxy.CommonProxy;
import machir.fishandfarm.tileentity.TileEntityStove;
import machir.fishandfarm.util.FishAndFarmConfig;
import machir.fishandfarm.util.Localization;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = ModInfo.MODID, name = "FishAndFarm", version = ModInfo.VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = { ModInfo.CHANNEL }, packetHandler = PacketHandler.class)
public class FishAndFarm {

	public static Logger fishAndFarmLog = Logger.getLogger(ModInfo.MODID);
	public static Configuration fishAndFarmConfig;
	
	// Blocks
	public static Block lettuceCrop;
	public static Block tomatoCrop;
	public static Block strawberryCrop;
	public static Block stove;
	
	// Items
	public static Item food;
	public static Item knife;
	public static Item seeds;
	public static Item fryingPan;
	
	@Instance(ModInfo.MODID)
	public static FishAndFarm instance;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent evt) {
		// initialize blocks, items, graphics, sounds, read configs, etc.
		fishAndFarmLog.info("Starting FishAndFarm version " + ModInfo.VERSION);
		
		fishAndFarmConfig = new Configuration(new File(evt.getModConfigurationDirectory(), "/machir/fishandfarm/main.conf"));
		
		try {
			// Knife
			Property knifeID = fishAndFarmConfig.getItem("knife.id", FishAndFarmConfig.DEFAULT_ID_ITEM_KNIFE);
			
			knife = new ItemKnife(knifeID.getInt() - 256).setUnlocalizedName(ModInfo.MODID + "." + ModInfo.UNLOC_NAME_ITEM_KNIFE + ".name").setTextureName("knife");
			GameRegistry.registerItem(knife, ModInfo.MODID + ":" + ModInfo.UNLOC_NAME_ITEM_KNIFE);
			GameRegistry.addRecipe(new ItemStack(knife, 1), new Object[]{" I", "S ", 'S', Item.stick, 'I', Item.ingotIron});
			
			// Foods
			Property foodID = fishAndFarmConfig.getItem("food.id", FishAndFarmConfig.DEFAULT_ID_ITEM_FOOD);
			
			food = new ItemFood(foodID.getInt() - 256).setUnlocalizedName(ModInfo.MODID + "." + ModInfo.UNLOC_NAME_ITEM_FOOD + ".name"); 
			GameRegistry.registerItem(food, ModInfo.MODID + ":" + ModInfo.UNLOC_NAME_ITEM_FOOD);
			GameRegistry.addSmelting(Item.egg.itemID, new ItemStack(food, 1, 0), 0.0f);
			GameRegistry.addShapelessRecipe(new ItemStack(food, 4, 1), new Object[]{Item.bread, new ItemStack(knife.itemID, 1, Short.MAX_VALUE)});
			GameRegistry.addRecipe(new ItemStack(food, 1, 5), new Object[]{ "S", "E", "B", 'B', new ItemStack(food, 2, 1), 'E', new ItemStack(food, 1, 0), 'S', Item.porkCooked});
			
			// Lettuce Crop
			Property lettuceCropID = fishAndFarmConfig.getBlock("lettuceCrop.id", FishAndFarmConfig.DEFAULT_ID_BLOCK_LETTUCE);
			
			lettuceCrop = new BlockLettuceCrop(lettuceCropID.getInt()).setUnlocalizedName(ModInfo.MODID + "." + ModInfo.UNLOC_NAME_BLOCK_LETTUCE + ".name").setTextureName("lettuce"); 
			GameRegistry.registerBlock(lettuceCrop, ModInfo.MODID + ":" + ModInfo.UNLOC_NAME_BLOCK_LETTUCE);
			
			// Tomato Crop
			Property tomatoCropID = fishAndFarmConfig.getBlock("tomatoCrop.id", FishAndFarmConfig.DEFAULT_ID_BLOCK_TOMATO);
			
			tomatoCrop = new BlockTomatoCrop(tomatoCropID.getInt()).setUnlocalizedName(ModInfo.MODID + "." + ModInfo.UNLOC_NAME_BLOCK_TOMATO + ".name").setTextureName("tomato"); 
			GameRegistry.registerBlock(tomatoCrop, ModInfo.MODID + ":" + ModInfo.UNLOC_NAME_BLOCK_TOMATO);
						
			// Strawberry Crop
			Property strawberryCropID = fishAndFarmConfig.getBlock("strawberryCrop.id", FishAndFarmConfig.DEFAULT_ID_BLOCK_STRAWBERRY);
			
			strawberryCrop = new BlockStrawberryCrop(strawberryCropID.getInt()).setUnlocalizedName(ModInfo.MODID + "." + ModInfo.UNLOC_NAME_BLOCK_STRAWBERRY + ".name").setTextureName("strawberry"); 
			GameRegistry.registerBlock(strawberryCrop, ModInfo.MODID + ":" + ModInfo.UNLOC_NAME_BLOCK_STRAWBERRY);
			
			// Seeds (AFTER CROPS!)
			Property seedsID = fishAndFarmConfig.getItem("seeds.id", FishAndFarmConfig.DEFAULT_ID_ITEM_SEEDS);
			
			seeds = new ItemCropSeeds(seedsID.getInt()).setUnlocalizedName(ModInfo.MODID + "." + ModInfo.UNLOC_NAME_ITEM_SEEDS + ".name");
			GameRegistry.registerItem(seeds, ModInfo.MODID + ":" + ModInfo.UNLOC_NAME_ITEM_SEEDS);
			GameRegistry.addShapelessRecipe(new ItemStack(seeds, 1, 0), new Object[]{ new ItemStack(food, 1, 2) });
			GameRegistry.addShapelessRecipe(new ItemStack(seeds, 1, 1), new Object[]{ new ItemStack(food, 1, 3) });
			GameRegistry.addShapelessRecipe(new ItemStack(seeds, 1, 2), new Object[]{ new ItemStack(food, 1, 4) });
			
			// Stove
			Property stoveID = fishAndFarmConfig.getBlock("stove.id", FishAndFarmConfig.DEFAULT_ID_BLOCK_STOVE);
			
			stove = new BlockStove(stoveID.getInt()).setUnlocalizedName(ModInfo.MODID + "." + ModInfo.UNLOC_NAME_BLOCK_STOVE + ".name").setTextureName("stove"); 
			GameRegistry.registerBlock(stove, ModInfo.MODID + ":" + ModInfo.UNLOC_NAME_BLOCK_STOVE);
			GameRegistry.addRecipe(new ItemStack(stove, 1, 0), new Object[]{ "III", "S S", "SSS", 'I', Item.ingotIron, 'S', Block.stone});
			
			// Frying Pan
			Property fryingPanID = fishAndFarmConfig.getItem("fryingPan.id", FishAndFarmConfig.DEFAULT_ID_ITEM_FRYINGPAN);
			
			fryingPan = new ItemFryingPan(fryingPanID.getInt() - 256).setUnlocalizedName(ModInfo.MODID + "." + ModInfo.UNLOC_NAME_ITEM_FRYINGPAN + ".name").setTextureName("fryingPan"); 
			GameRegistry.registerItem(fryingPan, ModInfo.MODID + ":" + ModInfo.UNLOC_NAME_ITEM_FRYINGPAN);
			GameRegistry.addRecipe(new ItemStack(fryingPan, 1, 0), new Object[]{ "IIS", 'S', Item.stick, 'I', Item.ingotIron});
		
		} finally {
			if(fishAndFarmConfig.hasChanged()) {
				fishAndFarmConfig.save();
			}
		}
	}
	
	@EventHandler
	public void init(FMLInitializationEvent evt) {
		// set up the mod
		Localization.addLocalization("/lang/fishandfarm/", "en_US");
		GameRegistry.registerCraftingHandler(new CraftingHandler());
		GameRegistry.registerTileEntity(TileEntityStove.class, "tileEntityStove");
		MinecraftForge.EVENT_BUS.register(new BonemealHandler());
        NetworkRegistry.instance().registerGuiHandler(this, new CommonProxy());
	
		CommonProxy.proxy.registerRenders();
		
		// Need to find a better way to do this but for now it'll work
		LanguageRegistry.instance().addStringLocalization(ModInfo.MODID + ".stove.name", "en_US", "Stove");
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent evt) {
		// handle things after loading, such as interacting with other mods
		fishAndFarmLog.info("Successfully loaded FishAndFarm");
	}
	
	@EventHandler
	public void serverStarting(FMLServerStartingEvent evt) {
		// things that only the server needs to do when starting, like registering commands
	}
}
