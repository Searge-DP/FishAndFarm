package machir.fishandfarm.block;

import machir.fishandfarm.tileentity.TileEntityEmpty;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEmpty extends BlockContainer {
	public BlockEmpty(int id) {
		super(id, Material.rock);
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
    	TileEntityEmpty tileEntityEmpty = (TileEntityEmpty)world.getBlockTileEntity(x, y, z);
    	
    	if (tileEntityEmpty != null) {
    		world.setBlockToAir(tileEntityEmpty.coreX, tileEntityEmpty.coreY, tileEntityEmpty.coreZ);
    		
    		int coreBlockID = world.getBlockId(tileEntityEmpty.coreX, tileEntityEmpty.coreY, tileEntityEmpty.coreZ);
    		int coreBlockMetadata = world.getBlockMetadata(tileEntityEmpty.coreX, tileEntityEmpty.coreY, tileEntityEmpty.coreZ);
    		if (coreBlockID != 0) {
    			Block.blocksList[coreBlockID].dropBlockAsItem(world, tileEntityEmpty.coreX, tileEntityEmpty.coreY, tileEntityEmpty.coreZ, coreBlockMetadata, 0);
    		}
    		
    		world.removeBlockTileEntity(tileEntityEmpty.coreX, tileEntityEmpty.coreY, tileEntityEmpty.coreZ);
    	}
    	
    	world.removeBlockTileEntity(x, y, z);
    }
    
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) 
     * 
     * @param world The world
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     * @param blockID Neighbor blockID
     */
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
    	TileEntityEmpty tileEntity = (TileEntityEmpty)world.getBlockTileEntity(x, y, z);
    	if (tileEntity != null) {
    		if (world.getBlockId(tileEntity.coreX, tileEntity.coreY, tileEntity.coreZ) < 1) {
    			world.setBlockToAir(x, y, z);
    			world.removeBlockTileEntity(x, y, z);
    		}
    	}
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
    	if (!world.isRemote) { 
	    	TileEntityEmpty tileEntity = (TileEntityEmpty)world.getBlockTileEntity(x, y, z);
	    	if (tileEntity != null && !entityPlayer.isSneaking()) {
	    		int blockID = world.getBlockId(tileEntity.coreX, tileEntity.coreY, tileEntity.coreZ);
	    		Block block = Block.blocksList[blockID];
	    		if (block != null) {
	    			return block.onBlockActivated(world, tileEntity.coreX, tileEntity.coreY, tileEntity.coreZ, entityPlayer, side, hitX, hitY, hitZ);
	    		}
	    	}
    	}
    	return true;
    }
    
    /**
     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
     * coordinates.  
     */
    @Override
    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int x, int y, int z, int side){
            return false;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
    public boolean isOpaqueCube(){
            return false;
    }
    
    /**
     * Called when a user uses the creative pick block button on this block
     *
     * @param target The full target the player is looking at
     * @return A ItemStack to add to the player's inventory, Null if nothing should be added.
     */
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
    	TileEntityEmpty tileEntityEmpty = (TileEntityEmpty)world.getBlockTileEntity(x, y, z);
    	if (tileEntityEmpty != null) {
    		int coreID = world.getBlockId(tileEntityEmpty.coreX, tileEntityEmpty.coreY, tileEntityEmpty.coreZ);
    		int coreMetadata = world.getBlockMetadata(tileEntityEmpty.coreX, tileEntityEmpty.coreY, tileEntityEmpty.coreZ);
    		return new ItemStack(coreID, 1, coreMetadata);
    	}
    	return new ItemStack(Block.stone, 1, 0);
    }
    
    /**
     * return a new tile entity stove, used to add a tile entity to the block
     * 
     * @param world The world
     */
    @Override
	public TileEntity createNewTileEntity(World world) {
            return new TileEntityEmpty();
    }
}
