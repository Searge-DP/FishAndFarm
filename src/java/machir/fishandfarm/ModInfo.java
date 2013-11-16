package machir.fishandfarm;

import net.minecraft.util.ResourceLocation;

public class ModInfo {

	// current mod version and id
	public static final String VERSION = "0.0.2";
	public static final String MODID = "fishandfarm";
	public static final String CHANNEL = "FishAndFarm";
	
	// unlocalized names
	public static final String UNLOC_NAME_ITEM_FOOD = "food";
	public static final String UNLOC_NAME_ITEM_KNIFE = "knife";
	public static final String UNLOC_NAME_ITEM_SEEDS = "seeds";
	public static final String UNLOC_NAME_ITEM_FRYINGPAN = "fryingPan";
	
	public static final String UNLOC_NAME_BLOCK_LETTUCE = "lettuceCrop";
	public static final String UNLOC_NAME_BLOCK_TOMATO = "tomatoCrop";
	public static final String UNLOC_NAME_BLOCK_STRAWBERRY = "strawberryCrop";
	public static final String UNLOC_NAME_BLOCK_STOVE = "stove";
	
	public static final String UNLOC_NAME_TILEENTITY_STOVE = "stove";
	
	// Resource locations
	public static final ResourceLocation STOVE_MODEL_TEXTURE = new ResourceLocation(MODID + ":textures/models/stove.png");
	public static final ResourceLocation FRYINGPAN_MODEL_TEXTURE = new ResourceLocation(MODID + ":textures/models/fryingPan.png");
	public static final ResourceLocation STOVE_GUI_TEXTURE = new ResourceLocation(MODID + ":textures/gui/stove.png");
}
