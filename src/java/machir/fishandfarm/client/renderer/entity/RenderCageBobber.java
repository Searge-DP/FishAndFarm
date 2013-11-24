package machir.fishandfarm.client.renderer.entity;

import machir.fishandfarm.ModInfo;
import machir.fishandfarm.entity.EntityCageBobber;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCageBobber extends Render {
    /**
     * Actually renders the fishing line and hook
     */
    public void doRenderCageBobber(EntityCageBobber entityCageBobber, double x, double y, double z, float yaw, float partialTickTime)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y + 0.1F, (float)z);
        this.bindTexture(ModInfo.CAGE_BOBBER_TEXTURE);
        Tessellator tessellator = Tessellator.instance;
        byte b0 = 1;
        byte b1 = 2;
        
        // Coord used to change between catch and no catch
        float xCoord = (entityCageBobber.cage.hasCatch() ? 1.0F : 0.5F);
        
        // Texture corners in file
        float f6 = 1.0F;
        float f7 = 0.5F;
        float f8 = 0.5F;
        float u1 = xCoord - 0.5F;
        float v1 = 0.0F;
        float u2 = xCoord;
        float v2 = 0.0F;
        float u3 = xCoord;
        float v3 = 1.0F;
        float u4 = xCoord - 0.5F;
        float v4 = 1.0F;
        GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 0.0F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        tessellator.addVertexWithUV((double)(0.0F - 0.5F), (double)(-0.5F), 0.0D, u1, v1);
        tessellator.addVertexWithUV((double)(1.0F - 0.5F), (double)(-0.5F), 0.0D, u2, v2);
        tessellator.addVertexWithUV((double)(1.0F - 0.5F), (double)(0.5F), 0.0D, u3, v3);
        tessellator.addVertexWithUV((double)(0.0F - 0.5F), (double)(0.5F), 0.0D, u4, v4);
        tessellator.draw();
        GL11.glPopMatrix();
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTickTime)
    {
        this.doRenderCageBobber((EntityCageBobber)entity, x, y, z, yaw, partialTickTime);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }
}
