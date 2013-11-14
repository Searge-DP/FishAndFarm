package machir.fishandfarm.handler;

import machir.fishandfarm.FishAndFarm;
import machir.fishandfarm.block.BlockLettuceCrop;
import machir.fishandfarm.block.BlockStrawberryCrop;
import machir.fishandfarm.block.BlockTomatoCrop;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.BonemealEvent;

public class BonemealHandler {
	@ForgeSubscribe
	public void onUseBonemeal(BonemealEvent event)
	{
		if (event.ID == FishAndFarm.lettuceCrop.blockID)
		{
			if (!event.world.isRemote)
			{
				((BlockLettuceCrop)FishAndFarm.lettuceCrop).fertilize(event.world, event.X, event.Y, event.Z);
                if (!event.world.isRemote)
                {
                	event.world.playAuxSFX(2005, event.X, event.Y, event.Z, 0);
                }
			}
		}
		if (event.ID == FishAndFarm.tomatoCrop.blockID)
		{
			if (!event.world.isRemote)
			{
				((BlockTomatoCrop)FishAndFarm.tomatoCrop).fertilize(event.world, event.X, event.Y, event.Z);
                if (!event.world.isRemote)
                {
                	event.world.playAuxSFX(2005, event.X, event.Y, event.Z, 0);
                }
			}
		}
		if (event.ID == FishAndFarm.strawberryCrop.blockID)
		{
			if (!event.world.isRemote)
			{
				((BlockStrawberryCrop)FishAndFarm.strawberryCrop).fertilize(event.world, event.X, event.Y, event.Z);
                if (!event.world.isRemote)
                {
                	event.world.playAuxSFX(2005, event.X, event.Y, event.Z, 0);
                }
			}
		}
	}
}
