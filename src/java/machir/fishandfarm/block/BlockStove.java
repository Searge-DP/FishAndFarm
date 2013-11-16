package machir.fishandfarm.block;

import java.util.Random;

import machir.fishandfarm.FishAndFarm;
import machir.fishandfarm.ModInfo;
import machir.fishandfarm.item.crafting.EnumStoveToolType.StoveToolType;
import machir.fishandfarm.tileentity.TileEntityStove;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockStove extends BlockContainer {
    /**
     * Is the random generator used by stove to drop the inventory contents in random directions.
     */
	private Random stoveRand;
    
    public BlockStove(int id)
    {
        super(id, Material.iron);
        this.setHardness(0.75F);
        stoveRand = new Random();
        setBlockBounds(0.062f, 0.0f, 0.062f, 0.938f, 0.625f, 0.938f);
        setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
    	return false;
    }
    
    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
    	return false;
    }
    
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return -1;
    }
    
    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
    	float height = 0.625f;
    	
    	// Currently not implemented
//    	TileEntityStove stove = (TileEntityStove) world.getBlockTileEntity(x, y, z);
//    	if(stove != null)
//    	{
//    		if(stove.tool == "cookingPan")
//    			height = 0.925f;
//    		if(stove.tool == "fryingPan")
//    			height = 0.725f;
//    	}
        return AxisAlignedBB.getBoundingBox(x , y, z, x + 1.0f, y + height, z + 1.0f);
    }

    /**
     * Called whenever the block is added into the world
     * 
     * @param world The world
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     */
    public void onBlockAdded(World world, int x, int y, int z)
    {
        super.onBlockAdded(world, x, y, z);
        setDefaultDirection(world, x, y, z);
    }

    /**
     * Set the way of facing
     * 
     * @param world The world
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     */
    private void setDefaultDirection(World world, int x, int y, int z)
    {
        if(world.isRemote)
        {
            return;
        }
        int north = world.getBlockId(x, y, z - 1);
        int south = world.getBlockId(x, y, z + 1);
        int west = world.getBlockId(x - 1, y, z);
        int east = world.getBlockId(x + 1, y, z);
        byte direction = 3;
        if(Block.opaqueCubeLookup[north] && !Block.opaqueCubeLookup[south])
        {
        	direction = 3;
        }
        if(Block.opaqueCubeLookup[south] && !Block.opaqueCubeLookup[north])
        {
        	direction = 2;
        }
        if(Block.opaqueCubeLookup[west] && !Block.opaqueCubeLookup[east])
        {
        	direction = 5;
        }
        if(Block.opaqueCubeLookup[east] && !Block.opaqueCubeLookup[west])
        {
        	direction = 4;
        }
        // Update the block metadata and notify all clients
        world.setBlockMetadataWithNotify(x, y, z, direction, 2);
    }

    /**
     * Called upon block activation (right click on the block.)
     * 
     * @param world The world
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     * @param entityPlayer The player who activated the block
     * @param side The side which was activated
     * @param hitX The x coordinate where the pointer hit
     * @param hitY The y coordinate where the pointer hit
     * @param hitZ The z coordinate where the pointer hit
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
    {
    	TileEntityStove tileentitystove = (TileEntityStove)world.getBlockTileEntity(x, y, z);
    	
    	// If there is a tile entity stove on x, y, z and if the world is not remote, open the gui
        if(tileentitystove != null)
        {
            if(!world.isRemote)
            {
            	entityPlayer.openGui(FishAndFarm.instance, 0, world, x, y, z);
            }
        }
        return true;
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
    	TileEntityStove tileEntityStove = (TileEntityStove)world.getBlockTileEntity(x, y, z);
    	
    	if (tileEntityStove != null)
	        if (tileEntityStove.tool != null && tileEntityStove.stoveBurnTime > 0)
	        {
	            int metadata = world.getBlockMetadata(x, y, z);
	            float particleX = (float)x + 0.5F;
	            float particleY = (float)y + 0.6F + random.nextFloat() / 6.0F;
	            float particleZ = (float)z + 0.5F;
	            float particleOffsetCenter = (tileEntityStove.tool == StoveToolType.FRYINGPAN ? 0.1F + (random.nextFloat() * 2.5F / 20.0F) : -0.1F + (random.nextFloat() * 2.5F / 20.0F));
	            float particleOffsetFront = random.nextFloat() * 3.5F / 10.0F;
	
	            if (metadata == 4)
	            {
	            	world.spawnParticle("smoke", (double)(particleX - particleOffsetCenter), (double)particleY + 0.3F, (double)(particleZ + particleOffsetFront), 0.0D, 0.0D, 0.0D);
	            	world.spawnParticle("flame", (double)(particleX - particleOffsetCenter), (double)particleY, (double)(particleZ + particleOffsetFront), 0.0D, 0.0D, 0.0D);
	            }
	            else if (metadata == 5)
	            {
	            	world.spawnParticle("smoke", (double)(particleX + particleOffsetCenter), (double)particleY + 0.3F, (double)(particleZ - particleOffsetFront), 0.0D, 0.0D, 0.0D);
	            	world.spawnParticle("flame", (double)(particleX + particleOffsetCenter), (double)particleY, (double)(particleZ - particleOffsetFront), 0.0D, 0.0D, 0.0D);
	            }
	            else if (metadata == 2)
	            {
	            	world.spawnParticle("smoke", (double)(particleX - particleOffsetFront), (double)particleY + 0.3F, (double)(particleZ - particleOffsetCenter), 0.0D, 0.0D, 0.0D);
	                world.spawnParticle("flame", (double)(particleX - particleOffsetFront), (double)particleY, (double)(particleZ - particleOffsetCenter), 0.0D, 0.0D, 0.0D);
	            }
	            else if (metadata == 3)
	            {
	            	world.spawnParticle("smoke", (double)(particleX + particleOffsetFront), (double)particleY + 0.3F, (double)(particleZ + particleOffsetCenter), 0.0D, 0.0D, 0.0D);
	            	world.spawnParticle("flame", (double)(particleX + particleOffsetFront), (double)particleY, (double)(particleZ + particleOffsetCenter), 0.0D, 0.0D, 0.0D);
	            }
	        }
    }

    /**
     * Called when the block is placed in the world.
     * 
     * @param world The world
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     * @param entityLivingBase The entity which placed the block
     * @param itemStack The itemStack which was placed
     */
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack)
    {
    	// Get the direction the entity is facing
    	int direction = MathHelper.floor_double((double)((entityLivingBase.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        if(direction == 0)
        {
            world.setBlockMetadataWithNotify(x, y, z, 2, 2);
        }
        if(direction == 1)
        {
            world.setBlockMetadataWithNotify(x, y, z, 5, 2);
        }
        // North?
        if(direction == 2)
        {
            world.setBlockMetadataWithNotify(x, y, z, 3, 2);
        }
        if(direction == 3)
        {
            world.setBlockMetadataWithNotify(x, y, z, 4, 2);
        }
    }
    
    /**
     * Called right before the block is destroyed by a player.  
     * 
     * @param world The world
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     * @param blockID The old bockID
     * @param metadata The block metadata
     */
    @Override
    public void breakBlock(World world, int x, int y, int z, int blockID, int metadata)
    {
    	// Check if the tile entity exists
        TileEntityStove tileentity = (TileEntityStove)world.getBlockTileEntity(x, y, z);

        if (tileentity != null)
        {
        	// If so, empty the slots and drop them randomly around the removed stove
            for (int slot = 0; slot < tileentity.getSizeInventory(); ++slot)
            {
                ItemStack itemstack = tileentity.getStackInSlot(slot);

                if (itemstack != null)
                {
                    float randX = stoveRand.nextFloat() * 0.8F + 0.1F;
                    float randY = stoveRand.nextFloat() * 0.8F + 0.1F;
                    float randZ = stoveRand.nextFloat() * 0.8F + 0.1F;

                    // As long as we still haven't emptied the slot 
                    // Keep on throwing itemStacks out
                    while (itemstack.stackSize > 0)
                    {
                        int stackSize = stoveRand.nextInt(21) + 10;

                        // If the stackSize to drop is bigger than the itemstack's stackSize,
                        // Set the stackSize to drop to the itemstack's stackSize
                        if (stackSize > itemstack.stackSize)
                        {
                        	stackSize = itemstack.stackSize;
                        }

                        itemstack.stackSize -= stackSize;
                        EntityItem entityitem = new EntityItem(world, (double)((float)x + randX), (double)((float)y + randY), (double)((float)z + randZ), new ItemStack(itemstack.itemID, stackSize, itemstack.getItemDamage()));

                        // Insert the itemStack into the entity
                        if (itemstack.hasTagCompound())
                        {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                        }

                        float f = 0.05F;
                        entityitem.motionX = (double)((float)stoveRand.nextGaussian() * f);
                        entityitem.motionY = (double)((float)stoveRand.nextGaussian() * f + 0.2F);
                        entityitem.motionZ = (double)((float)stoveRand.nextGaussian() * f);
                        world.spawnEntityInWorld(entityitem);
                    }
                }
            }
            world.func_96440_m(x, y, z, blockID);
        }

        super.breakBlock(world, x, y, z, blockID, metadata);
    }
    
    /**
     * If this returns true, then comparators facing away from this block will use the value from
     * getComparatorInputOverride instead of the actual redstone signal strength.
     */
    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    /**
     * If hasComparatorInputOverride returns true, the return value from this is used instead of the redstone signal
     * strength when this block inputs to a comparator.
     * 
     * @param world The world
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     * @param 
     */
    public int getComparatorInputOverride(World world, int x, int y, int z, int par5)
    {
    	// Get the stove tile entity
    	IInventory inventory = (IInventory)world.getBlockTileEntity(x, y, z);
    	
    	// If it's null, return 0
    	if (inventory == null)
        {
            return 0;
        }
        else
        {
        	// Keeps track of the amount of filled slots (i.e. not empty slots)
            int filledSlots = 0;
            float outputStrength = 0.0F;

            // Go through every slot (except the tool slot) and update the outputStrength
            for (int j = 1; j < inventory.getSizeInventory(); ++j)
            {
                ItemStack itemstack = inventory.getStackInSlot(j);

                // If the slot is not empty, update the outputStrength using how much the slot if filled
                // Then increase the filledSlots (i.e. not empty slots)
                if (itemstack != null)
                {
                	outputStrength += (float)itemstack.stackSize / (float)Math.min(inventory.getInventoryStackLimit(), itemstack.getMaxStackSize());
                    ++filledSlots;
                }
            }
            
            // Divide the outputStrength by the size of the inventory - tool slot
            outputStrength /= (float)inventory.getSizeInventory() - 1;
            
            // Calculate the final result and return it
            return MathHelper.floor_float(outputStrength * 14.0F) + (filledSlots > 0 ? 1 : 0);
        }
    }
	
	/**
	 * Returns the unlocalized name
	 * 
	 * @param itemstack The corresponding itemstack
	 */
	@Override
	public String getUnlocalizedName() {		
		return ModInfo.MODID + "." + ModInfo.UNLOC_NAME_BLOCK_STOVE;
	}
    
    /**
     * return a new tile entity stove, used to add a tile entity to the block
     * 
     * @param world The world
     */
    @Override
	public TileEntity createNewTileEntity(World world) {
    	return new TileEntityStove();
	}
}
