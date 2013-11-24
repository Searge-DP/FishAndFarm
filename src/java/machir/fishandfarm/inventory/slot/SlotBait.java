package machir.fishandfarm.inventory.slot;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;

public class SlotBait extends Slot {
    // Allowed bait
    private static List<ItemStack> allowedBait = new ArrayList();

    // The amount of items in the slot
    private int amount;

    public SlotBait(IInventory inventory, int x, int y, int z)
    {
        super(inventory, x, y, z);
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    @Override
    public boolean isItemValid(ItemStack itemStack)
    {
        for (int i = 0; i < allowedBait.size(); i++) {
            if (allowedBait.get(i).itemID == itemStack.itemID) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isBaitAllowed(ItemStack itemStack) {
        for (int i = 0; i < allowedBait.size(); i++) {
            if (allowedBait.get(i).itemID == itemStack.itemID) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Adds a new bait to the allowed bait
     * 
     * @param itemstack The bait to be allowed
     */
    public static void addAllowedBait(ItemStack itemstack) {
        allowedBait.add(itemstack);
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
}