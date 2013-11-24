package machir.fishandfarm.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import machir.fishandfarm.FishAndFarm;
import machir.fishandfarm.ModInfo;

public class ItemCropSeeds extends ItemSeeds {
	// The seed icons
	private Icon[] icons;
	
	// The seed names
	private String[] seedNames = { "lettuceSeeds", "tomatoSeeds", "strawberrySeeds" };
	
	// The type of block this seed turns into (wheat or pumpkin stems for instance)
    private int[] cropBlockID = { FishAndFarm.lettuceCrop.blockID, FishAndFarm.tomatoCrop.blockID, FishAndFarm.strawberryCrop.blockID };

    // BlockID of the block the seeds can be planted on.
    private int[] soilBlockID = { Block.tilledField.blockID, Block.tilledField.blockID, Block.tilledField.blockID };

	public ItemCropSeeds(int id) {
		super(id, 0, 0);
		setMaxDamage(0);
		setHasSubtypes(true);
		setCreativeTab(CreativeTabs.tabMaterials);
	}
	
	/**
	 * Registers the sub-items
	 * 
	 * @param id Item ID
	 * @param tabs The CreativeTabs
	 * @param list The list of sub-items
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int id, CreativeTabs tabs, List list) {
		for (int i = 0; i < cropBlockID.length; i++) {
			list.add(new ItemStack(id, 1, i));
		}
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
	@Override
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
            if (soil != null && soil.blockID == this.soilBlockID[itemStack.getItemDamage()] && soil.canSustainPlant(world, x, y, z, ForgeDirection.UP, this) && world.isAirBlock(x, y + 1, z))
            {
            	// Place the crop on top and decrease the stack size
                world.setBlock(x, y + 1, z, this.cropBlockID[itemStack.getItemDamage()]);
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
     * When this method is called, your item should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
	 * 
	 * @param register An object implementing IconRegister
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register) {
		icons = new Icon[seedNames.length];
		int i = 0;
		for (String seedName : seedNames) {
			icons[i] = register.registerIcon(ModInfo.MODID + ":" + seedName);
			i++;
		}
	}
	
	/**
	 * Returns the icon depending on damage value
	 * 
	 * @param damage The damage value
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int damage) {
		return damage < icons.length ? icons[damage] : null;
	}
	
	/**
	 * Returns the unlocalized name of the itemstack
	 * 
	 * @param itemstack The corresponding itemstack
	 */
	@Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
		return ModInfo.MODID + "." + ModInfo.UNLOC_NAME_ITEM_SEEDS + "." + itemStack.getItemDamage();
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
        return 0;
    }
}
