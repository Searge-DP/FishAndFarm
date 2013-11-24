package machir.fishandfarm.block;

import java.util.Random;

import machir.fishandfarm.FishAndFarm;
import machir.fishandfarm.tileentity.TileEntityCage;
import machir.fishandfarm.tileentity.TileEntityStove;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCage extends BlockContainer {
    /**
     * Is the random generator used by the cage to drop the inventory contents in random directions.
     */
    private Random cageRand;
    
    public BlockCage(int id) {
        super(id, Material.iron);
        cageRand = new Random();
        this.setHardness(0.75F);
        setCreativeTab(CreativeTabs.tabDecorations);
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
        TileEntityCage tileentitycage = (TileEntityCage)world.getBlockTileEntity(x, y, z);
        
        // If there is a tile entity cage on x, y, z and if the world is not remote, open the gui
        if(tileentitycage != null)
        {
            if(!world.isRemote)
            {
                entityPlayer.openGui(FishAndFarm.instance, FishAndFarm.GUI_CAGE_ID, world, x, y, z);
            }
        }
        return true;
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
        TileEntityCage tileentity = (TileEntityCage)world.getBlockTileEntity(x, y, z);

        if (tileentity != null)
        {
            // If so, empty the slots and drop them randomly around the removed stove
            for (int slot = 0; slot < tileentity.getSizeInventory(); ++slot)
            {
                ItemStack itemstack = tileentity.getStackInSlot(slot);

                if (itemstack != null)
                {
                    float randX = cageRand.nextFloat() * 0.8F + 0.1F;
                    float randY = cageRand.nextFloat() * 0.8F + 0.1F;
                    float randZ = cageRand.nextFloat() * 0.8F + 0.1F;

                    // As long as we still haven't emptied the slot 
                    // Keep on throwing itemStacks out
                    while (itemstack.stackSize > 0)
                    {
                        int stackSize = cageRand.nextInt(21) + 10;

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
                        entityitem.motionX = (double)((float)cageRand.nextGaussian() * f);
                        entityitem.motionY = (double)((float)cageRand.nextGaussian() * f + 0.2F);
                        entityitem.motionZ = (double)((float)cageRand.nextGaussian() * f);
                        world.spawnEntityInWorld(entityitem);
                    }
                }
            }
            if (tileentity.bobber != null) {    
                tileentity.bobber.setDead();
            }
            world.func_96440_m(x, y, z, blockID);
        }

        super.breakBlock(world, x, y, z, blockID, metadata);
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

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileEntityCage();
    }
}
