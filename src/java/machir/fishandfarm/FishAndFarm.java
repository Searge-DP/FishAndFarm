package machir.fishandfarm;

import java.io.File;
import java.util.logging.Logger;

import machir.fishandfarm.block.BlockCage;
import machir.fishandfarm.block.BlockEmpty;
import machir.fishandfarm.block.BlockLettuceCrop;
import machir.fishandfarm.block.BlockSmoker;
import machir.fishandfarm.block.BlockStove;
import machir.fishandfarm.block.BlockStrawberryCrop;
import machir.fishandfarm.block.BlockTomatoCrop;
import machir.fishandfarm.entity.EntityCageBobber;
import machir.fishandfarm.entity.EntityIronFishHook;
import machir.fishandfarm.handler.BonemealHandler;
import machir.fishandfarm.handler.NetworkHandler;
import machir.fishandfarm.inventory.slot.SlotBait;
import machir.fishandfarm.inventory.slot.SlotFish;
import machir.fishandfarm.item.ItemCropSeeds;
import machir.fishandfarm.item.ItemFish;
import machir.fishandfarm.item.ItemFood;
import machir.fishandfarm.item.ItemFryingPan;
import machir.fishandfarm.item.ItemIronFishingRod;
import machir.fishandfarm.item.ItemKnife;
import machir.fishandfarm.proxy.CommonProxy;
import machir.fishandfarm.tileentity.TileEntityCage;
import machir.fishandfarm.tileentity.TileEntityEmpty;
import machir.fishandfarm.tileentity.TileEntitySmoker;
import machir.fishandfarm.tileentity.TileEntityStove;
import machir.fishandfarm.util.FishAndFarmConfig;
import machir.fishandfarm.util.Localization;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = ModInfo.MODID, name = "FishAndFarm", version = ModInfo.VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {ModInfo.CHANNEL}, packetHandler = NetworkHandler.class)
public class FishAndFarm {

	public static Logger fishAndFarmLog = Logger.getLogger(ModInfo.MODID);
	public static Configuration fishAndFarmConfig;
	
	// Gui ids
	public static final int GUI_STOVE_ID = 0;
	public static final int GUI_SMOKER_ID = 1;
	public static final int GUI_CAGE_ID = 2;
		
	// Blocks
	public static Block lettuceCrop;
	public static Block tomatoCrop;
	public static Block strawberryCrop;
	public static Block stove;
	public static Block empty;
	public static Block smoker;
	public static Block cage;
	
	// Items
	public static Item food;
	public static Item knife;
	public static Item seeds;
	public static Item fryingPan;
	public static Item ironFishingRod;
	public static Item fish;
	
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
			
			knife = new ItemKnife(knifeID.getInt() - 256).setUnlocalizedName(ModInfo.MODID + "." + ModInfo.UNLOC_NAME_ITEM_KNIFE).setTextureName("knife");
			GameRegistry.registerItem(knife, ModInfo.MODID + ":" + ModInfo.UNLOC_NAME_ITEM_KNIFE);
			GameRegistry.addRecipe(new ItemStack(knife, 1), new Object[]{" I", "S ", 'S', Item.stick, 'I', Item.ingotIron});
			
			// Foods
			Property foodID = fishAndFarmConfig.getItem("food.id", FishAndFarmConfig.DEFAULT_ID_ITEM_FOOD);
			
			food = new ItemFood(foodID.getInt() - 256).setUnlocalizedName(ModInfo.MODID + "." + ModInfo.UNLOC_NAME_ITEM_FOOD); 
			GameRegistry.registerItem(food, ModInfo.MODID + ":" + ModInfo.UNLOC_NAME_ITEM_FOOD);
			GameRegistry.addShapelessRecipe(new ItemStack(food, 4, 1), new Object[]{Item.bread, new ItemStack(knife.itemID, 1, Short.MAX_VALUE)});
			GameRegistry.addRecipe(new ItemStack(food, 1, 5), new Object[]{ "S", "E", "B", 'S', new ItemStack(food, 2, 0), 'E', new ItemStack(food, 1, 1), 'B', Item.porkCooked});
			
			// Lettuce Crop
			Property lettuceCropID = fishAndFarmConfig.getBlock("lettuceCrop.id", FishAndFarmConfig.DEFAULT_ID_BLOCK_LETTUCE);
			
			lettuceCrop = new BlockLettuceCrop(lettuceCropID.getInt()).setUnlocalizedName(ModInfo.MODID + "." + ModInfo.UNLOC_NAME_BLOCK_LETTUCE).setTextureName("lettuce"); 
			GameRegistry.registerBlock(lettuceCrop, ModInfo.MODID + ":" + ModInfo.UNLOC_NAME_BLOCK_LETTUCE);
			
			// Tomato Crop
			Property tomatoCropID = fishAndFarmConfig.getBlock("tomatoCrop.id", FishAndFarmConfig.DEFAULT_ID_BLOCK_TOMATO);
			
			tomatoCrop = new BlockTomatoCrop(tomatoCropID.getInt()).setUnlocalizedName(ModInfo.MODID + "." + ModInfo.UNLOC_NAME_BLOCK_TOMATO).setTextureName("tomato"); 
			GameRegistry.registerBlock(tomatoCrop, ModInfo.MODID + ":" + ModInfo.UNLOC_NAME_BLOCK_TOMATO);
						
			// Strawberry Crop
			Property strawberryCropID = fishAndFarmConfig.getBlock("strawberryCrop.id", FishAndFarmConfig.DEFAULT_ID_BLOCK_STRAWBERRY);
			
			strawberryCrop = new BlockStrawberryCrop(strawberryCropID.getInt()).setUnlocalizedName(ModInfo.MODID + "." + ModInfo.UNLOC_NAME_BLOCK_STRAWBERRY).setTextureName("strawberry"); 
			GameRegistry.registerBlock(strawberryCrop, ModInfo.MODID + ":" + ModInfo.UNLOC_NAME_BLOCK_STRAWBERRY);
			
			// Seeds (AFTER CROPS!)
			Property seedsID = fishAndFarmConfig.getItem("seeds.id", FishAndFarmConfig.DEFAULT_ID_ITEM_SEEDS);
			
			seeds = new ItemCropSeeds(seedsID.getInt() - 256).setUnlocalizedName(ModInfo.MODID + "." + ModInfo.UNLOC_NAME_ITEM_SEEDS);
			GameRegistry.registerItem(seeds, ModInfo.MODID + ":" + ModInfo.UNLOC_NAME_ITEM_SEEDS);
			GameRegistry.addShapelessRecipe(new ItemStack(seeds, 1, 0), new Object[]{ new ItemStack(food, 1, 2) });
			GameRegistry.addShapelessRecipe(new ItemStack(seeds, 1, 1), new Object[]{ new ItemStack(food, 1, 3) });
			GameRegistry.addShapelessRecipe(new ItemStack(seeds, 1, 2), new Object[]{ new ItemStack(food, 1, 4) });

			// Stove
			Property stoveID = fishAndFarmConfig.getBlock("stove.id", FishAndFarmConfig.DEFAULT_ID_BLOCK_STOVE);
			
			stove = new BlockStove(stoveID.getInt()).setUnlocalizedName(ModInfo.MODID + "." + ModInfo.UNLOC_NAME_BLOCK_STOVE).setTextureName("stove"); 
			GameRegistry.registerBlock(stove, ModInfo.MODID + ":" + ModInfo.UNLOC_NAME_BLOCK_STOVE);
			GameRegistry.addRecipe(new ItemStack(stove, 1, 0), new Object[]{ "III", "S S", "SSS", 'I', Item.ingotIron, 'S', Block.stone});
			
			// Frying Pan
			Property fryingPanID = fishAndFarmConfig.getItem("fryingPan.id", FishAndFarmConfig.DEFAULT_ID_ITEM_FRYINGPAN);
			
			fryingPan = new ItemFryingPan(fryingPanID.getInt() - 256).setUnlocalizedName(ModInfo.MODID + "." + ModInfo.UNLOC_NAME_ITEM_FRYINGPAN).setTextureName("fryingPan"); 
			GameRegistry.registerItem(fryingPan, ModInfo.MODID + ":" + ModInfo.UNLOC_NAME_ITEM_FRYINGPAN);
			GameRegistry.addRecipe(new ItemStack(fryingPan, 1, 0), new Object[]{ "IIS", 'S', Item.stick, 'I', Item.ingotIron});
			
			// Iron Fishing Rod
			Property ironFishingRodID = fishAndFarmConfig.getItem("ironFishingRod.id", FishAndFarmConfig.DEFAULT_ID_ITEM_IRONFISHINGROD);
			
			ironFishingRod = new ItemIronFishingRod(ironFishingRodID.getInt() - 256).setUnlocalizedName(ModInfo.MODID + "." + ModInfo.UNLOC_NAME_ITEM_IRONFISHINGROD).setTextureName("ironFishingRod"); 
			GameRegistry.registerItem(ironFishingRod, ModInfo.MODID + ":" + ModInfo.UNLOC_NAME_ITEM_IRONFISHINGROD);
			GameRegistry.addRecipe(new ItemStack(ironFishingRod, 1, 0), new Object[]{ "  I", " IS", "I S", 'S', Item.silk, 'I', Item.ingotIron});
		
			// Empty
			Property emptyID = fishAndFarmConfig.getBlock("empty.id", FishAndFarmConfig.DEFAULT_ID_BLOCK_EMPTY);
			emptyID.comment = "Used in multi-structured blocks";
			
			empty = new BlockEmpty(emptyID.getInt()).setUnlocalizedName(ModInfo.MODID + "." + ModInfo.UNLOC_NAME_BLOCK_EMPTY).setTextureName("empty"); 
			GameRegistry.registerBlock(empty, ModInfo.MODID + ":" + ModInfo.UNLOC_NAME_BLOCK_EMPTY);
			
			// Smoker
			Property smokerID = fishAndFarmConfig.getBlock("smoker.id", FishAndFarmConfig.DEFAULT_ID_BLOCK_SMOKER);
			
			smoker = new BlockSmoker(smokerID.getInt()).setUnlocalizedName(ModInfo.MODID + "." + ModInfo.UNLOC_NAME_BLOCK_SMOKER).setTextureName("smoker"); 
			GameRegistry.registerBlock(smoker, ModInfo.MODID + ":" + ModInfo.UNLOC_NAME_BLOCK_SMOKER);
			GameRegistry.addRecipe(new ItemStack(smoker, 1, 0), new Object[]{ "PPP", "IPI", "PSP", 'P', Block.planks, 'I', Item.ingotIron, 'S', Block.stone});
			
			// Fish, overrides default fish
            fish = new ItemFish(Item.fishRaw.itemID - 256).setUnlocalizedName(ModInfo.MODID + "." + ModInfo.UNLOC_NAME_ITEM_FISH); 
            GameRegistry.registerItem(fish, ModInfo.MODID + ":" + ModInfo.UNLOC_NAME_ITEM_FISH);
            GameRegistry.addShapelessRecipe(new ItemStack(fish, 1, 12), new Object[]{ new ItemStack(fish, 1, 2), new ItemStack(knife, 1, OreDictionary.WILDCARD_VALUE)});
            GameRegistry.addShapelessRecipe(new ItemStack(fish, 1, 13), new Object[]{ new ItemStack(fish, 1, 3), new ItemStack(knife, 1, OreDictionary.WILDCARD_VALUE)});
            GameRegistry.addShapelessRecipe(new ItemStack(fish, 1, 14), new Object[]{ new ItemStack(fish, 1, 4), new ItemStack(knife, 1, OreDictionary.WILDCARD_VALUE)});
            
            // Remove cooked fish and change the recipe of cooked fish
            Item.itemsList[Item.fishCooked.itemID] = null;
            FurnaceRecipes.smelting().getSmeltingList().remove(Item.fishRaw.itemID);
            FurnaceRecipes.smelting().addSmelting(FishAndFarm.fish.itemID, 0, new ItemStack(FishAndFarm.fish, 1), 0.35F);
            
            // Stove
            Property cageID = fishAndFarmConfig.getBlock("cage.id", FishAndFarmConfig.DEFAULT_ID_BLOCK_CAGE);
            
            cage = new BlockCage(cageID.getInt()).setUnlocalizedName(ModInfo.MODID + "." + ModInfo.UNLOC_NAME_BLOCK_CAGE).setTextureName("cage"); 
            GameRegistry.registerBlock(cage, ModInfo.MODID + ":" + ModInfo.UNLOC_NAME_BLOCK_CAGE);
            GameRegistry.addRecipe(new ItemStack(cage, 1, 0), new Object[]{ "I", "B", "I", 'B', Block.fenceIron, 'I', Item.ingotIron});
		} finally {
			if(fishAndFarmConfig.hasChanged()) {
				fishAndFarmConfig.save();
			}
		}
	}
	
	@EventHandler
	public void init(FMLInitializationEvent evt) {
		// Load localizations
		Localization.addLocalization("/lang/fishandfarm/", "en_US");
		
		// Register tile entities
		GameRegistry.registerTileEntity(TileEntityStove.class, "tileEntityStove");
		GameRegistry.registerTileEntity(TileEntityEmpty.class, "tileEntityEmpty");
		GameRegistry.registerTileEntity(TileEntitySmoker.class, "tileEntitySmoker");
		GameRegistry.registerTileEntity(TileEntityCage.class, "tileEntityCageCage");
		
		// Register Handlers
		//GameRegistry.registerCraftingHandler(new CraftingHandler());
	    MinecraftForge.EVENT_BUS.register(new BonemealHandler());
		NetworkRegistry.instance().registerGuiHandler(this, new NetworkHandler());
        
        // Register entities
        EntityRegistry.registerModEntity(EntityIronFishHook.class, ModInfo.MODID + ":EntityIronFishHook", 0, this, 80, 1, true);
        EntityRegistry.registerModEntity(EntityCageBobber.class, ModInfo.MODID + ":EntityCageBobber", 1, this, 80, 1, true);
	
        // Register renders
		CommonProxy.proxy.registerRenders();
		
		// Temporary add seeds to grass
		MinecraftForge.addGrassSeed(new ItemStack(seeds, 1, 0), 1);
		MinecraftForge.addGrassSeed(new ItemStack(seeds, 1, 1), 1);
		MinecraftForge.addGrassSeed(new ItemStack(seeds, 1, 2), 1);
		
		// Add in fish, not sure yet if I want to keep it like this
		SlotFish.addAllowedFish(new ItemStack(FishAndFarm.fish, 1, 14));
		
		// Same as above but now for the cage baits
		SlotBait.addAllowedBait(new ItemStack(Item.beefRaw));
		SlotBait.addAllowedBait(new ItemStack(Item.porkRaw));
		SlotBait.addAllowedBait(new ItemStack(Item.chickenRaw));
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

