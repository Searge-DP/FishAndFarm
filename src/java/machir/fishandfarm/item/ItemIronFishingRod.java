package machir.fishandfarm.item;

import machir.fishandfarm.FishAndFarm;
import machir.fishandfarm.ModInfo;
import machir.fishandfarm.entity.EntityIronFishHook;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemIronFishingRod extends ItemFishAndFarm {
	@SideOnly(Side.CLIENT)
    private Icon castIcon;

    public ItemIronFishingRod(int id)
    {
        super(id);
        this.setMaxDamage(128);
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    @SideOnly(Side.CLIENT)
    @Override
    public boolean isFull3D()
    {
        return true;
    }

    /**
     * Returns true if this item should be rotated by 180 degrees around the Y axis when being held in an entities
     * hands.
     */
    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldRotateAroundWhenRendering()
    {
        return true;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. 
 	 * 
 	 * @param itemStack The used itemstack
 	 * @param world The world
 	 * @param entityPlayer The player entity
     */
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
    {
    	// If the rod is cast, return it and check if a fish has been caught
        if (entityPlayer.fishEntity != null)
        {
            int i = entityPlayer.fishEntity.catchFish();
            itemStack.damageItem(i, entityPlayer);
            entityPlayer.swingItem();
        }
        else
        {
        	// Cast the rot
        	world.playSoundAtEntity(entityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        	// On server side, spawn a hook
            if (!world.isRemote)
            {
            	world.spawnEntityInWorld(new EntityIronFishHook(world, entityPlayer));
            }

            entityPlayer.swingItem();
        }

        return itemStack;
    }

	/**
     * When this method is called, your item should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
	 * 
	 * @param register An object implementing IconRegister
	 */
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister register)
    {
        this.itemIcon = register.registerIcon(ModInfo.MODID + ":" + this.getIconString() + "_uncast");
        this.castIcon = register.registerIcon(ModInfo.MODID + ":" + this.getIconString() + "_cast");
    }

    /**
     * Player, Render pass, and item usage sensitive version of getIconIndex.
     *
     * @param stack The item stack to get the icon for. (Usually this, and usingItem will be the same if usingItem is not null)
     * @param renderPass The pass to get the icon for, 0 is default.
     * @param player The player holding the item
     * @param usingItem The item the player is actively using. Can be null if not using anything.
     * @param useRemaining The ticks remaining for the active item.
     * @return The icon index
     */
    @Override
    public Icon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
    {
        // If the rod is cast, change the icon
        return (stack.itemID == FishAndFarm.ironFishingRod.itemID && player.fishEntity != null) ? this.castIcon : this.itemIcon; 
    }
}
