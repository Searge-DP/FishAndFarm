package machir.fishandfarm.item;

import machir.fishandfarm.ModInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;

import com.google.common.collect.Multimap;

public class ItemKnife extends ItemDamagable {
	// Max 30 uses, -1 to destroy on damage 30
	private static int maxUses = (30 - 1); 
	
	public ItemKnife(int id) {
		super(id, maxUses);
        this.setCreativeTab(CreativeTabs.tabTools);
        this.bFull3D = true;
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
}
