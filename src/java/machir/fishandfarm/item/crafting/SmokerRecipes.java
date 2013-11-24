package machir.fishandfarm.item.crafting;

import machir.fishandfarm.FishAndFarm;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SmokerRecipes {
    public static ItemStack getResult(ItemStack itemstack)
    {
        if(itemstack.itemID == FishAndFarm.fish.itemID && itemstack.getItemDamage() == 14)
        {
            return new ItemStack(FishAndFarm.fish, 1, 15);
        }
        else
        {
            return null;
        }
    }
}
