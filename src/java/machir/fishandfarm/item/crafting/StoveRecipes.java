package machir.fishandfarm.item.crafting;

import machir.fishandfarm.FishAndFarm;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class StoveRecipes {
	public static ItemStack getResult(ItemStack itemstack, ItemStack itemstack2)
    {
		if(itemstack.itemID == FishAndFarm.fryingPan.itemID && itemstack2.itemID == Item.egg.itemID)
        {
            return new ItemStack(FishAndFarm.food, 1, 1);
        }
		if(itemstack.itemID == FishAndFarm.fryingPan.itemID && itemstack2.itemID == Item.fishRaw.itemID)
        {
            return new ItemStack(Item.fishCooked, 1);
        }
        else
        {
        	return null;
        }
    }
}
