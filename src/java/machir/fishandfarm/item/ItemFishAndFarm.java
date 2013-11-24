package machir.fishandfarm.item;

import java.util.List;

import machir.fishandfarm.ModInfo;
import machir.fishandfarm.util.Localization;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.LanguageRegistry;
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
        String localizedName = LanguageRegistry.instance().getStringLocalization("item." + ModInfo.MODID + "." + this.getUnlocalizedName() + ".desc");
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
		this.itemIcon = register.registerIcon(ModInfo.MODID + ":" + this.getIconString());
	}
}
