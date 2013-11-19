package machir.fishandfarm.tileentity;

import machir.fishandfarm.ModInfo;
import machir.fishandfarm.inventory.slot.SlotFish;
import machir.fishandfarm.item.crafting.SmokerRecipes;
import machir.fishandfarm.packet.TileEntityPacket;
import machir.fishandfarm.util.Localization;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntitySmoker extends TileEntity implements ISidedInventory {
	// The smoke duration
	private static final int SMOKE_DURATION = 600;
	
	// The itemstacks inside
	private ItemStack smokerItemStacks[];
	
	// Hopper config
	private static final int[] processSlots = new int[] { 1, 2, 3, 4 };
	// The burn times
    public int smokerBurnTime;
    public int currentItemBurnTime;
    public int smokerTimes[];
    
    public TileEntitySmoker()
    {
    	// 9 Slots, 1 fuel, 4 food
    	smokerItemStacks = new ItemStack[5];
        
    	smokerBurnTime = 0;
        currentItemBurnTime = 0;
        smokerTimes = new int[4];
    }
    
    /**
     * Returns the number of slots in the inventory.
     */
    @Override
    public int getSizeInventory()
    {
        return smokerItemStacks.length;
    }

    /**
     * Returns the stack in slot
     */
    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return smokerItemStacks[slot];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    @Override
    public ItemStack decrStackSize(int slot, int amount)
    {
        if(smokerItemStacks[slot] != null)
        {
            if(smokerItemStacks[slot].stackSize <= amount)
            {
                ItemStack itemstack = smokerItemStacks[slot];
                smokerItemStacks[slot] = null;
                return itemstack;
            }
            ItemStack itemstack1 = smokerItemStacks[slot].splitStack(amount);
            if(smokerItemStacks[slot].stackSize == 0)
            {
            	smokerItemStacks[slot] = null;
            }
            return itemstack1;
        } else
        {
            return null;
        }
    }
    
    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    @Override
	public ItemStack getStackInSlotOnClosing(int slot) {
        if (smokerItemStacks[slot] != null)
        {
            ItemStack itemstack = smokerItemStacks[slot];
            smokerItemStacks[slot] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
	}

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack)
    {
    	smokerItemStacks[slot] = itemStack;
        if(itemStack != null && itemStack.stackSize > getInventoryStackLimit())
        {
        	itemStack.stackSize = getInventoryStackLimit();
        }
    }

    /**
     * Returns the name of the inventory.
     */
    @Override
    public String getInvName()
    {
        return Localization.get(ModInfo.MODID + ".tileentity." + ModInfo.UNLOC_NAME_TILEENTITY_SMOKER + ".inv_name");
    }

    /**
     * Reads a tile entity from NBT.
     */
    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        smokerItemStacks = new ItemStack[getSizeInventory()];
        for(int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            byte slot = nbttagcompound1.getByte("Slot");
            if(slot >= 0 && slot < smokerItemStacks.length)
            {
            	smokerItemStacks[slot] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
                
        smokerBurnTime = nbttagcompound.getShort("BurnTime");
        smokerTimes = nbttagcompound.getIntArray("CookTime");
        currentItemBurnTime = nbttagcompound.getShort("ItemBurnTime");
    }

    /**
     * Writes a tile entity to NBT.
     */
    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("BurnTime", (short)smokerBurnTime);
        nbttagcompound.setIntArray("CookTime", smokerTimes);
        nbttagcompound.setShort("ItemBurnTime", (short)currentItemBurnTime);
        NBTTagList nbttaglist = new NBTTagList();
        for(int slot = 0; slot < smokerItemStacks.length; slot++)
        {
            if(smokerItemStacks[slot] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)slot);
                smokerItemStacks[slot].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        nbttagcompound.setTag("Items", nbttaglist);
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }
    
    /**
     * Returns an integer between 0 and the passed value representing how much burn time is left on the current fuel
     * item, where 0 means that the item is exhausted and the passed value means that the item is fresh
     */
    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int i)
    {
        if(currentItemBurnTime == 0)
        {
            currentItemBurnTime = 200;
        }
        return smokerBurnTime * i / currentItemBurnTime;
    }

    /**
     * Returns true if the stove is currently burning
     */
    public boolean isBurning()
    {
        return smokerBurnTime > 0;
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    @Override
    public void updateEntity()
    {
        boolean burning = smokerBurnTime > 0;
        boolean changedActivity = false;
        
        // If it's burning, decrease the burn time left
        if(smokerBurnTime > 0)
        {
        	smokerBurnTime--;
        }    
        
        if(!worldObj.isRemote)
        {
        	// If it's not burning but can process, try to use fuel
            if(smokerBurnTime == 0 && (canProcess(1) || canProcess(2)
     		 	   				   ||  canProcess(3) || canProcess(4))) {
            	// Add the fuel to the stove
                currentItemBurnTime = smokerBurnTime = getItemBurnTime(smokerItemStacks[0]);
                if(smokerBurnTime > 0)
                {
                    smokerItemStacks[0].stackSize--;
                    
                	// Changed from inactive to active
                	changedActivity = true;
                }
            }
            // If it is burning and it can process
            if(isBurning() && (canProcess(1) || canProcess(2)
            		 	   ||  canProcess(3) || canProcess(4))) {
            	// Increase the cooking time
            	for (int i = 0; i < smokerTimes.length; i++) {
            		if (smokerItemStacks[i + 1] != null && smokerTimes[i] < this.SMOKE_DURATION) {
            			smokerTimes[i]++;
            		}
            		
                    // If it has reached it's end process the item
                    // And mark the inventory for change
                    if(smokerTimes[i] == this.SMOKE_DURATION) {
                        processItem(i + 1);
                        changedActivity = true;
                    }
            	}
            }
            
            // If the previous state of burning is not the same as the current (e.g Was burning but no longer),
            // mark for inventory change
            if(burning != (smokerBurnTime > 0))
            {
                changedActivity = true;
            }
        }
        if(changedActivity)
        {
            onInventoryChanged();
        }
    }
    
    @Override
    public void onInventoryChanged() {
    	super.onInventoryChanged();
        for (int i = 0; i < smokerTimes.length; i++) {
            if (smokerItemStacks[i + 1] == null) {
                smokerTimes[i] = 0;
            }
        }
    	sendPacket();
    }
    
    public void sendPacket() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		this.writeToNBT(nbttagcompound);
    	if (this.worldObj.isRemote) {
    		PacketDispatcher.sendPacketToServer(new TileEntityPacket(nbttagcompound).makePacket());
    	} else {
    		PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 16, 0, new TileEntityPacket(nbttagcompound).makePacket());
    	}
    }

    /**
     * Returns whether the current item can be processed or not
     * 
     * @return Can process?
     */
    private boolean canProcess(int slot)
    {
    	// If the process slot or the tool slot is empty, return false
        if(smokerItemStacks[slot] == null) {
            return false;
        }
        
        // Get the result corresponding to the current input
        ItemStack result = SmokerRecipes.getResult(smokerItemStacks[slot]);
        
        // If there is not result return false
        if(result == null)
        {       
            return false;
        }
        
        // If none of the above returned anything, return true 
        return true;
    }

    /**
     * Processes the item
     */
    public void processItem(int slot)
    {
    	// If the item cannot be processed, return
        if(!canProcess(slot))
        {
            return;
        }
        // Get the result corresponding to the current input
        ItemStack result = SmokerRecipes.getResult(smokerItemStacks[slot]);
     
      	// Set the output
        smokerItemStacks[slot] = result;
    }

    /**
     * Returns the amount of burn time a certain item gives
     * 
     * @param itemstack The fuel
     * @return The amount of burn time
     */
    private static int getItemBurnTime(ItemStack itemstack)
    {
    	//  If no fuel is inserted, return 0
        if(itemstack == null)
        {
            return 0;
        }
        else
        {
            int i = itemstack.getItem().itemID;
            Item item = itemstack.getItem();

            if (itemstack.getItem() instanceof ItemBlock && Block.blocksList[i] != null)
            {
                Block block = Block.blocksList[i];

                if (block == Block.woodSingleSlab)
                {
                    return 150;
                }

                if (block.blockMaterial == Material.wood)
                {
                    return 300;
                }
            }

            if (item instanceof ItemTool && ((ItemTool) item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemSword && ((ItemSword) item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemHoe && ((ItemHoe) item).getMaterialName().equals("WOOD")) return 200;
            if (i == Item.stick.itemID) return 100;
            if (i == Block.sapling.blockID) return 100;
            return 0;
        }
    }
    
    /**
     * Return true if item is a fuel source (getItemBurnTime() > 0).
     */
    public static boolean isItemFuel(ItemStack itemStack)
    {
        return getItemBurnTime(itemStack) > 0;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }

        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }
    
    /**
     * Called when you receive a TileEntityData packet for the location this
     * TileEntity is currently in. On the client, the NetworkManager will always
     * be the remote server. On the server, it will be whomever is responsible for
     * sending the packet.
     *
     * @param net The NetworkManager the packet originated from
     * @param packet The data packet
     */
    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData packet)
    {
    	this.readFromNBT(packet.data);
    }
    
    /**
     * Overriden in a sign to provide the text.
     */
    @Override
    public Packet getDescriptionPacket()
    {
    	NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, -1, nbttagcompound);
    }
    
    /**
     * Gets called on opening
     */
    @Override
    public void openChest()
    {
    }
    
    /**
     * Gets called on closing
     */
    @Override
    public void closeChest()
    {
    }

    /**
     * If this returns false, the inventory name will be used as an unlocalized name, and translated into the player's
     * language. Otherwise it will be used directly.
     */
	@Override
	public boolean isInvNameLocalized() {
		return true;
	}

	/**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack)
    {
        // If it's a fuel slot check for a valid fuel, if not, check if the fish is allowed
        return slot == 0 ? isItemFuel(itemStack) : SlotFish.isFishAllowed(itemStack);
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
        // The bottom has the process slots accessible, the top too and the side will be used for fuel
        return side == 0 ? processSlots : (side == 1 ? processSlots : new int[] {0});
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
        // First do a check if the item is valid for the requested slot, if so, check if the side is bigger than 0 (i.e. non-fuel)
        // Then if it's true check if the slot is empty so the itemstack can fit in
        int spaceLeft = 0;
        if (slot > 0 && this.getStackInSlot(slot) == null) {
            // Which is one but to clarify why I use 1 I put the constant there
            spaceLeft = SlotFish.MAX_STACK_SIZE; 
        }
        return this.isItemValidForSlot(slot, itemStack) && (slot > 0 ? spaceLeft > 0 : true);
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
        // If the side is on the bottom and the requested slot is bigger than 0 (i.e. non-fuel)
        // Check if the fish is ready, if so, extract it
    	if (side == 0 && slot > 0) {
    		if (smokerTimes[slot - 1] == 600) {
    		    return true;
    		} else {
    		    return false;
    		}
    	} else {
    		return false;
    	} 
    }
}
