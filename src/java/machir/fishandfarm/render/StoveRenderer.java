package machir.fishandfarm.render;

import machir.fishandfarm.FishAndFarm;
import machir.fishandfarm.tileentity.TileEntityStove;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class StoveRenderer implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId,
			RenderBlocks renderer) {		
		if (modelId == this.getRenderId()) {
			TileEntityRenderer.instance.renderTileEntityAt(new TileEntityStove(), 0.0D, 0.0D, 0.0D, 0.0F);
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		if(modelId == this.getRenderId()) {
    		TileEntityRenderer.instance.renderTileEntityAt(world.getBlockTileEntity(x, y, z), x, y, z, 0.0F);
		}
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		return true;
	}

	@Override
	public int getRenderId() {
		return FishAndFarm.stoveRenderID;
	}
}
