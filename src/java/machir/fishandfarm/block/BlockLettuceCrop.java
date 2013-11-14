package machir.fishandfarm.block;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import machir.fishandfarm.FishAndFarm;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockLettuceCrop extends BlockCrop {
	private static final int LETTUCE_DAMAGE = 2;
	private static final int SEED_DAMAGE = 0;
	
	public BlockLettuceCrop(int id) {
		super(id);
		this.maxGrowthStage = 3;
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
                	items.add(new ItemStack(FishAndFarm.seeds, 1, this.SEED_DAMAGE));
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
        return metadata == this.maxGrowthStage ? FishAndFarm.food.itemID : FishAndFarm.seeds.itemID;
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
        return FishAndFarm.seeds.itemID;
    }
    
    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    @Override
    public int damageDropped(int damage)
    {
    	// If the crop is on it's last stage, drop the item
    	// If not, drop a seed
        return damage == this.maxGrowthStage ? this.LETTUCE_DAMAGE : this.SEED_DAMAGE;
    }
    
    /**
     * The type of render function that is called for this block
     */
    @Override
    public int getRenderType()
    {
    	// A cross form
        return 1;
    }
}
