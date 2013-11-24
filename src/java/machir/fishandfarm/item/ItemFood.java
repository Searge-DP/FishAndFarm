package machir.fishandfarm.item;

import java.util.List;

import machir.fishandfarm.ModInfo;
import machir.fishandfarm.util.Localization;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFood extends ItemFishAndFarm {
	// Array of food icons
	private Icon[] icons;
	
	// Array of food names with their properties
	private static String[] foodNames = {"eggFried", "sliceBread", "lettuce", "tomato", "strawberry", "breadBaconEgg"};
	private static float[] saturation = {0.2F, 0.2F, 0.3F, 0.25F, 0.1F, 0.5F};
	private static int[] healAmount = {4, 2, 4, 2, 1, 6};
	private static boolean[] wolfFav = {false, false, false, false, false, false};
	
	public ItemFood(int id) {
		super(id);
		setMaxDamage(0);
		setHasSubtypes(true);
		setCreativeTab(CreativeTabs.tabFood);
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
		for (int i = 0; i < foodNames.length; i++) {
			list.add(new ItemStack(id, 1, i));
		}
	}
	
	/**
	 * Add health and saturation depending on damage value
	 * 
	 * @param itemstack The eaten itemstack
	 * @param world The world
	 * @param entityPlayer The player entity
	 */
	@Override
	public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer entityPlayer) {
        --itemstack.stackSize;
        entityPlayer.getFoodStats().addStats(healAmount[itemstack.getItemDamage()], saturation[itemstack.getItemDamage()]);
        applyPotionEffect(itemstack, world, entityPlayer);
        world.playSoundAtEntity(entityPlayer, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        return itemstack;
    }
	
	/**
	 * Applies a corresponding potion effect if needed
	 */
	public void applyPotionEffect(ItemStack itemstack, World world, EntityPlayer entityPlayer) {
	    
	}
	
    /**
     * How long it takes to use or consume an item
     * 
     * @param itemstack The used itemstack
     */
	@Override
    public int getMaxItemUseDuration(ItemStack itemstack)
    {
        return 32;
    }
	
	/**
     * returns the action that specifies what animation to play when the items is being used
     * 
     * @param itemstack The used itemstack
     */
	@Override
    public EnumAction getItemUseAction(ItemStack itemstack)
    {
        return EnumAction.eat;
    }
    
    /**
     * Called whenever this item is equipped and the right mouse button is pressed. 
 	 * 
 	 * @param itemStack The used itemstack
 	 * @param world The world
 	 * @param entityPlayer The player entity
     */
	@Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityPlayer)
    {
        if (entityPlayer.canEat(false))
        {
        	entityPlayer.setItemInUse(itemstack, this.getMaxItemUseDuration(itemstack));
        }

        return itemstack;
    }
	
    /**
     * allows items to add custom lines of information to the mouseover description
     *
     * @param itemStack The corresponding itemstack
     * @param entityPlayer The player holding the item
     * @param desc The list of description lines
     * @param adv Advanced tooltips on?
     */
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List desc, boolean adv) {
        String localizedName = LanguageRegistry.instance().getStringLocalization("item." + ModInfo.MODID + "." + ModInfo.UNLOC_NAME_ITEM_FOOD + "." + itemStack.getItemDamage() + ".desc");
        if (!localizedName.equals("")) {
            String[] lines = localizedName.split("\n");
            for (String line : lines) {
                desc.add(line);
            }
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
		icons = new Icon[foodNames.length];
		int i = 0;
		for (String foodName : foodNames) {
			icons[i] = register.registerIcon(ModInfo.MODID + ":" + foodName);
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
		return "item." + ModInfo.MODID + "." + ModInfo.UNLOC_NAME_ITEM_FOOD + "." + itemStack.getItemDamage();
    }
}
