package machir.fishandfarm.inventory.slot;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;

public class SlotFish extends Slot {
	// Allowed fish
	private static List<ItemStack> allowedFish = new ArrayList();
	
	// Made this a static final to be able to use it in the tile entity with hoppers
	public static final int MAX_STACK_SIZE = 1;
	
	// Used for stat tracking
    private EntityPlayer thePlayer;
    
    // The amount of items in the slot
    private int amount;

    public SlotFish(EntityPlayer player, IInventory inventory, int x, int y, int z)
    {
        super(inventory, x, y, z);
        thePlayer = player;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    @Override
    public boolean isItemValid(ItemStack itemStack)
    {
        for (int i = 0; i < allowedFish.size(); i++) {
            if (allowedFish.get(i).itemID == itemStack.itemID) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isFishAllowed(ItemStack itemStack) {
        for (int i = 0; i < allowedFish.size(); i++) {
            if (allowedFish.get(i).itemID == itemStack.itemID) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Adds a new fish to the allowed fish
     * 
     * @param itemstack The fish to be allowed
     */
    public static void addAllowedFish(ItemStack itemstack) {
    	allowedFish.add(itemstack);
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    @Override
    public ItemStack decrStackSize(int amount)
    {
        if (getHasStack())
        {
            amount += Math.min(amount, getStack().stackSize);
        }

        return super.decrStackSize(amount);
    }

    @Override
    public void onPickupFromSlot(EntityPlayer entityPlayer, ItemStack itemstack)
    {
        onCrafting(itemstack);
        super.onPickupFromSlot(entityPlayer, itemstack);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
     * internal count then calls onCrafting(item).
     */
    @Override
    protected void onCrafting(ItemStack itemstack, int amount)
    {
        this.amount += amount;
        onCrafting(itemstack);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
     */
    @Override
    protected void onCrafting(ItemStack itemstack)
    {
        // Add the amount of processed items to the stats
        itemstack.onCrafting(thePlayer.worldObj, thePlayer, amount);
        amount = 0;

        // Default cook fish achievement
        if (itemstack.itemID == Item.fishCooked.itemID)
        {
            thePlayer.addStat(AchievementList.cookFish, 1);
        }
    }
    
    /**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case
     * of armor slots)
     */
    public int getSlotStackLimit()
    {
    	return this.MAX_STACK_SIZE;
    }
}