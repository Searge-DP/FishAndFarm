package machir.fishandfarm.inventory.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;

public class SlotCage extends Slot {
    private EntityPlayer thePlayer;
    
    // The amount of items in the slot
    private int amount;

    public SlotCage(EntityPlayer player, IInventory inventory, int x, int y, int z)
    {
        super(inventory, x, y, z);
        thePlayer = player;
    }

    @Override
    public boolean isItemValid(ItemStack itemstack)
    {
        return false;
    }

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

    @Override
    protected void onCrafting(ItemStack itemstack, int amount)
    {
            this.amount += amount;
            onCrafting(itemstack);
    }

    @Override
    protected void onCrafting(ItemStack itemstack)
    {
        // Add the amount of processed items to the stats
        itemstack.onCrafting(thePlayer.worldObj, thePlayer, amount);
        amount = 0;
    }
}
