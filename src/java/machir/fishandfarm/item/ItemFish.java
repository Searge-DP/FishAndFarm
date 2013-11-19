package machir.fishandfarm.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import machir.fishandfarm.ModInfo;
import machir.fishandfarm.util.Localization;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class ItemFish extends ItemFood {
    // Array of fish icons
    private Icon[] icons;
    
    // Array of fish names with their properties
    private static String[] fishNames = {"fish", "carp", "tuna", "salmon", "carpFried", "tunaFried", "salmonFried"};
    private static float[] saturation = {0.2F, 0.2F, 0.2F, 0.2F, 0.4F, 0.4F, 0.4F};
    private static int[] healAmount = {2, 4, 4, 4, 7, 8, 11};
    private static boolean[] wolfFav = {false, false, false, false, false, false, false};
    
    public ItemFish(int id) {
        super(id);
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
        for (int i = 0; i < fishNames.length; i++) {
            list.add(new ItemStack(id, 1, i));
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
        icons = new Icon[fishNames.length];
        int i = 0;
        for (String fishName : fishNames) {
            icons[i] = register.registerIcon(ModInfo.MODID + ":" + fishName);
            i++;
        }
    }
    
    /**
     * Returns the unlocalized name of the itemstack
     * 
     * @param itemstack The corresponding itemstack
     */
    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        return "fishandfarm." + ModInfo.UNLOC_NAME_ITEM_FISH + ".name." + itemStack.getItemDamage();
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
        if (!Localization.get("fishandfarm." + ModInfo.UNLOC_NAME_ITEM_FISH + ".desc." + itemStack.getItemDamage()).equals(
                "fishandfarm." + ModInfo.UNLOC_NAME_ITEM_FISH + ".desc." + itemStack.getItemDamage())) {
            String[] lines = Localization.get("fishandfarm." + ModInfo.UNLOC_NAME_ITEM_FISH + ".desc." + itemStack.getItemDamage()).split("\n");
            for (String line : lines) {
                desc.add(line);
            }
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
}
