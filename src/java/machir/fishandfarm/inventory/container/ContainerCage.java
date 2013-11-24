package machir.fishandfarm.inventory.container;

import machir.fishandfarm.inventory.slot.SlotBait;
import machir.fishandfarm.inventory.slot.SlotCage;
import machir.fishandfarm.tileentity.TileEntityCage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerCage extends Container {
    private TileEntityCage cage;
    private int lastProcessTime;

    public ContainerCage(InventoryPlayer inventoryplayer, TileEntityCage tileentitycage)
    {
        lastProcessTime = 0;
        cage = tileentitycage;
        addSlotToContainer(new SlotBait(tileentitycage, 0, 56, 35));
        addSlotToContainer(new SlotCage(inventoryplayer.player, tileentitycage, 1, 116, 35));
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
        iCrafting.sendProgressBarUpdate(this, 0, this.cage.cageProcessTime);
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
            if (this.lastProcessTime != this.cage.cageProcessTime)
            {
                icrafting.sendProgressBarUpdate(this, 0, this.cage.cageProcessTime);
            }
        }

        this.lastProcessTime = this.cage.cageProcessTime;
    }
    
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int progressBar, int progressBarValue)
    {
        if (progressBar == 0)
        {
            this.cage.cageProcessTime = progressBarValue;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return cage.isUseableByPlayer(entityplayer);
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
            if(slotID == 0 || slotID == 1)
            {
                if(!mergeItemStack(itemstack1, 2, 37, true))
                {
                    return null;
                }
            } else if(slotID > 1 && slotID < 30) {
                if (TileEntityCage.isItemBait(itemstack1)) {
                    if(!mergeItemStack(itemstack1, 1, 1, false))
                    {
                        return null;
                    }
                } else {
                    return null;
                }
            } else
            if(slotID >= 30 && slotID < 37)
            {
                if(!mergeItemStack(itemstack1, 2, 30, false))
                {
                    return null;
                }
            } else
            if(!mergeItemStack(itemstack1, 2, 37, false))
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
