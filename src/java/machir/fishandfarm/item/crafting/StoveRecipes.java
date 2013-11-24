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
        if(itemstack.itemID == FishAndFarm.fryingPan.itemID && (itemstack2.itemID == FishAndFarm.fish.itemID && itemstack2.getItemDamage() == 0))
        {
            return new ItemStack(FishAndFarm.fish, 1, 1);
        }
        if(itemstack.itemID == FishAndFarm.fryingPan.itemID && (itemstack2.itemID == FishAndFarm.fish.itemID && itemstack2.getItemDamage() == 12))
        {
            return new ItemStack(FishAndFarm.fish, 1, 5);
        }
        if(itemstack.itemID == FishAndFarm.fryingPan.itemID && (itemstack2.itemID == FishAndFarm.fish.itemID && itemstack2.getItemDamage() == 13))
        {
            return new ItemStack(FishAndFarm.fish, 1, 6);
        }
        if(itemstack.itemID == FishAndFarm.fryingPan.itemID && (itemstack2.itemID == FishAndFarm.fish.itemID && itemstack2.getItemDamage() == 14))
        {
            return new ItemStack(FishAndFarm.fish, 1, 7);
        }
        else
        {
            return null;
        }
    }
}