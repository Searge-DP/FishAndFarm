package machir.fishandfarm.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;

public class BlockFlower extends BlockFishAndFarm implements IPlantable {

	public BlockFlower(int id) {
		super(id, Material.plants);
        this.setTickRandomly(true);
        this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.6F, 0.7F);
        this.setCreativeTab(CreativeTabs.tabDecorations);
	}
	
    /**
     * Checks to see if its valid to put this block at the specified coordinates
     * 
	 * @param world The world
	 * @param x coordinate
	 * @param y coordinate
	 * @param z coordinate
     */
	@Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        return super.canPlaceBlockAt(world, x, y, z) && canBlockStay(world, x, y, z);
    }

    /**
     * Gets passed in the blockID of the block below and supposed to return true 
     * if its allowed to grow on the type of blockID passed in
     * 
     * @param soilID soil blockID
     */
    protected boolean canThisPlantGrowOnThisBlockID(int soilID)
    {
        return soilID == Block.grass.blockID || soilID == Block.dirt.blockID || soilID == Block.tilledField.blockID;
    }

    /**
     * Lets the block know when one of its neighbor changes. 
     * Doesn't know which neighbor changed (coordinates passed are their own) 
     * 
     * @param world The World
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     * @param blockID neighbor blockID
     */
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
    {
        super.onNeighborBlockChange(world, x, y, z, blockID);
        this.checkFlowerChange(world, x, y, z);
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
        this.checkFlowerChange(world, x, y, z);
    }

    /**
     * Checks if the flower can stay, if not it'll be dropped as an item 
     * @param world The world
     * 
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     */
    protected final void checkFlowerChange(World world, int x, int y, int z)
    {
        if (!this.canBlockStay(world, x, y, z))
        {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlock(x, y, z, 0, 0, 2);
        }
    }
	
    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants
     * 
     * @param world The world
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     */
    @Override
    public boolean canBlockStay(World world, int x, int y, int z)
    {
        Block soil = blocksList[world.getBlockId(x, y - 1, z)];
        return (world.getFullBlockLightValue(x, y, z) >= 8 || world.canBlockSeeTheSky(x, y, z)) && 
                (soil != null && soil.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this));
    }
	
    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     * 
     * @param world The world
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     */
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return null;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block
     */
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
    public int getRenderType()
    {
        return 1;
    }

    /**
     * Requests the type of plant
     * 
     * @param world The world
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     * @return The corresponding plant type
     */
	@Override
	public EnumPlantType getPlantType(World world, int x, int y, int z) {
		return EnumPlantType.Plains;
	}

	/**
	 * Request the plant ID
	 * 
	 * @param world The world
	 * @param x coordinate
	 * @param y coordinate
	 * @param z coordinate
	 */
	@Override
	public int getPlantID(World world, int x, int y, int z) {
		return blockID;
	}

	/**
	 * Requests the plant metadata
	 * 
	 * @param world The world
	 * @param x coordinate
	 * @param y coordinate
	 * @param z coordinate
	 */
	@Override
	public int getPlantMetadata(World world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z);
	}

}
