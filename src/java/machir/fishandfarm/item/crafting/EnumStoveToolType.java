package machir.fishandfarm.item.crafting;

public class EnumStoveToolType {
	public enum StoveToolType {
		FRYINGPAN,
		COOKINGPAN,
	}	
	
	public static int getIntFromTool(StoveToolType toolType) {
		switch(toolType) {
		case FRYINGPAN:
			return 0;
		case COOKINGPAN:
			return 1;
		default:
			return -1;
		}
	}
		
	public static StoveToolType getToolFromInt(int toolType) {
		switch(toolType) {
		case 0:
			return StoveToolType.FRYINGPAN;
		case 1:
			return StoveToolType.COOKINGPAN;
		default:
			return null;
		}
	}
}
