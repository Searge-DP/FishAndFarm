package machir.fishandfarm.inventory.container;

import machir.fishandfarm.inventory.slot.SlotFish;
import machir.fishandfarm.inventory.slot.SlotSmokerFuel;
import machir.fishandfarm.tileentity.TileEntitySmoker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerSmoker extends Container {
    private TileEntitySmoker smoker;
    private int lastBurnTime;
    private int lastItemBurnTime;

    public ContainerSmoker(InventoryPlayer inventoryplayer, TileEntitySmoker tileentitysmoker)
    {
        lastBurnTime = 0;
        lastItemBurnTime = 0;
        smoker = tileentitysmoker;
        addSlotToContainer(new SlotSmokerFuel(tileentitysmoker, 0, 80, 59));
        addSlotToContainer(new SlotFish(inventoryplayer.player, tileentitysmoker, 1, 26, 21));
        addSlotToContainer(new SlotFish(inventoryplayer.player, tileentitysmoker, 2, 62, 21));
        addSlotToContainer(new SlotFish(inventoryplayer.player, tileentitysmoker, 3, 98, 21));
        addSlotToContainer(new SlotFish(inventoryplayer.player, tileentitysmoker, 4, 134, 21));
        for(int i = 0; i < 3; i++)
        {
            for(int k = 0; k < 9; k++)
            {
                addSlotToContainer(new Slot(inventoryplayer, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
            }
        }

        for(int j = 0; j < 9; j++)
        {
            addSlotToContainer(new Slot(inventoryplayer, j, 8 + j * 18, 142));
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting iCrafting)
    {
        super.addCraftingToCrafters(iCrafting);
        iCrafting.sendProgressBarUpdate(this, 0, this.smoker.smokerBurnTime);
        iCrafting.sendProgressBarUpdate(this, 1, this.smoker.currentItemBurnTime);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);
            if (this.lastBurnTime != this.smoker.smokerBurnTime)
            {
                icrafting.sendProgressBarUpdate(this, 0, this.smoker.smokerBurnTime);
            }

            if (this.lastItemBurnTime != this.smoker.currentItemBurnTime)
            {
                icrafting.sendProgressBarUpdate(this, 1, this.smoker.currentItemBurnTime);
            }
        }

        this.lastBurnTime = this.smoker.smokerBurnTime;
        this.lastItemBurnTime = this.smoker.currentItemBurnTime;
    }
    
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int progressBar, int progressBarValue)
    {
        if (progressBar == 0)
        {
            this.smoker.smokerBurnTime = progressBarValue;
        }

        if (progressBar == 1)
        {
            this.smoker.currentItemBurnTime = progressBarValue;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return smoker.isUseableByPlayer(entityplayer);
    }
    
    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int slotID)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(slotID);
        if(slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(slotID == 0)
            {
                if(!mergeItemStack(itemstack1, 5, 39, true))
                {
                    return null;
                }
            } else if (slotID >= 1 && slotID < 5) {
            	if(!mergeItemStack(itemstack1, 5, 39, false)) {
            		return null;
            	}
            } else
            if(slotID >= 5 && slotID < 30)
            {
            	if (((Slot)inventorySlots.get(1)).isItemValid(itemstack1)) {
	                if(!mergeItemStack(itemstack1, 1, 4, false))
	                {
	                    return null;
	                }
            	} else {
            		return null;
            	}
            } else
            if(slotID >= 30 && slotID < 39)
            {
                if(!mergeItemStack(itemstack1, 5, 30, false))
                {
                    return null;
                }
            } else
            if(!mergeItemStack(itemstack1, 5, 39, false))
            {
                return null;
            }
            if(itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            } else
            {
                slot.onSlotChanged();
            }
            if(itemstack1.stackSize != itemstack.stackSize)
            {
                slot.onPickupFromSlot(entityPlayer, itemstack1);
            } else
            {
                return null;
            }
        }
        return itemstack;
    }
}