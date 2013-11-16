package machir.fishandfarm.inventory.slot;

import machir.fishandfarm.item.crafting.IStoveTool;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;

public class SlotTool extends Slot {

    public SlotTool(IInventory inventory, int x, int y, int z)
    {
        super(inventory, x, y, z);
    }

    @Override
    public boolean isItemValid(ItemStack itemstack)
    {
        return (itemstack.getItem() instanceof IStoveTool);
    }
}
