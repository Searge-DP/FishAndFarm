package machir.fishandfarm.tileentity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import machir.fishandfarm.ModInfo;
import machir.fishandfarm.item.crafting.EnumStoveToolType;
import machir.fishandfarm.item.crafting.EnumStoveToolType.StoveToolType;
import machir.fishandfarm.item.crafting.IStoveTool;
import machir.fishandfarm.item.crafting.StoveRecipes;
import machir.fishandfarm.util.Localization;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityStove extends TileEntity implements IInventory {
	// The itemstacks inside
	private ItemStack stoveItemStacks[];
	
	// The burn times
    public int stoveBurnTime;
    public int currentItemBurnTime;
    public int stoveCookTime;
    
    // Which tool is used
    public StoveToolType tool;
    
    public TileEntityStove()
    {
    	// 4 Slots, 1 tool, 1 food, 1 fuel, 1 output
        stoveItemStacks = new ItemStack[4];
        
        stoveBurnTime = 0;
        currentItemBurnTime = 0;
        stoveCookTime = 0;
        
        // Default, no tool used
        tool = null;
    }
    
    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return stoveItemStacks.length;
    }

    /**
     * Returns the stack in slot
     */
    public ItemStack getStackInSlot(int slot)
    {
        return stoveItemStacks[slot];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int slot, int amount)
    {
        if(stoveItemStacks[slot] != null)
        {
            if(stoveItemStacks[slot].stackSize <= amount)
            {
                ItemStack itemstack = stoveItemStacks[slot];
                stoveItemStacks[slot] = null;
                return itemstack;
            }
            ItemStack itemstack1 = stoveItemStacks[slot].splitStack(amount);
            if(stoveItemStacks[slot].stackSize == 0)
            {
                stoveItemStacks[slot] = null;
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
	public ItemStack getStackInSlotOnClosing(int slot) {
        if (stoveItemStacks[slot] != null)
        {
            ItemStack itemstack = stoveItemStacks[slot];
            stoveItemStacks[slot] = null;
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
    public void setInventorySlotContents(int slot, ItemStack itemStack)
    {
        stoveItemStacks[slot] = itemStack;
        if(itemStack != null && itemStack.stackSize > getInventoryStackLimit())
        {
        	itemStack.stackSize = getInventoryStackLimit();
        }
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return Localization.get(ModInfo.MODID + ".tileentity." + ModInfo.UNLOC_NAME_TILEENTITY_STOVE + ".inv_name");
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        stoveItemStacks = new ItemStack[getSizeInventory()];
        for(int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            byte slot = nbttagcompound1.getByte("Slot");
            if(slot >= 0 && slot < stoveItemStacks.length)
            {
                stoveItemStacks[slot] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
        
        stoveBurnTime = nbttagcompound.getShort("BurnTime");
        stoveCookTime = nbttagcompound.getShort("CookTime");
        currentItemBurnTime = getItemBurnTime(stoveItemStacks[1]);
        
        // Get the tool from the nbttagcompound
        tool = EnumStoveToolType.getToolFromInt(nbttagcompound.getInteger("Tool"));
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("BurnTime", (short)stoveBurnTime);
        nbttagcompound.setShort("CookTime", (short)stoveCookTime);
        NBTTagList nbttaglist = new NBTTagList();
        for(int slot = 0; slot < stoveItemStacks.length; slot++)
        {
            if(stoveItemStacks[slot] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)slot);
                stoveItemStacks[slot].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        nbttagcompound.setTag("Items", nbttaglist);
        if (tool != null) nbttagcompound.setInteger("Tool", EnumStoveToolType.getIntFromTool(tool));
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
     * Returns an integer between 0 and the passed value representing how close the current item is to being completely
     * cooked
     */
    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int i)
    {
        return (stoveCookTime * i) / 200;
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
        return (stoveBurnTime * i) / currentItemBurnTime;
    }

    /**
     * Returns true if the stove is currently burning
     */
    public boolean isBurning()
    {
        return stoveBurnTime > 0;
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        boolean burning = stoveBurnTime > 0;
        boolean changedActivity = false;
        
        // If it's burning, descrease the burn time left
        if(stoveBurnTime > 0)
        {
            stoveBurnTime--;
        }    
        
        // If a tool was used, update the tool string
        if(stoveItemStacks[0] != null)
        {
        	this.tool = (stoveItemStacks[0].getItem() instanceof IStoveTool ? ((IStoveTool)stoveItemStacks[0].getItem()).getToolType() : null);
        }
        // No tool was used, empty the tool string
        else
        {
        	this.tool = null;
        }
        
        if(!worldObj.isRemote)
        {
        	// If it's not burning but can process, try to use fuel
            if(stoveBurnTime == 0 && canProcess())
            {
            	// Add the fuel to the stove
                currentItemBurnTime = stoveBurnTime = getItemBurnTime(stoveItemStacks[2]);
                if(stoveBurnTime > 0)
                {
                	// Changed from inactive to active
                	changedActivity = true;
                	
                	// If the item to be processed is not empty,
                	// Check if it has a container item, if not decrease the stack size
                    if(stoveItemStacks[2] != null)
                    {
                        if(stoveItemStacks[2].getItem().hasContainerItem())
                        {
                            stoveItemStacks[2] = new ItemStack(stoveItemStacks[2].getItem().getContainerItem());
                        } else {
                            stoveItemStacks[2].stackSize--;
                        }
                        
                        // If the stack size is 0, remove the itemstack
                        if(stoveItemStacks[2].stackSize == 0)
                        {
                            stoveItemStacks[2] = null;
                        }
                    }
                }
            }
            // If it is burning and it can process
            if(isBurning() && canProcess())
            {
            	// Increase the cooking time
                stoveCookTime++;
                
                // If it has reached it's end process the item
                // And mark the inventory for change
                if(stoveCookTime == 200)
                {
                    stoveCookTime = 0;
                    processItem();
                    changedActivity = true;
                }
            } else
            {
                stoveCookTime = 0;
            }
            
            // If the previous state of burning is not the same as the current (e.g Was burning but no longer),
            // mark for inventory change
            if(burning != (stoveBurnTime > 0))
            {
                changedActivity = true;
            }
        }
        if(changedActivity)
        {
            onInventoryChanged();
        }
    }

    /**
     * Returns whether the current item can be processed or not
     * 
     * @return Can process?
     */
    private boolean canProcess()
    {
    	// If the process slot or the tool slot is empty, return false
        if(stoveItemStacks[0]== null || stoveItemStacks[1]== null)
        {
            return false;
        }
        
        // Get the result corresponding to the current setup
        ItemStack itemstack = StoveRecipes.getResult(stoveItemStacks[0], stoveItemStacks[1]);
        
        // If there is not result return false
        if(itemstack == null)
        {       
            return false;
        }
        
        // If there is nothing in the result slot, return true
        if(stoveItemStacks[3] == null)
        {
            return true;
        }
        
        // If there is something in the result slot but not an item equal to the result, return false
        if(!stoveItemStacks[3].isItemEqual(itemstack))
        {
            return false;
        }
        
        // If the result slot stack size is less than the maximum 
        // and the result slot stack size is less than the maximal stack size of that item
        // Return true
        if(stoveItemStacks[3].stackSize < getInventoryStackLimit() && stoveItemStacks[3].stackSize < stoveItemStacks[3].getMaxStackSize())
        {
            return true;
        }
        
        // If none of the above returned anything, return false 
        return false;
    }

    /**
     * Processes the item
     */
    public void processItem()
    {
    	// If the item cannot be processed, return
        if(!canProcess())
        {
            return;
        }
        // Check what the result will be
        ItemStack itemstack = StoveRecipes.getResult(stoveItemStacks[0], stoveItemStacks[1]);
        
        // If the result is empty, place a copy of the result inside
        if(stoveItemStacks[3] == null)
        {
            stoveItemStacks[3] = itemstack.copy();
        }
        
        // Else if the itemID in the result is the same as the result
        // Increase the stack size
        else if(stoveItemStacks[3].itemID == itemstack.itemID)
        {
        	stoveItemStacks[3].stackSize++;
        }
        
        // If the processed item has a container item
        // Pop out the container item
        if(stoveItemStacks[1].getItem().hasContainerItem())
        {
        	ItemStack itemStack = new ItemStack(stoveItemStacks[1].getItem().getContainerItem());
        	
        	float randX = worldObj.rand.nextFloat() * 0.8F + 0.1F;
            float randY = worldObj.rand.nextFloat() * 0.8F + 0.1F;
            float randZ = worldObj.rand.nextFloat() * 0.8F + 0.1F;
            
            EntityItem entityitem = new EntityItem(worldObj, (double)((float)xCoord + randX), (double)((float)yCoord + randY), (double)((float)zCoord + randZ), new ItemStack(itemstack.itemID, 1, itemstack.getItemDamage()));
            
            // Insert the itemstack into the entity
            if (itemstack.hasTagCompound())
            {
                entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
            }

            float f = 0.05F;
            entityitem.motionX = (double)((float)worldObj.rand.nextGaussian() * f);
            entityitem.motionY = (double)((float)worldObj.rand.nextGaussian() * f + 0.2F);
            entityitem.motionZ = (double)((float)worldObj.rand.nextGaussian() * f);
            worldObj.spawnEntityInWorld(entityitem);
        }
      	// Decrease the processed item stack
        stoveItemStacks[1].stackSize--;
        
        // Get the tool and if it's damageable, damage it
        if (stoveItemStacks[0].isItemStackDamageable() && 
        		stoveItemStacks[0].attemptDamageItem(1, this.worldObj.rand)) {
    		stoveItemStacks[0] = null;
        }
        
        // If the process stack size is less or equal to 0, remove the itemstack
        if(stoveItemStacks[1].stackSize <= 0)
        {
            stoveItemStacks[1] = null;
        }
    }

    /**
     * Returns the amount of burn time a certain item gives
     * 
     * @param itemstack The fuel
     * @return The amount of burn time
     */
    private int getItemBurnTime(ItemStack itemstack)
    {
    	//  If no fuel is inserted, return 0
        if(itemstack == null)
        {
            return 0;
        }
        int i = itemstack.getItem().itemID;
        // Anything of material wood, return 300
        if(Block.blocksList[i].blockMaterial == Material.wood)
        {
            return 300;
        }
        
        // A stick, return 100
        if(i == Item.stick.itemID)
        {
            return 100;
        }
        
        // A log, return 1600
        if(i == Block.wood.blockID)
        {
            return 1600;
        }
        
        // Coal, return 1600
        if(i == Item.coal.itemID)
        {
            return 1600;
        }
        
        // A bucket of lava, return 20000
        if(i == Item.bucketLava.itemID)
        {
            return 20000;
        }
        
        // A sapling, return 100
        if(i == Block.sapling.blockID)
        {
            return 100;
        }
        
        // A blaze rod, return 2400
        if (i == Item.blazeRod.itemID)
        {
            return 2400;
        }
        
        // If it's none of the above, return 0
        else
        {
        	return 0;
        }
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
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
    	this.tool = EnumStoveToolType.getToolFromInt(packet.data.getInteger("Tool"));
    	worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
    
    /**
     * Overriden in a sign to provide the text.
     */
    @Override
    public Packet getDescriptionPacket()
    {
    	if (this.tool != null) {
	    	Packet132TileEntityData packet = new Packet132TileEntityData();
	    	packet.data = new NBTTagCompound();
	    	packet.data.setInteger("Tool", tool.ordinal());
	        return packet;
    	}
    	return null;
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
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return false;
	}
}
