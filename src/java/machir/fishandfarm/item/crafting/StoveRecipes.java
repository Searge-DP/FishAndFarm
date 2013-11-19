package machir.fishandfarm.item.crafting;

import machir.fishandfarm.FishAndFarm;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class StoveRecipes {
    public static ItemStack getResult(ItemStack itemstack, ItemStack itemstack2)
    {
        if(itemstack.itemID == FishAndFarm.fryingPan.itemID && itemstack2.itemID == Item.egg.itemID)
        {
            return new ItemStack(FishAndFarm.food, 1, 0);
        }
        if(itemstack.itemID == FishAndFarm.fryingPan.itemID && itemstack2.itemID == Item.fishRaw.itemID)
        {
            return new ItemStack(Item.fishCooked, 1);
        }
        if(itemstack.itemID == FishAndFarm.fryingPan.itemID && itemstack2.equals(new ItemStack(FishAndFarm.fish, 1, 0)))
        {
            return new ItemStack(FishAndFarm.fish, 1, 3);
        }
        if(itemstack.itemID == FishAndFarm.fryingPan.itemID && itemstack2.equals(new ItemStack(FishAndFarm.fish, 1, 1)))
        {
            return new ItemStack(FishAndFarm.fish, 1, 4);
        }
        if(itemstack.itemID == FishAndFarm.fryingPan.itemID && itemstack2.equals(new ItemStack(FishAndFarm.fish, 1, 2)))
        {
            return new ItemStack(FishAndFarm.fish, 1, 5);
        }
        else
        {
            return null;
        }
    }
}