package machir.fishandfarm.item.crafting;

import machir.fishandfarm.FishAndFarm;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SmokerRecipes {
    public static ItemStack getResult(ItemStack itemstack)
    {
        if(itemstack.itemID == Item.fishRaw.itemID)
        {
            return new ItemStack(Item.fishCooked.itemID, 1, 0);
        }
        else
        {
            return null;
        }
    }
}
