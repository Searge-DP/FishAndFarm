package machir.fishandfarm.item;

import java.util.List;

import machir.fishandfarm.ModInfo;
import machir.fishandfarm.util.Localization;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFishAndFarm extends Item {
	
	public ItemFishAndFarm(int id) {
		super(id);
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
    	if (!Localization.get("fishandfarm." + this.getIconString() + ".desc").equals(
    			"fishandfarm." + this.getIconString() + ".desc")) {
    		String[] lines = Localization.get("fishandfarm." + this.getIconString() + ".desc").split("\n");
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
		this.itemIcon = register.registerIcon(ModInfo.MODID + ":" + this.getIconString());
	}
	
	/**
	 * Returns the unlocalized name
	 * 
	 * @param itemstack The corresponding itemstack
	 */
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return Localization.get(ModInfo.MODID + "." + this.getIconString() + ".name");
	}
	
	/**
	 * Returns the item name for display
	 * 
	 * @param itemstack The corresponding itemstack
	 */
	@Override
	public String getItemDisplayName(ItemStack itemstack) {
		return Localization.get(getUnlocalizedName(itemstack));
	}
}
