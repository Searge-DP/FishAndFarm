package machir.fishandfarm.client.renderer.tileentity;

import machir.fishandfarm.ModInfo;
import machir.fishandfarm.client.model.ModelCage;
import machir.fishandfarm.tileentity.TileEntityCage;
import machir.fishandfarm.tileentity.TileEntitySmoker;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class TileEntityCageRenderer extends TileEntitySpecialRenderer implements IItemRenderer {
    private ModelCage cage;

    public TileEntityCageRenderer()
    {
        cage = new ModelCage();
    }

    public void renderModelAt(TileEntityCage tileEntityCage, double d, double d1, double d2, float f)
    {          
        // Start
        GL11.glPushMatrix();
        
        // Positioning
        GL11.glTranslatef((float)d, (float)d1 + 1.5F, (float)d2 + 1.0F);
        GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);

        // Render the cage
        renderCage();
        
        // Fix textures if it's placed
        if (tileEntityCage.worldObj != null) {
            this.bindTexture(TextureMap.locationBlocksTexture);
        }
        
        // Stop
        GL11.glPopMatrix();
    }
    
    public void renderCage() {
        // Set texture
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(ModInfo.CAGE_MODEL_TEXTURE);
        
        // Start for rotation and rendering stove
        GL11.glPushMatrix();
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        cage.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
            
        // Stop rotation and rendering stove
        GL11.glPopMatrix();
    }
    
    public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
    {
        renderModelAt((TileEntityCage)tileentity, d, d1, d2, f);
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
                
                // Render the cage
                renderCage();
                
                // Stop the model render
                GL11.glPopMatrix();
                
                // Stop
                GL11.glPopMatrix();
                break;
            case EQUIPPED:
                // Start
                GL11.glPushMatrix();
                
                // Positioning
                GL11.glTranslatef(1.0F, 1.5F, 1.0F);
                
                // Start the model render
                GL11.glPushMatrix();
                
                // Rotation
                GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                
                // Render the smoker cage
                renderCage();
                
                // Stop the model render
                GL11.glPopMatrix();
                
                // Stop
                GL11.glPopMatrix();
                break;
            case INVENTORY:
                // Start
                GL11.glPushMatrix();
                
                // Re-scale for inventory slot
                GL11.glScalef(1.0F, 1.0F, 1.0F);
                
                // Positioning
                GL11.glTranslatef(0.0F, 0.6F, -1.0F);
                
                // Start the model render
                GL11.glPushMatrix();
                
                // Rotation
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                
                // Render the smoker cage
                renderCage();
                
                // Stop the model render
                GL11.glPopMatrix();
                
                // Stop
                GL11.glPopMatrix();
                break;
            case EQUIPPED_FIRST_PERSON:
                // Start
                GL11.glPushMatrix();
                
                // Re-scale for 1st person
                GL11.glScalef(1.0F, 1.0F, 1.0F);
                
                // Positioning
                GL11.glTranslatef(1.0F, 1.6F, 1.0F);
                
                // Start the model render
                GL11.glPushMatrix();
                
                // Rotation
                GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                
                // Render the cage
                renderCage();
                
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
