package machir.fishandfarm.block;

import java.util.ArrayList;
import java.util.Random;

import machir.fishandfarm.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCrop extends BlockFlower {
	// The growth icons
	@SideOnly(Side.CLIENT)
	protected Icon[] iconArray;
	
	// The minimal light value to grow
	protected static final int MIN_LIGHT_VALUE = 9;
	
	// The maximal growth stage
	protected int maxGrowthStage = 7;
	
	// The crop seed ID
	protected ItemStack seed = new ItemStack(Item.seeds, 1, 0);
	
	// The crop item ID
	protected ItemStack cropItem = new ItemStack(Item.wheat, 1, 0);

	/**
	 * If no seed is given use Item.seeds.itemID
	 * 
	 * @param id The crop ID
	 */
    public BlockCrop(int id)
    {
        super(id);
        this.setTickRandomly(true);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
        this.setCreativeTab((CreativeTabs)null);
        this.setHardness(0.0F);
        this.setStepSound(soundGrassFootstep);
        this.disableStats();
    }

    /**
     * Gets passed in the blockID of the block below and supposed to return true if its allowed to grow on the type of
     * blockID passed in. 
     * 
     * @param soilID Soil blockID
     */
    @Override
    protected boolean canThisPlantGrowOnThisBlockID(int soilID)
    {
        return soilID == Block.tilledField.blockID;
    }

    /**
     * Ticks the block if it's been scheduled
     * 
     * @param world The world
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     * @param random generator
     */
    @Override
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        super.updateTick(world, x, y, z, random);
        
        // If the light value is above the minimum, grow
        if (world.getBlockLightValue(x, y + 1, z) >= this.MIN_LIGHT_VALUE)
        {
            int metadata = world.getBlockMetadata(x, y, z);
            
            // If it's not fully grown, grow
            if (metadata < this.maxGrowthStage)
            {
            	// Get the growth rate
                float growthRate = this.getGrowthRate(world, x, y, z);
                
                // Has a chance of to grow. 
                // The higher the growthRate the higher the chance becomes to grow.
                if (random.nextInt((int)(25.0F / growthRate) + 1) == 0)
                {
                	// Increase the stage (metadata)
                    ++metadata;
                    
                    // Update the block metadata and notify the clients (flag 2)
                    world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
                }
            }
        }
    }

    /**
     * Apply bonemeal to the crops.
     * 
     * @param world The world
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     */
    public void fertilize(World world, int x, int y, int z)
    {
    	// Grab the current metadata and increase it with a random between 2 and 5
        int newMetadata = world.getBlockMetadata(x, y, z) + MathHelper.getRandomIntegerInRange(world.rand, 1, 2);

        // If the new metadata is bigger than the maximum, set it to the maximum
        if (newMetadata > this.maxGrowthStage)
        {
        	newMetadata = this.maxGrowthStage;
        }
        
        // Update the block metadata and notify the clients (flag 2)
        world.setBlockMetadataWithNotify(x, y, z, newMetadata, 2);
    }

    /**
     * Gets the growth rate for the crop. Setup to encourage rows by halving growth rate if there is diagonals, crops on
     * different sides that aren't opposing, and by adding growth for every crop next to this one (and for crop below
     * this one). 
     * 
     * @param World The world
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     */
    private float getGrowthRate(World world, int x, int y, int z)
    {
    	// Set the default growth rate and check around for other crops
        float growthRate = 1.0F;
        int north = world.getBlockId(x, y, z - 1);
        int south = world.getBlockId(x, y, z + 1);
        int west = world.getBlockId(x - 1, y, z);
        int east = world.getBlockId(x + 1, y, z);
        int northWest = world.getBlockId(x - 1, y, z - 1);
        int northEast = world.getBlockId(x + 1, y, z - 1);
        int southWest = world.getBlockId(x - 1, y, z + 1);
        int southEast = world.getBlockId(x + 1, y, z + 1);
        boolean cropNorthSouth = north == this.blockID || south == this.blockID;
        boolean cropEastWest = east == this.blockID || west == this.blockID;
        boolean cropCorners = northWest == this.blockID || northEast == this.blockID || southWest == this.blockID || southEast == this.blockID;

        // Check around for fertile grounds which increases the growth rate
        for (int xPos = x - 1; xPos <= x + 1; ++xPos)
        {
            for (int zPos = z - 1; zPos <= z + 1; ++zPos)
            {
                int blockID = world.getBlockId(xPos, y - 1, zPos);
                float growthBonus = 0.0F;

                if (blocksList[blockID] != null && blocksList[blockID].canSustainPlant(world, xPos, y - 1, zPos, ForgeDirection.UP, this))
                {
                	// Grounds able to sustain a plant set the bonus to 1.0F
                	growthBonus = 1.0F;

                    if (blocksList[blockID].isFertile(world, xPos, y - 1, zPos))
                    {
                    	// Fertile ground sets the bonus to 3.0F
                    	growthBonus = 3.0F;
                    }
                }
                
                // If the ground is not directly beneath the crop
                // Divide it's bonus by 4.0F
                if (xPos != x || zPos != z)
                {
                	growthBonus /= 4.0F;
                }
                
                // Add the bonus to the growth rate
                growthRate += growthBonus;
            }
        }

        // Crops in corner or crops in both north or south and east or west divides the growth rate by 2
        if (cropCorners || cropNorthSouth && cropEastWest)
        {
        	growthRate /= 2.0F;
        }

        // Return the growth rate
        return growthRate;
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. 
     * 
     * @param side The side of the block
     * @param metadata The block metadata
     */
    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIcon(int side, int metadata)
    {
    	// If the stage is more than the maximum, set it to the maximum
    	// If the stage is less than the minimum, set it to the maximum
        if (metadata < 0 || metadata > this.maxGrowthStage)
        {
        	metadata = this.maxGrowthStage;
        }

        // Return the corresponding icon
        return this.iconArray[metadata];
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
    public int getRenderType()
    {
    	// A square form
        return 6;
    }

    /**
     * Drops the block items with a specified chance of dropping the specified items
     * 
     * @param world The world
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     * @param metadata The block metadata
     * @param chance The chance of dropping
     * @param fortune The fortune level
     */
    @Override
    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int metadata, float chance, int fortune)
    {
        super.dropBlockAsItemWithChance(world, x, y, z, metadata, chance, 0);
    }

    /**
     * This returns a complete list of items dropped from this block
     * 
     * @param The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param metadata Current metadata
     * @param fortune Breakers fortune level
     */
    @Override 
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
    {
    	// A list of dropped items
        ArrayList<ItemStack> items = super.getBlockDropped(world, x, y, z, metadata, fortune);

        // If the crop is on it's last stage drop seeds
        if (metadata >= this.maxGrowthStage)
        {
            for (int n = 0; n < 3 + fortune; n++)
            {	
            	// A random chance to add seeds (A maximum of 3 + fortune)
                if (world.rand.nextInt(15) <= metadata)
                {
                	items.add(this.seed);
                }
            }
        }

        return items;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     * 
     * @param metadata The block metadata
     * @param random generator
     * @param randomNumber Unknown, it's used in gravel in the random chance of getting flint
     */
    @Override
    public int idDropped(int metadata, Random random, int randomNumber)
    {
    	// If the crop is on it's last stage, drop the item
    	// If not, drop a seed
        return metadata == this.maxGrowthStage ? this.cropItem.itemID : this.seed.itemID;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     * 
     * @param random generator
     */
    @Override
    public int quantityDropped(Random random)
    {
        return 1;
    }

    /**
     * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
     * 
     * @param world The world
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     */
    @SideOnly(Side.CLIENT)
    @Override
    public int idPicked(World world, int x, int y, int z)
    {
        return this.seed.itemID;
    }
    
    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    @Override
    public int damageDropped(int damage)
    {
    	// If the crop is on it's last stage, drop the item
    	// If not, drop a seed
        return damage == this.maxGrowthStage ? this.cropItem.getItemDamage() : this.seed.getItemDamage();
    }
    
	@Override
	public EnumPlantType getPlantType(World world, int x, int y, int z) {
		return EnumPlantType.Crop;
	}

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     * 
	 * @param register An object implementing IconRegister
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        this.iconArray = new Icon[this.maxGrowthStage + 1];

        for (int i = 0; i < this.iconArray.length; ++i)
        {
            this.iconArray[i] = iconRegister.registerIcon(ModInfo.MODID + ":" + this.getTextureName() + "_stage_" + i);
        }
    }
}
