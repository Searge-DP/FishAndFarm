package machir.fishandfarm.block;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import machir.fishandfarm.ModInfo;
import machir.fishandfarm.util.Localization;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockFishAndFarm extends Block {
	
	/**
	 * No material specified auto-sets it to Material.rock
	 * 
	 * @param id The block ID
	 */
	public BlockFishAndFarm(int id) {
		super(id, Material.rock);
	}
	
	/**
	 * Material specified
	 * 
	 * @param id The block ID
	 * @param material The block material
	 */	
	protected BlockFishAndFarm(int id, Material material) {
		super(id, material);
	}
	
	/**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
	 * 
	 * @param register An object implementing IconRegister
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register) {
		this.blockIcon = register.registerIcon(ModInfo.MODID + ":" + this.getTextureName());
	}	
	
	/**
	 * Returns the unlocalized name
	 * 
	 * @param itemstack The corresponding itemstack
	 */
	@Override
	public String getUnlocalizedName() {
		return Localization.get(this.getTextureName());
	}
}
