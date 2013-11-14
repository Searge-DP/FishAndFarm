package machir.fishandfarm;

import java.io.File;
import java.util.logging.Logger;

import machir.fishandfarm.block.BlockLettuceCrop;
import machir.fishandfarm.block.BlockStrawberryCrop;
import machir.fishandfarm.block.BlockTomatoCrop;
import machir.fishandfarm.handler.BonemealHandler;
import machir.fishandfarm.handler.CraftingHandler;
import machir.fishandfarm.item.ItemCropSeeds;
import machir.fishandfarm.item.ItemFood;
import machir.fishandfarm.item.ItemKnife;
import machir.fishandfarm.util.FishAndFarmConfig;
import machir.fishandfarm.util.Localization;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = ModInfo.MODID, name = "FishAndFarm", version = ModInfo.VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class FishAndFarm {

	public static Logger fishAndFarmLog = Logger.getLogger(ModInfo.MODID);
	public static Configuration fishAndFarmConfig;
	
	// Blocks
	public static Block lettuceCrop;
	public static Block tomatoCrop;
	public static Block strawberryCrop;
	
	// Items
	public static Item food;
	public static Item knife;
	public static Item seeds;
	
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
			GameRegistry.addRecipe(new ItemStack(food, 1, 5), new Object[]{ "S", "E", "B", 'S', new ItemStack(food, 2, 0), 'E', new ItemStack(food, 1, 1), 'B', Item.porkCooked});
			
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
		MinecraftForge.EVENT_BUS.register(new BonemealHandler());
		
		// Temporary add seeds to grass
		MinecraftForge.addGrassSeed(new ItemStack(seeds, 1, 0), 1);
		MinecraftForge.addGrassSeed(new ItemStack(seeds, 1, 1), 1);
		MinecraftForge.addGrassSeed(new ItemStack(seeds, 1, 2), 1);
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
