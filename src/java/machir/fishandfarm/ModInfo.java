package machir.fishandfarm;

import net.minecraft.util.ResourceLocation;

public class ModInfo {

	/** Mod properties **/
	public static final String VERSION = "0.0.3";
	public static final String MODID = "fishandfarm";
    public static final String CHANNEL = "FishAndFarm";
	
	/** Unlocalized names **/
	// Items
	public static final String UNLOC_NAME_ITEM_FOOD = "food";
	public static final String UNLOC_NAME_ITEM_KNIFE = "knife";
	public static final String UNLOC_NAME_ITEM_SEEDS = "seeds";
	public static final String UNLOC_NAME_ITEM_FRYINGPAN = "fryingPan";
	public static final String UNLOC_NAME_ITEM_IRONFISHINGROD = "ironFishingRod";
	public static final String UNLOC_NAME_ITEM_FISH = "fish";
	
	// Blocks 
	public static final String UNLOC_NAME_BLOCK_LETTUCE = "lettuceCrop";
	public static final String UNLOC_NAME_BLOCK_TOMATO = "tomatoCrop";
	public static final String UNLOC_NAME_BLOCK_STRAWBERRY = "strawberryCrop";
	public static final String UNLOC_NAME_BLOCK_STOVE = "stove";
	public static final String UNLOC_NAME_BLOCK_EMPTY = "empty";
	public static final String UNLOC_NAME_BLOCK_SMOKER = "smoker";
    public static final String UNLOC_NAME_BLOCK_CAGE = "cage";
	
	// Tile entities
	public static final String UNLOC_NAME_TILEENTITY_STOVE = "stove";
	public static final String UNLOC_NAME_TILEENTITY_SMOKER = "smoker";
	public static final String UNLOC_NAME_TILEENTITY_CAGE = "cage";
	
	/** Resource locations **/ 
	// Models
	public static final ResourceLocation STOVE_MODEL_TEXTURE = new ResourceLocation(MODID + ":textures/models/stove.png");
	public static final ResourceLocation FRYINGPAN_MODEL_TEXTURE = new ResourceLocation(MODID + ":textures/models/fryingPan.png");
	public static final ResourceLocation SMOKER_MODEL_TEXTURE = new ResourceLocation(MODID + ":textures/models/smoker.png");
	public static final ResourceLocation CAGE_MODEL_TEXTURE = new ResourceLocation(MODID + ":textures/models/cage.png");
	
	// Guis
	public static final ResourceLocation STOVE_GUI_TEXTURE = new ResourceLocation(MODID + ":textures/guis/stove.png");
	public static final ResourceLocation SMOKER_GUI_TEXTURE = new ResourceLocation(MODID + ":textures/guis/smoker.png");
    public static final ResourceLocation CAGE_GUI_TEXTURE = new ResourceLocation(MODID + ":textures/guis/cage.png");
    
    // Entities
    public static final ResourceLocation CAGE_BOBBER_TEXTURE = new ResourceLocation(MODID + ":textures/entities/cageBobber.png");
    
}
