package machir.fishandfarm.item;

import machir.fishandfarm.item.crafting.EnumStoveToolType.StoveToolType;
import machir.fishandfarm.item.crafting.EnumStoveToolType;
import machir.fishandfarm.item.crafting.IStoveTool;
import net.minecraft.creativetab.CreativeTabs;

public class ItemFryingPan extends ItemDamagable implements IStoveTool {
    // Max 30 uses, -1 to destroy on damage 30
    private static int maxUses = (30 - 1);
    
    public ItemFryingPan(int id) {
        super(id, maxUses);
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public StoveToolType getToolType() {
        return StoveToolType.FRYINGPAN;
    }
}