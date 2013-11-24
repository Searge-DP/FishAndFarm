package machir.fishandfarm.item;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import machir.fishandfarm.ModInfo;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFish extends ItemFood {
    // Array of fish icons
    private Icon[] icons;
    
    // Array of fish names with their properties
    private static String[] fishNames = {"fish", "fishCooked", "carp", "tuna", "salmon", "carpFried", "tunaFried", "salmonFried", "crab", "crabPeeled", "lobster", "lobsterPeeled", "carpSliced", "TunaSliced", "salmonSliced", "salmonSmoked"};
    private static float[] saturation = {0.3F, 0.6F, 0.2F, 0.2F, 0.2F, 0.4F, 0.4F, 0.4F, 0.0F, 0.5F, 0.0F, 0.5F, 0.2F, 0.2F, 0.2F, 1.0F};
    private static int[] healAmount = {2, 5, 4, 4, 4, 7, 8, 8, 0, 3, 0, 4, 4, 4, 4, 11};
    private static boolean[] wolfFav = {false, false, false, false, false, false, false, false, false, false, false, false, false, false};
    
    public ItemFish(int id) {
        super(id);
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
    @Override
    public void applyPotionEffect(ItemStack itemstack, World world, EntityPlayer entityPlayer) {
        switch (itemstack.getItemDamage()) {
        case 7:
        case 9:
            entityPlayer.addPotionEffect(new PotionEffect(7, 1, 0));
            break;
        default:
            break;
        }
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
        return "item." + ModInfo.MODID + "." + ModInfo.UNLOC_NAME_ITEM_FISH + "." + itemStack.getItemDamage();
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
        String localizedName = LanguageRegistry.instance().getStringLocalization("item." + ModInfo.MODID + "." + ModInfo.UNLOC_NAME_ITEM_FISH + "." + itemStack.getItemDamage() + ".desc");
        if (!localizedName.equals("")) {
            String[] lines = localizedName.split("\\\\n");
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
