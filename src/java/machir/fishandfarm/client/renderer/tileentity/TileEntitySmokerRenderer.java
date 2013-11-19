package machir.fishandfarm.client.renderer.tileentity;

import machir.fishandfarm.ModInfo;
import machir.fishandfarm.client.model.ModelSmoker;
import machir.fishandfarm.tileentity.TileEntitySmoker;
import machir.fishandfarm.tileentity.TileEntityStove;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class TileEntitySmokerRenderer extends TileEntitySpecialRenderer implements IItemRenderer {
	private ModelSmoker smoker;

    public TileEntitySmokerRenderer()
    {
		smoker = new ModelSmoker();
    }

    public void renderModelAt(TileEntitySmoker tileEntitySmoker, double d, double d1, double d2, float f)
    {          
        int i;

        if (tileEntitySmoker.worldObj == null)
        {
            i = 0;
        }
        else
        {
            Block block = tileEntitySmoker.getBlockType();
            i = tileEntitySmoker.getBlockMetadata();
        }

        int j = 180;
        float xOffset = 1.0F;
        float zOffset = 0.0F;
   
        if (i == 1)
        {
            j = 180;
        }

        if (i == 2)
        {
            j = 0;
            xOffset = 0.0F;
            zOffset = 1.0F;
        }

        if (i == 3)
        {
            j = -90;
            xOffset = 0.0F;
            zOffset = 0.0F;
        }

        if (i == 4)
        {
            j = 90;
            xOffset = 1.0F;
            zOffset = 1.0F;
        }

        // Start
        GL11.glPushMatrix();
        
        // Positioning
        GL11.glTranslatef((float)d + xOffset, (float)d1 + 1.5F, (float)d2 + zOffset);
        GL11.glRotatef(j, 0.0F, 1.0F, 0.0F);

        // Render the smoker
        renderSmoker();
        
        // Fix textures if it's placed
        if (tileEntitySmoker.worldObj != null) {
            this.bindTexture(TextureMap.locationBlocksTexture);
        }
        
        // Stop
        GL11.glPopMatrix();
    }
    
    public void renderSmoker() {
        // Set texture
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(ModInfo.SMOKER_MODEL_TEXTURE);
        
        // Start for rotation and rendering stove
        GL11.glPushMatrix();
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        smoker.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
            
        // Stop rotation and rendering stove
        GL11.glPopMatrix();
    }
    
    public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
    {
        renderModelAt((TileEntitySmoker)tileentity, d, d1, d2, f);
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
            return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
                    ItemRendererHelper helper) {
            return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
            switch(type) {
            case ENTITY:
	            // Start
	            GL11.glPushMatrix();
	            
	            // Positioning
	            GL11.glTranslatef(-0.5F, 1.0F, 0.5F);
	            
	            // Start the model render
	            GL11.glPushMatrix();
	            
	            // Rotation
	            GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
	            
	            // Render the smoker
                renderSmoker();
                
                // Stop the model render
                GL11.glPopMatrix();
                
                // Stop
                GL11.glPopMatrix();
                break;
            case EQUIPPED:
                // Start
	            GL11.glPushMatrix();
	            
	            // Positioning
	            GL11.glTranslatef(-0.0F, 1.5F, 1.0F);
	            
	            // Start the model render
	            GL11.glPushMatrix();
	            
	            // Rotation
	            GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
	            
	            // Render the smoker
	            renderSmoker();
                
                // Stop the model render
                GL11.glPopMatrix();
                
                // Stop
                GL11.glPopMatrix();
                break;
            case INVENTORY:
                // Start
		        GL11.glPushMatrix();
		        
		        // Re-scale for inventory slot
		        GL11.glScalef(0.8F, 0.8F, 0.8F);
		        
		        // Positioning
		        GL11.glTranslatef(-3.0F, -1.3F, -2.0F);
		        
		        // Start the model render
		        GL11.glPushMatrix();
		        
		        // Rotation
		        GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
		        
		        // Render the smoker
		        renderSmoker();
                
                // Stop the model render
                GL11.glPopMatrix();
                
                // Stop
                GL11.glPopMatrix();
                break;
            case EQUIPPED_FIRST_PERSON:
                // Start
                GL11.glPushMatrix();
                
                // Re-scale for inventory slot
                GL11.glScalef(1.0F, 1.0F, 1.0F);
                
                // Positioning
                GL11.glTranslatef(1.0F, 1.0F, 1.0F);
                
                // Start the model render
                GL11.glPushMatrix();
                
                // Rotation
                GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
                
                // Render the smoker
                renderSmoker();
                
                // Stop the model render
                GL11.glPopMatrix();
                
                // Stop
                GL11.glPopMatrix();
                break;
            default:
                    break;
            }
            
    }
}
