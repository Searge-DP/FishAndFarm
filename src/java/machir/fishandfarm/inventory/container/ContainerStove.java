package machir.fishandfarm.inventory.container;

import machir.fishandfarm.inventory.slot.SlotStove;
import machir.fishandfarm.inventory.slot.SlotTool;
import machir.fishandfarm.packet.StovePacket;
import machir.fishandfarm.tileentity.TileEntityStove;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerStove extends Container {
	private TileEntityStove stove;
    private int lastCookTime;
    private int lastBurnTime;
    private int lastItemBurnTime;

    public ContainerStove(InventoryPlayer inventoryplayer, TileEntityStove tileentitystove)
    {
        lastCookTime = 0;
        lastBurnTime = 0;
        lastItemBurnTime = 0;
        stove = tileentitystove;
        addSlotToContainer(new SlotTool(tileentitystove, 0, 20, 17));
        addSlotToContainer(new Slot(tileentitystove, 1, 56, 17));
        addSlotToContainer(new Slot(tileentitystove, 2, 56, 53));
        addSlotToContainer(new SlotStove(inventoryplayer.player, tileentitystove, 3, 116, 35));
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
        iCrafting.sendProgressBarUpdate(this, 0, this.stove.stoveCookTime);
        iCrafting.sendProgressBarUpdate(this, 1, this.stove.stoveBurnTime);
        iCrafting.sendProgressBarUpdate(this, 2, this.stove.currentItemBurnTime);
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

            if (this.lastCookTime != this.stove.stoveCookTime)
            {
                icrafting.sendProgressBarUpdate(this, 0, this.stove.stoveCookTime);
            }

            if (this.lastBurnTime != this.stove.stoveBurnTime)
            {
                icrafting.sendProgressBarUpdate(this, 1, this.stove.stoveBurnTime);
            }

            if (this.lastItemBurnTime != this.stove.currentItemBurnTime)
            {
                icrafting.sendProgressBarUpdate(this, 2, this.stove.currentItemBurnTime);
            }
        }

        this.lastCookTime = this.stove.stoveCookTime;
        this.lastBurnTime = this.stove.stoveBurnTime;
        this.lastItemBurnTime = this.stove.currentItemBurnTime;
    }
    
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int progressBar, int progressBarValue)
    {
        if (progressBar == 0)
        {
            this.stove.stoveCookTime = progressBarValue;
        }

        if (progressBar == 1)
        {
            this.stove.stoveBurnTime = progressBarValue;
        }

        if (progressBar == 2)
        {
            this.stove.currentItemBurnTime = progressBarValue;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return stove.isUseableByPlayer(entityplayer);
    }
    
    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(i);
        if(slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(i == 2)
            {
                if(!mergeItemStack(itemstack1, 4, 39, true))
                {
                    return null;
                }
            } else
            if(i >= 3 && i < 30)
            {
                if(!mergeItemStack(itemstack1, 30, 39, false))
                {
                    return null;
                }
            } else
            if(i >= 30 && i < 39)
            {
                if(!mergeItemStack(itemstack1, 4, 30, false))
                {
                    return null;
                }
            } else
            if(!mergeItemStack(itemstack1, 4, 39, false))
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
