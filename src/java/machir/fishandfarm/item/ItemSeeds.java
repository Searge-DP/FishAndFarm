package machir.fishandfarm.item;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;

public class ItemSeeds extends ItemFishAndFarm implements IPlantable {
    //The type of block this seed turns into (wheat or pumpkin stems for instance)
    private int cropBlockID;

    // BlockID of the block the seeds can be planted on.
    private int soilBlockID;

    public ItemSeeds(int id, int cropBlockID, int soilBlockID)
    {
        super(id);
        this.cropBlockID = cropBlockID;
        this.soilBlockID = soilBlockID;
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     * 
     * @param itemStack The used itemstack
     * @param entityPlayer The player entity using the itemstack
     * @param world The world
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     * @param side The side of the block on which it is used
     * @param hitX The x coordinate where the pointer hit
     * @param hitY The y coordinate where the pointer hit
     * @param hitZ The z coordinate where the pointer hit
     */
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
    	// If it's not on the top, return false
        if (side != 1)
        {
            return false;
        }
        // If it is on the top, check if the player may edit the soil and top
        else if (entityPlayer.canPlayerEdit(x, y, z, side, itemStack) && entityPlayer.canPlayerEdit(x, y + 1, z, side, itemStack))
        {
        	// If so grab the soil id
            int soilID = world.getBlockId(x, y, z);
            Block soil = Block.blocksList[soilID];
            
            // Check if the soil can have a crop on top and if above it is an air block
            if (soil != null && soil.blockID == this.soilBlockID && soil.canSustainPlant(world, x, y, z, ForgeDirection.UP, this) && world.isAirBlock(x, y + 1, z))
            {
            	// Place the crop on top and decrease the stack size
                world.setBlock(x, y + 1, z, this.cropBlockID);
                --itemStack.stackSize;
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
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
    public EnumPlantType getPlantType(World world, int x, int y, int z)
    {
        return EnumPlantType.Crop;
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
    public int getPlantID(World world, int x, int y, int z)
    {
        return cropBlockID;
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
    public int getPlantMetadata(World world, int x, int y, int z)
    {
        return 0;
    }
}
