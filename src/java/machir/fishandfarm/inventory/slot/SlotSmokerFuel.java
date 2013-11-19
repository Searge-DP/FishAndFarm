package machir.fishandfarm.inventory.slot;

import machir.fishandfarm.tileentity.TileEntitySmoker;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotSmokerFuel extends Slot {
	private TileEntitySmoker tileEntity;
	
	public SlotSmokerFuel(IInventory inventory, int x, int y, int z)
    {
        super(inventory, x, y, z);
        tileEntity = (TileEntitySmoker)inventory;
    }
	
	@Override
    public boolean isItemValid(ItemStack itemstack)
    {
        return tileEntity.isItemFuel(itemstack);
    }
}
