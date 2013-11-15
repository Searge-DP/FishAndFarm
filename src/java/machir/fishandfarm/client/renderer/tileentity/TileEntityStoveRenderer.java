package machir.fishandfarm.client.renderer.tileentity;

import machir.fishandfarm.ModInfo;
import machir.fishandfarm.client.model.ModelFryingPan;
import machir.fishandfarm.client.model.ModelStove;
import machir.fishandfarm.tileentity.TileEntityStove;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

public class TileEntityStoveRenderer extends TileEntitySpecialRenderer {
	private ModelStove stove;
	private ModelFryingPan fryingPan;

    public TileEntityStoveRenderer()
    {
    	stove = new ModelStove();
    	fryingPan = new ModelFryingPan();
    }

    public void renderModelAt(TileEntityStove tileEntityStove, double d, double d1, double d2, float f)
    {  	
        int i;

        if (tileEntityStove.worldObj == null)
        {
            i = 0;
        }
        else
        {
            Block block = tileEntityStove.getBlockType();
            i = tileEntityStove.getBlockMetadata();
        }

        int j = 180;
   
        if (i == 2)
        {
            j = 180;
        }

        if (i == 3)
        {
            j = 0;
        }

        if (i == 4)
        {
            j = -90;
        }

        if (i == 5)
        {
            j = 90;
        }

        // Start
        GL11.glPushMatrix();
        
        // Positioning
        GL11.glTranslatef((float)d + 0.5F, (float)d1 + 1.5F, (float)d2 + 0.5F);
        GL11.glRotatef(j, 0.0F, 1.0F, 0.0F);
        
        if (tileEntityStove.worldObj != null && tileEntityStove.tool != null) {
    		switch (tileEntityStove.tool) {
    		case FRYINGPAN:
    	        // Set texture
    	        this.bindTexture(ModInfo.FRYINGPAN_MODEL_TEXTURE);
    	        
    	        // Render the stove
                renderStove();
    	        
                // Render the fryingPan
                renderFryingPan();
                
				break;
				
    			default: 
				break;
    		}
        } else {
        	// Set texture
            this.bindTexture(ModInfo.STOVE_MODEL_TEXTURE);
            renderStove();
        }
        
        // Fix textures if it's placed
        if (tileEntityStove.worldObj != null) {
        	this.bindTexture(TextureMap.locationBlocksTexture);
        }
        
        // Stop
        GL11.glPopMatrix();
    }
    
    public void renderStove() {
        // Start for rotation and rendering stove
        GL11.glPushMatrix();
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
	    stove.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		
	    // Stop rotation and rendering stove
        GL11.glPopMatrix();
    }
    
    public void renderFryingPan() {
        // Start for rotation and rendering frying pan
        GL11.glPushMatrix();
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        
        // Render! :3
		fryingPan.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		
	    // Stop rotation and rendering stove
        GL11.glPopMatrix();
    }

    public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
    {
        renderModelAt((TileEntityStove)tileentity, d, d1, d2, f);
    }
}

