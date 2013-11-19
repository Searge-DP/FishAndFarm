package machir.fishandfarm.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BlockContainer extends BlockFishAndFarm implements ITileEntityProvider
{
	
	protected BlockContainer(int id, Material material)
    {
        super(id, material);
        this.isBlockContainer = true;
    }
	
	protected BlockContainer(int id)
    {
        super(id);
        this.isBlockContainer = true;
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        super.onBlockAdded(world, x, y, z);
    }

    /**
     * Called on server worlds only when the block has been replaced by a different block ID, or the same block with a
     * different metadata value, but before the new metadata value is set. 
     * 
     * @param world The world
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     * @param blockID The old block ID
     * @param metadata The old block metadata
     */
    @Override
    public void breakBlock(World world, int x, int y, int z, int blockID, int metadata)
    {
        super.breakBlock(world, x, y, z, blockID, metadata);
        world.removeBlockTileEntity(x, y, z);
    }

    /**
     * Called when the block receives a BlockEvent - see World.addBlockEvent. By default, passes it on to the tile
     * entity at this location. 
     * 
     * @param world The world
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     */
    @Override
    public boolean onBlockEventReceived(World world, int x, int y, int z, int eventID, int event)
    {
        super.onBlockEventReceived(world, x, y, z, eventID, event);
        TileEntity tileentity = world.getBlockTileEntity(x, y, z);
        return tileentity != null ? tileentity.receiveClientEvent(eventID, event) : false;
    }
}
