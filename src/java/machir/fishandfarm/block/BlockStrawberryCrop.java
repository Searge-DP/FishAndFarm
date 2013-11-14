package machir.fishandfarm.block;

import java.util.ArrayList;
import java.util.Random;

import machir.fishandfarm.FishAndFarm;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockStrawberryCrop extends BlockCrop {
	private static final int STRAWBERRY_DAMAGE = 4;
	private static final int SEED_DAMAGE = 2;
	
	public BlockStrawberryCrop(int id) {
		super(id);
		this.maxGrowthStage = 1;
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
            for (int n = 0; n < 2 + fortune; n++) 
            {
            	// A random chance to add more strawberries (A maximum of 2 + fortune)
                if (world.rand.nextInt(15) <= metadata)
                {
                	items.add(new ItemStack(FishAndFarm.food, 1, this.STRAWBERRY_DAMAGE));
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
        return damage == this.maxGrowthStage ? this.STRAWBERRY_DAMAGE : this.SEED_DAMAGE;
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
    
    /**
     * Called upon block activation (right click on the block.)
     * 
     * @param world The world
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate 
     * @param entityPlayer The player entity
     * @param side Block side
     * @param hitX The x coordinate where the pointer hit
     * @param hitY The y coordinate where the pointer hit
     * @param hitZ The z coordinate where the pointer hit
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
    {
    	// Check if the metadata is on it's final stage
    	if (world.getBlockMetadata(x, y, z) == this.maxGrowthStage) {
    		// Drop tomatoes
    		this.dropBlockAsItemWithChance(world, x, y, z, world.getBlockMetadata(x, y, z), 1.0F, 0);
    		// Set the stage back to just before the flowers pop on
    		world.setBlockMetadataWithNotify(x, y, z, this.maxGrowthStage - 1, 2);
    		return true;
    	}
        return false;
    }
}
