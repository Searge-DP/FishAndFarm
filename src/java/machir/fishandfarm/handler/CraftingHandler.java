package machir.fishandfarm.handler;

import machir.fishandfarm.FishAndFarm;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.ICraftingHandler;

public class CraftingHandler implements ICraftingHandler {

	/**
	 * Gets called on craft event
	 * 
	 * @param entityPlayer The player entity which is crafting
	 * @param itemStack The itemstack being made
	 * @param craftMatrix The crafting matrix
	 */
	@Override
	public void onCrafting(EntityPlayer entityPlayer, ItemStack itemStack, IInventory craftMatrix) {
		for (int i = 0; i < craftMatrix.getSizeInventory(); i++) {
			if (craftMatrix.getStackInSlot(i) != null) {
				ItemStack slotStack = craftMatrix.getStackInSlot(i);
				// If a knife is used, damage it by 1
				if (slotStack.itemID == FishAndFarm.knife.itemID) {
					slotStack.damageItem(1, entityPlayer);
					if (slotStack.stackSize > 0) {
						slotStack.stackSize++;
					}
					return;
				}
			}
		}
	}

	/**
	 * Gets called on smelt event
	 * 
	 * @param entityPlayer The player entity which is smelting
	 * @param itemStack The itemstack being made
	 */
	@Override
	public void onSmelting(EntityPlayer entityPlayer, ItemStack itemStack) {
	}

}
