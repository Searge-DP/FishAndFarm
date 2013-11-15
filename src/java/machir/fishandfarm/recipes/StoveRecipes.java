package machir.fishandfarm.recipes;

import machir.fishandfarm.FishAndFarm;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class StoveRecipes {
	public static ItemStack getResult(ItemStack itemstack, ItemStack itemstack2)
    {
		if(itemstack2.itemID == Item.egg.itemID)
        {
            return new ItemStack(FishAndFarm.food, 1, 1);
        }
        else
        {
        	return null;
        }
    }
}
