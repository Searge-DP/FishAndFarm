package machir.fishandfarm.tileentity;

import java.util.Random;

import machir.fishandfarm.FishAndFarm;
import machir.fishandfarm.ModInfo;
import machir.fishandfarm.entity.EntityCageBobber;
import machir.fishandfarm.inventory.slot.SlotBait;
import machir.fishandfarm.packet.TileEntityPacket;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class TileEntityCage extends TileEntity implements ISidedInventory {
    // The cage duration
    private static final int MIN_CAGE_DURATION = 800;
    private static final int MAX_CAGE_DURATION = 1200;
    
    // The itemStacks inside
    private ItemStack cageItemStacks[];
   
    // The process times
    public int cageProcessTime;
    
    // Random used to calculate whether the bait has been used
    private Random random = new Random();
    
    // Cage bobber
    public EntityCageBobber bobber = null;
    
    public TileEntityCage() {
        // 2 Slots, 1 bait, 1 food
        cageItemStacks = new ItemStack[2];
        
        cageProcessTime = 0;
    }
    
    /**
     * Returns the number of slots in the inventory.
     */
    @Override
    public int getSizeInventory()
    {
        return cageItemStacks.length;
    }

    /**
     * Returns the stack in slot
     */
    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return cageItemStacks[slot];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    @Override
    public ItemStack decrStackSize(int slot, int amount)
    {
        if(cageItemStacks[slot] != null)
        {
            if(cageItemStacks[slot].stackSize <= amount)
            {
                ItemStack itemstack = cageItemStacks[slot];
                cageItemStacks[slot] = null;
                return itemstack;
            }
            ItemStack itemstack1 = cageItemStacks[slot].splitStack(amount);
            if(cageItemStacks[slot].stackSize == 0)
            {
                cageItemStacks[slot] = null;
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
        if (cageItemStacks[slot] != null)
        {
            ItemStack itemstack = cageItemStacks[slot];
            cageItemStacks[slot] = null;
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
        cageItemStacks[slot] = itemStack;
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
        return LanguageRegistry.instance().getStringLocalization(ModInfo.MODID + ".tileentity." + ModInfo.UNLOC_NAME_TILEENTITY_CAGE + ".inv_name");
    }

    /**
     * Reads a tile entity from NBT.
     */
    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        cageItemStacks = new ItemStack[getSizeInventory()];
        for(int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            byte slot = nbttagcompound1.getByte("Slot");
            if(slot >= 0 && slot < cageItemStacks.length)
            {
                cageItemStacks[slot] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
                
        cageProcessTime = nbttagcompound.getShort("ProcessTime");
    }

    /**
     * Writes a tile entity to NBT.
     */
    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("ProcessTime", (short)cageProcessTime);
        NBTTagList nbttaglist = new NBTTagList();
        for(int slot = 0; slot < cageItemStacks.length; slot++)
        {
            if(cageItemStacks[slot] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)slot);
                cageItemStacks[slot].writeToNBT(nbttagcompound1);
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
     * Returns true if the cage uses bait
     */
    public boolean usingBait()
    {
        return cageProcessTime > 0;
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    @Override
    public void updateEntity()
    {
        boolean usingBait = cageProcessTime > 0;
        boolean changedActivity = false;
        
        if(!worldObj.isRemote)
        {
            if (isInWater()) {
                // Check if the bobber is already existing, if not spawn it
                if (bobber == null) {
                    bobber = new EntityCageBobber(worldObj, this);
                    worldObj.spawnEntityInWorld(bobber);
                }
                
                // If it has bait and it can process
                if(hasBait()) {
                    // If it's using bait, increase the time used
                    if(cageProcessTime < MAX_CAGE_DURATION)
                    {
                        cageProcessTime++;
                    } 
                        
                    // If it has reached it's end process the item
                    // And mark the inventory for change
                    if(cageProcessTime > MIN_CAGE_DURATION) {
                        if (worldObj.rand.nextInt(MAX_CAGE_DURATION - cageProcessTime) == 0) {
                            processItem();
                            cageProcessTime = 0;
                            cageItemStacks[0].stackSize--;
                            if (cageItemStacks[0].stackSize < 1) {
                                cageItemStacks[0] = null;
                            }
                        }
                        changedActivity = true;
                    }
                } else {
                    cageProcessTime = 0;
                }
                
                // If the previous state of usingBait is not the same as the current (e.g Was using bait but no longer),
                // mark for inventory change
                if(usingBait != (cageProcessTime > 0))
                {
                    changedActivity = true;
                }
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
     * @return Does the cage have bait inside?
     */
    private boolean hasBait()
    {
        // If the bait slot is not empty, return true
        return cageItemStacks[0] != null;
    }
    
    /**
     * Returns whether the cage is under water 
     * and has water around it.
     */
    private boolean isInWater() {
        // Check if water is surrounding the cage
        boolean north = worldObj.getBlockId(xCoord, yCoord, zCoord - 1) == Block.waterStill.blockID;
        boolean south = worldObj.getBlockId(xCoord, yCoord, zCoord + 1) == Block.waterStill.blockID;
        boolean west = worldObj.getBlockId(xCoord - 1, yCoord, zCoord) == Block.waterStill.blockID;
        boolean east = worldObj.getBlockId(xCoord + 1, yCoord, zCoord) == Block.waterStill.blockID;
        boolean northWest = worldObj.getBlockId(xCoord - 1, yCoord, zCoord - 1) == Block.waterStill.blockID;
        boolean northEast = worldObj.getBlockId(xCoord + 1, yCoord, zCoord - 1) == Block.waterStill.blockID;
        boolean southWest = worldObj.getBlockId(xCoord - 1, yCoord, zCoord - 1) == Block.waterStill.blockID;
        boolean southEast = worldObj.getBlockId(xCoord + 1, yCoord, zCoord - 1) == Block.waterStill.blockID;
        boolean up = worldObj.getBlockId(xCoord, yCoord + 1, zCoord) == Block.waterStill.blockID;
        
        return north && south && west && east 
            && northWest && northEast && southWest && southEast 
            && up;
    }
    
    public boolean hasCatch() {
        return cageItemStacks[1] != null;
    }

    /**
     * Processes the item
     */
    public void processItem()
    {
        // If the item cannot be processed, return
        if(!hasBait())
        {
            return;
        }
        // Get the result corresponding to the current input
        ItemStack result = null;
        
        if (worldObj.rand.nextInt(5) > 3) {
            result = new ItemStack(FishAndFarm.fish, 1, 7);
        } else {
            result = new ItemStack(FishAndFarm.fish, 1, 9);
        }
        
        // Set the output
        if (cageItemStacks[1] == null) {
            cageItemStacks[1] = result;
        } else if (cageItemStacks[1].itemID == result.itemID && cageItemStacks[1].stackSize < cageItemStacks[1].getMaxStackSize()) {
            cageItemStacks[1].stackSize++;
        }
    }
    
    /**
     * Return true if item is a bait.
     */
    public static boolean isItemBait(ItemStack itemStack)
    {
        return SlotBait.isBaitAllowed(itemStack);
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
        // If it's the bait slot, check if it's useable
        return slot == 0 ? isItemBait(itemStack) : false;
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
        // The bottom has the process slots accessible, the top and sides will be used for bait
        return side == 0 ? new int[] {1} : new int[] {0};
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
        return this.isItemValidForSlot(slot, itemStack);
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
        // If the requested slot is the catch slot
        // Return true
        if (side == 0 && slot == 1) {
            return true;
        } else {
            return false;
        } 
    }
}
