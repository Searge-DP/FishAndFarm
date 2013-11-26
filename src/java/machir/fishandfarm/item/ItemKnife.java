package machir.fishandfarm.item;

import machir.fishandfarm.ModInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;

import com.google.common.collect.Multimap;

public class ItemKnife extends ItemFishAndFarm {
	// Max 30 uses, -1 to destroy on damage 30
	private int maxUses = (30 - 1); 
	
	public ItemKnife(int id) {
		super(id);
		this.maxStackSize = 1;
		this.setMaxDamage(this.maxUses);
        this.setCreativeTab(CreativeTabs.tabTools);
        this.bFull3D = true;
        this.setContainerItem(this);
	}
	
	
    /**
     * If this returns true, after a recipe involving this item is crafted the container item will be added to the
     * player's inventory instead of remaining in the crafting grid.
     */
	@Override
	public boolean doesContainerItemLeaveCraftingGrid(ItemStack itemStack) {
	    return false;
	}
	
    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     * 
     * @param itemstack The used itemstack
     * @param livingEntityBase The entity using the itemstack
     * @param livingEntityBase2 The hit entity 
     */
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase livingEntityBase, EntityLivingBase livingEntityBase2)
    {
    	itemstack.damageItem(1, livingEntityBase2);
        return true;
    }
    
    /**
     * Gets a map of item attribute modifiers, used to increase hit damage.
     */
    public Multimap getItemAttributeModifiers()
    {
    	// Small damage boost as it can be used as a weapon
        Multimap multimap = super.getItemAttributeModifiers();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", 1, 0));
        return multimap;
    }
    
    @Override
    public ItemStack getContainerItemStack(ItemStack itemStack)
    {
        int newDamage = itemStack.getItemDamage() + 1;
        itemStack.setItemDamage(newDamage);
        return itemStack;
    }
}
