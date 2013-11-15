package machir.fishandfarm.item;

public class ItemDamagable extends ItemFishAndFarm {

	public ItemDamagable(int id, int maxUses) {
		super(id);
		this.setMaxDamage(maxUses);
		this.maxStackSize = 1;
	}
}
