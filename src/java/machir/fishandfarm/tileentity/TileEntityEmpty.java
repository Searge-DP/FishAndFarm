package machir.fishandfarm.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityEmpty extends TileEntity implements ISidedInventory {
	public int coreX;
	public int coreY;
	public int coreZ;
	
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("coreX", coreX);
        par1NBTTagCompound.setInteger("coreY", coreY);
        par1NBTTagCompound.setInteger("coreZ", coreZ);
    }
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.coreX = par1NBTTagCompound.getInteger("coreX");
        this.coreY = par1NBTTagCompound.getInteger("coreY");
        this.coreZ = par1NBTTagCompound.getInteger("coreZ");
    }
    
	@Override
	public int getSizeInventory() {
		TileEntity tileEntity = worldObj.getBlockTileEntity(coreX, coreY, coreZ);
		if (tileEntity != null && tileEntity instanceof IInventory) {
			return ((IInventory)tileEntity).getSizeInventory();
		}		
		return 0;
	}
	
	@Override
	public ItemStack getStackInSlot(int i) {
		TileEntity tileEntity = worldObj.getBlockTileEntity(coreX, coreY, coreZ);
		if (tileEntity != null && tileEntity instanceof IInventory) {
			return ((IInventory)tileEntity).getStackInSlot(i);
		}		
		return null;
	}
	
	@Override
	public ItemStack decrStackSize(int i, int j) {
		TileEntity tileEntity = worldObj.getBlockTileEntity(coreX, coreY, coreZ);
		if (tileEntity != null && tileEntity instanceof IInventory) {
			return ((IInventory)tileEntity).decrStackSize(i, j);
		}		
		return null;
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		TileEntity tileEntity = worldObj.getBlockTileEntity(coreX, coreY, coreZ);
		if (tileEntity != null && tileEntity instanceof IInventory) {
			return ((IInventory)tileEntity).getStackInSlotOnClosing(i);
		}		
		return null;
	}
	
	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		TileEntity tileEntity = worldObj.getBlockTileEntity(coreX, coreY, coreZ);
		if (tileEntity != null && tileEntity instanceof IInventory) {
			((IInventory)tileEntity).setInventorySlotContents(i, itemstack);
		}		
	}
	
	@Override
	public String getInvName() {
		TileEntity tileEntity = worldObj.getBlockTileEntity(coreX, coreY, coreZ);
		if (tileEntity != null && tileEntity instanceof IInventory) {
			return ((IInventory)tileEntity).getInvName();
		}	
		return null;
	}
	
	@Override
	public boolean isInvNameLocalized() {
		TileEntity tileEntity = worldObj.getBlockTileEntity(coreX, coreY, coreZ);
		if (tileEntity != null && tileEntity instanceof IInventory) {
			return ((IInventory)tileEntity).isInvNameLocalized();
		}	
		return false;
	}
	
	@Override
	public int getInventoryStackLimit() {
		TileEntity tileEntity = worldObj.getBlockTileEntity(coreX, coreY, coreZ);
		if (tileEntity != null && tileEntity instanceof IInventory) {
			return ((IInventory)tileEntity).getInventoryStackLimit();
		}	
		return 0;
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		TileEntity tileEntity = worldObj.getBlockTileEntity(coreX, coreY, coreZ);
		if (tileEntity != null && tileEntity instanceof IInventory) {
			return ((IInventory)tileEntity).isUseableByPlayer(entityplayer);
		}
		return false;
	}
	
	@Override
	public void openChest() {
		TileEntity tileEntity = worldObj.getBlockTileEntity(coreX, coreY, coreZ);
		if (tileEntity != null && tileEntity instanceof IInventory) {
			((IInventory)tileEntity).openChest();
		}		
	}
	
	@Override
	public void closeChest() {
		TileEntity tileEntity = worldObj.getBlockTileEntity(coreX, coreY, coreZ);
		if (tileEntity != null && tileEntity instanceof IInventory) {
			((IInventory)tileEntity).closeChest();
		}				
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		TileEntity tileEntity = worldObj.getBlockTileEntity(coreX, coreY, coreZ);
		if (tileEntity != null && tileEntity instanceof IInventory) {
			return ((IInventory)tileEntity).isItemValidForSlot(i, itemstack);
		}
		return false;
	}
	
	/**
     * Returns an array containing the indices of the slots that can be accessed by automation on the given side of this
     * block.
     * 
 	 * @param side The side which will be accessed
     */
    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
		TileEntity tileEntity = worldObj.getBlockTileEntity(coreX, coreY, coreZ);
		if (tileEntity != null && tileEntity instanceof IInventory) {
			return ((ISidedInventory)tileEntity).getAccessibleSlotsFromSide(side);
		}
		return null;
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side. 
     * 
     * @param slot The requested slot
     * @param itemStack The itemstack to be inserted
     * @param side The input side
     */
    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, int side)
    {
        TileEntity tileEntity = worldObj.getBlockTileEntity(coreX, coreY, coreZ);
        if (tileEntity != null && tileEntity instanceof IInventory) {
            return ((ISidedInventory)tileEntity).canInsertItem(slot, itemStack, side);
        }
        return false;
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side
     * 
     * @param slot The requested slot
     * @param itemStack The itemStack to be retrieved
     * @param side The output side
     */
    @Override
    public boolean canExtractItem(int slot, ItemStack itemStack, int side)
    {
        TileEntity tileEntity = worldObj.getBlockTileEntity(coreX, coreY, coreZ);
        if (tileEntity != null && tileEntity instanceof IInventory) {
            return ((ISidedInventory)tileEntity).canExtractItem(slot, itemStack, side);
        }
        return false;
    }
}
