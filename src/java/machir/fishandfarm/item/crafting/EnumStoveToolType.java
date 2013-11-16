package machir.fishandfarm.item.crafting;

public class EnumStoveToolType {
	public enum StoveToolType {
		NONE,
		FRYINGPAN,
		COOKINGPAN
	}	
	
	public static int getIntFromTool(StoveToolType toolType) {
		switch(toolType) {
		case FRYINGPAN:
			return 1;
		case COOKINGPAN:
			return 2;
		default:
			return 0;
		}
	}
		
	public static StoveToolType getToolFromInt(int toolType) {
		switch(toolType) {
		case 1:
			return StoveToolType.FRYINGPAN;
		case 2:
			return StoveToolType.COOKINGPAN;
		default:
			return StoveToolType.NONE;
		}
	}
}
